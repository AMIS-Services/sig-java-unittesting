package nl.amis.web;

import nl.amis.data.jpa.domain.Notification;
import nl.amis.data.jpa.domain.Subscription;
import nl.amis.data.jpa.service.NotificationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class NotificationController {

	private final NotificationRepository notificationRepository;

	public NotificationController(NotificationRepository notificationRepository) {
		this.notificationRepository = notificationRepository;
	}

	@GetMapping("/notifications")
	Iterable<Notification> all() {
		return notificationRepository.findAll();
	}

}
