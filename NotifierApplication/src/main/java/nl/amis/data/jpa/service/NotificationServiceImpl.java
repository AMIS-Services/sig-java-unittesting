package nl.amis.data.jpa.service;

import nl.amis.data.jpa.domain.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.Instant;
import java.util.List;

@Component("notificationService")
@Transactional
public class NotificationServiceImpl implements NotificationService {

	@Autowired
	private NotificationRepository notificationRepository;

	@Override
	public List<Notification> getNotifications(final String channelId, final Instant instant) {
		Assert.notNull(channelId, "Channel must not be null");
		Assert.hasLength(channelId, "Channel must not be empty");
		return notificationRepository.findNewNotificationsOnChannel(channelId, instant);
	}

}
