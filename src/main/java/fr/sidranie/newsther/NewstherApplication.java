package fr.sidranie.newsther;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class NewstherApplication {

	public static void main(String[] args) {
		SpringApplication.run(NewstherApplication.class, args);
	}

}
