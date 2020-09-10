package com.facef.helloemitter;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Runner implements CommandLineRunner {
  private RabbitTemplate rabbitTemplate;

  public Runner(RabbitTemplate rabbitTemplate) {
    this.rabbitTemplate = rabbitTemplate;
  }

  @Override
  public void run(String... args) throws Exception {
    System.out.println("Enviando mensagem...");

    rabbitTemplate.convertAndSend(HelloEmitterApplication.queueName, "Hello from RabbitMQ!");

    System.out.println("Mensagem enviada");
  }
}
