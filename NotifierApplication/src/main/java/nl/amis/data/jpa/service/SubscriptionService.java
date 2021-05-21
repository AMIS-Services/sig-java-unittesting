package nl.amis.data.jpa.service;

import nl.amis.data.jpa.domain.Notification;
import nl.amis.data.jpa.domain.Subscription;

import java.time.Instant;
import java.util.List;

public interface SubscriptionService {

	List<Subscription> getActive(String channel);

	List<Subscription> getAdded (String channel, Instant instant);

	List<Subscription> getEnded (String channel, Instant instant);

}
