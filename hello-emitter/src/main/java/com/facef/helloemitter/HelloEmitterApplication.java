package com.facef.helloemitter;

import org.springframework.amqp.core.Queue;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class HelloEmitterApplication {
	static final String queueName = "hello-queue";
	
	@Bean
	Queue queue() {
		return new Queue(queueName, false);
	}

	public static void main(String[] args) {
		SpringApplication.run(HelloEmitterApplication.class, args);
	}
}
