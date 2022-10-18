package ch.ts.flashcardsservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class FlashCardsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlashCardsServiceApplication.class, args);
	}

}
