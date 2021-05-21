package nl.amis.web;

import nl.amis.data.jpa.domain.Subscription;
import nl.amis.data.jpa.service.SubscriptionRepository;
import nl.amis.data.jpa.service.SubscriptionService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class SubscriptionController {

	private final SubscriptionRepository subscriptionRepository;

	public SubscriptionController(SubscriptionRepository subscriptionRepository) {
		this.subscriptionRepository = subscriptionRepository;
	}

	@RequestMapping("/")
	@ResponseBody
	public String toDo() {
		return "Not implemented yet";
	}

	@GetMapping("/subscriptions")
	Iterable<Subscription> all() {
		return subscriptionRepository.findAll();
	}

}
