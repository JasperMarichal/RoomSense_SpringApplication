package be.kdg.integration3;

import be.kdg.integration3.repository.RawDataRepository;
import be.kdg.integration3.repository.SerialRawDataRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringProjectApplication {

	public static void main(String[] args) {
		var context = SpringApplication.run(SpringProjectApplication.class, args);
		RawDataRepository repo = context.getBean(SerialRawDataRepository.class);
		while(true){
			int newData = repo.readSerial();
		}
	}

}
