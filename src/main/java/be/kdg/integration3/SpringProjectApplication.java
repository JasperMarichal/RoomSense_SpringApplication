package be.kdg.integration3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SpringProjectApplication {
	public final static boolean development = false;

	public static void main(String[] args) {
		var context = SpringApplication.run(SpringProjectApplication.class, args);
//		RawDataRepository repo = context.getBean(SerialRawDataRepository.class);
//		while(true){
//			int newData = repo.readSerial();
//		}

//		JSONReaderRepository reader = context.getBean(JSONReaderRepository.class);
//		reader.read();
//		System.out.println(reader.getRecordList());
	}

}
