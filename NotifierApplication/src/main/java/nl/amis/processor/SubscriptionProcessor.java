package nl.amis.processor;

import lombok.NoArgsConstructor;
import nl.amis.NotifierApplication;
import nl.amis.data.jpa.service.NotificationServiceImpl;
import nl.amis.data.jpa.service.SubscriptionService;
import nl.amis.data.jpa.service.VaultService;
import nl.amis.data.jpa.domain.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Component
@NoArgsConstructor
public class SubscriptionProcessor implements Runnable {

    ScheduledExecutorService executorService = Executors.newScheduledThreadPool(5);

    @Autowired
    SubscriptionService subscriptionService;

    @Autowired
    NotificationServiceImpl notificationService;

    @Autowired
    VaultService vaultService;

    final Map<Subscription, ChannelProcessor> channelProcessors = new ConcurrentHashMap<>();

    private  Map<Subscription, ChannelProcessor> getProcessorsForActiveSubscriptions() {

        final Iterable<Subscription> iterable = subscriptionService.getActive(NotifierApplication.API);
        final Map<String, Channel> channels = determineChannelMap(iterable);
        final Map<Subscription, ChannelProcessor> result = createChannelProcessors(channels, iterable);
        return result;
    }

    private  Map<Subscription, ChannelProcessor> getProcessorsForAddedSubscriptions() {

        final Optional<Instant>  mostRecent = determineMostRecent(channelProcessors.keySet());
        final Iterable<Subscription> iterable = subscriptionService.getAdded(NotifierApplication.API, mostRecent.orElse(Instant.EPOCH));
        final Map<String, Channel> channels = determineChannelMap(iterable);
        final Map<Subscription, ChannelProcessor> result = createChannelProcessors(channels, iterable);
        return result;
    }

    private  Map<Subscription, ChannelProcessor> getProcessorsForEndedSubscriptions() {

        final Optional<Instant> lastEnded = determineLastEnded(channelProcessors.keySet());
        final Map<Subscription, ChannelProcessor> result = new HashMap<>();
        final Iterable<Subscription> iterable = subscriptionService.getEnded(NotifierApplication.API, lastEnded.orElse(Instant.EPOCH));
        iterable.forEach(subscription -> {
            final ChannelProcessor channelProcessor = channelProcessors.get(subscription);
            if (channelProcessor != null) {
                result.put(subscription, channelProcessor);
            }
        });
        return result;
    }

    private Optional<Instant> determineMostRecent(Set<Subscription> subscriptions) {
        return subscriptions.stream().map(Subscription::getStart).max(Instant::compareTo);
    }

    private Optional<Instant> determineLastEnded(Set<Subscription> subscriptions) {
        //return subscriptions.stream().map(Subscription::getEnd).max(Instant::compareTo).get();
        return subscriptions.stream().filter(subscription -> subscription.getEnd() != null).map(Subscription::getUnsubscribedAt).max(Instant::compareTo);
    }


    private Map<String, Channel> determineChannelMap(final Iterable<Subscription> subscriptions) {

        final Map<String, Channel> channels = new HashMap<>();
        subscriptions.forEach(subscription -> {
            final String secret = subscription.getSubscriber();
            if (!channels.containsKey(secret)) {
                final Channel channel = new Channel(secret, vaultService.getSecret(secret));
                channels.put(secret, channel);
            }
        });
        return channels;
    }

    private Map<Subscription, ChannelProcessor> createChannelProcessors(final Map<String, Channel> channels, final Iterable<Subscription> iterable) {

        final Stream<Subscription> subscriptionsStream = StreamSupport.stream(iterable.spliterator(), false);
        final Map<Subscription, ChannelProcessor> result = new HashMap<>();
        subscriptionsStream.forEach(subscription -> {
            final Channel channel = channels.get(subscription.getSubscriber());
            if (channel != null) {
                final ChannelProcessor channelProcessor =
                        new ChannelProcessor(channel, notificationService);
                result.put(subscription, channelProcessor);
            }
        });
        return result;
    }

    private void sendNotifications() {
        final Map<Subscription, ChannelProcessor> added = getProcessorsForAddedSubscriptions();
        if (!added.isEmpty()) {
            channelProcessors.putAll(added);
        }
        final Map<Subscription, ChannelProcessor> ended = getProcessorsForEndedSubscriptions();
        if (!ended.isEmpty()) {
            ended.values().forEach(channelProcessor -> channelProcessors.remove(channelProcessor));
        }
        if (!channelProcessors.isEmpty()) {
            channelProcessors.values().forEach(channelProcessor -> {
                System.out.println("channel = " + channelProcessor.getChannel().getChannelId());
                channelProcessor.run();
            });
        }
        return;
    }


    @Override
    public void run() {

        channelProcessors.putAll(getProcessorsForActiveSubscriptions());
        executorService.scheduleAtFixedRate(() -> sendNotifications(), 0, 15, TimeUnit.SECONDS);

    }

    @PreDestroy
    public void preDestroy() {

        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
}
