package nl.amis.web;

import nl.amis.data.jpa.domain.Notification;
import nl.amis.data.jpa.service.NotificationRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class NotificationController {

	private final NotificationRepository notificationRepository;

	public NotificationController(NotificationRepository notificationRepository) {
		this.notificationRepository = notificationRepository;
	}

	@GetMapping("/allnotifications")
	Iterable<Notification> all() {
		return notificationRepository.findAll();
	}

}
