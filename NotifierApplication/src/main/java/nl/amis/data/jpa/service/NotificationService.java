package nl.amis.data.jpa.service;

import nl.amis.data.jpa.domain.Notification;

import java.time.Instant;
import java.util.List;

public interface NotificationService {

	List<Notification> getNotifications(final String channelId, final Instant instant);
}
