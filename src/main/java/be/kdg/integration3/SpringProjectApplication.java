package be.kdg.integration3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;

@SpringBootApplication(scanBasePackages = {"be.kdg.integration3"})
@EnableScheduling
public class SpringProjectApplication {
	public static final boolean DEVELOPMENT = true;

	public static void main(String[] args) {
		var context = SpringApplication.run(SpringProjectApplication.class, args);
	}

	@Bean
	public ErrorViewResolver customErrorViewResolver() {
		return (request, status, model) -> {
			if (status == HttpStatus.NOT_FOUND || status == HttpStatus.INTERNAL_SERVER_ERROR) {
				return new ModelAndView("errorPage", Collections.emptyMap(), HttpStatus.OK);
			}
			return null;
		};
	}

}
