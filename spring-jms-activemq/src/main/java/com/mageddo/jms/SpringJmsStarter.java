package com.mageddo.jms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.jms.annotation.EnableJms;

@EnableJms
@EnableAspectJAutoProxy
@SpringBootApplication
public class SpringJmsStarter {

	public static void main(String[] args) {
		SpringApplication.run(SpringJmsStarter.class, args);
	}

}
