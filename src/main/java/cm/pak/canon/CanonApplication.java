package cm.pak.canon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CanonApplication {

	public static void main(String[] args) {
		SpringApplication.run(CanonApplication.class, args);
	}

}
