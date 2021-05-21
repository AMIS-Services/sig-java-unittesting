package nl.amis.data.jpa.service;

import nl.amis.data.jpa.domain.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Component("subscriptionService")
@Transactional
public class SubscriptionServiceImpl implements SubscriptionService {

	@Autowired
	SubscriptionRepository subscriptionRepository;

	@Override
	public List<Subscription> getActive (String channel) {
		final List<Subscription> list = new ArrayList<Subscription>();
		subscriptionRepository.findAllActive(channel).forEach(list::add);
		return list;
	}

	@Override
	public List<Subscription> getAdded (String channel, Instant instant) {
		final List<Subscription> list = new ArrayList<Subscription>();
		subscriptionRepository.findNewActive(channel, instant).forEach(list::add);
		return list;
	}

	@Override
	public List<Subscription> getEnded (String channel, Instant instant) {
		final List<Subscription> list = new ArrayList<Subscription>();
		subscriptionRepository.findEnded(channel, instant).forEach(list::add);
		return list;
	}
}
