package nl.amis.data.jpa.service;

import java.time.Instant;
import java.util.List;

import nl.amis.data.jpa.domain.Notification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface NotificationRepository extends CrudRepository<Notification, Long> {

	@Query("select n from Notification n where upper(n.channelId) = upper(?1) and n.createdAt > ?2 order by n.createdAt ASC")
	List<Notification> findNewNotificationsOnChannel(String channelId, Instant instant);

}
