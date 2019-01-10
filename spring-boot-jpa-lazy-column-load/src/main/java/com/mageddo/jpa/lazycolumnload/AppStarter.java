package com.mageddo.jpa.lazycolumnload;

import com.mageddo.jpa.lazycolumnload.service.PersonService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

@Configuration
@SpringBootApplication
public class AppStarter {
	public static void main(String[] args) {
		SpringApplication.run(AppStarter.class, args)
			.getBean(PersonService.class)
			.createAndFind();
	}
}
