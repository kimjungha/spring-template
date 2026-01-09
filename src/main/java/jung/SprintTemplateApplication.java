package jung;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SprintTemplateApplication {

	public static void main(String[] args) {
		SpringApplication.run(SprintTemplateApplication.class, args);
	}

}
