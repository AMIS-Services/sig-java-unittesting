package nl.amis;

import nl.amis.processor.SubscriptionProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class NotifierApplication implements CommandLineRunner {

	public static final String API = "Api01";

	@Autowired
	SubscriptionProcessor subscriptionProcessor;

	public static void main(String[] args) throws Exception {
		SpringApplication.run(NotifierApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		subscriptionProcessor.run();
	}

}
