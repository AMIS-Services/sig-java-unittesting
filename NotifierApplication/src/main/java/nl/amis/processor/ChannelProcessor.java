package nl.amis.processor;

import lombok.Getter;
import nl.amis.data.jpa.domain.Notification;
import nl.amis.data.jpa.service.NotificationService;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public class ChannelProcessor implements Runnable {

    private Instant instant = Instant.EPOCH;

    private NotificationService notificationService;

    @Getter
    final Channel channel;

    public ChannelProcessor(final Channel channel, final NotificationService notificationService) {

        this.channel = channel;
        this.notificationService = notificationService;
    }

    @Override
    public void run() {
        // poll for notifications on this channel and publish
        final List<Notification> notificationsList = notificationService.getNotifications(channel.getChannelId(), instant);
        final Optional<Instant> optional = notificationsList.stream().filter(notification -> notification.getCreatedAt() != null).map(Notification::getCreatedAt).max(Instant::compareTo);
        if (optional.isPresent()) instant = optional.get();
        notificationsList.forEach(notification -> System.out.println(notification.getMessage()));
    }
}
