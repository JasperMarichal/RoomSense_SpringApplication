package be.kdg.integration3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SpringProjectApplication {
	public final static boolean development = true;

	public static void main(String[] args) {
		var context = SpringApplication.run(SpringProjectApplication.class, args);
	}

}
