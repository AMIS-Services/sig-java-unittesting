import nl.amis.api.lib.RequestIdentifier;
import nl.amis.api.lib.Tags;
import nl.amis.api.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class AuthenticationApplication {

    @Autowired
    AuthenticationService authenticationService;

    public static void main(String[] args) throws Exception {
        SpringApplication.run(AuthenticationApplication.class, args);
    }

    public void run(String... args) throws Exception {
        RequestIdentifier requestIdentifier = RequestIdentifier.create();
        Tags tags = authenticationService.getTags(requestIdentifier);
    }
}
