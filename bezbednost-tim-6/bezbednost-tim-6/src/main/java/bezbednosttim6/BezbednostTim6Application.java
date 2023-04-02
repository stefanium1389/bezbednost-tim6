package bezbednosttim6;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaRepositories("bezbednosttim6")
@EntityScan("bezbednosttim6")
public class BezbednostTim6Application {

	public static void main(String[] args) {
		SpringApplication.run(BezbednostTim6Application.class, args);
	}

}
