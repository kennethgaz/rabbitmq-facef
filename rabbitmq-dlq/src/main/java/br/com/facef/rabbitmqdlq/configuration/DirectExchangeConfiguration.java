package br.com.facef.rabbitmqdlq.configuration;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DirectExchangeConfiguration {
  public static final String DIRECT_EXCHANGE_NAME = "order-exchange";
  public static final String ORDER_QUEUE_NAME = "order-queue";
  public static final String ORDER_QUEUE_DLQ_NAME = ORDER_QUEUE_NAME + ".dlq";
  public static final String ORDER_QUEUE_PARKING_LOT_NAME = ORDER_QUEUE_NAME + ".parking-lot";

  // Configuring exchanges

  @Bean
  DirectExchange exchange() {
    return ExchangeBuilder.directExchange(DIRECT_EXCHANGE_NAME).durable(true).build();
  }

  // Configuring queues

  @Bean
  Queue orderQueue() {
    return QueueBuilder.durable(ORDER_QUEUE_NAME)
        .withArgument("x-dead-letter-exchange", "")
        .withArgument("x-dead-letter-routing-key", ORDER_QUEUE_DLQ_NAME)
        .build();
  }

  @Bean
  Queue orderDeadLetterQueue() {
    return QueueBuilder.durable(ORDER_QUEUE_DLQ_NAME).build();
  }

  @Bean
  Queue orderParkingLotQueue() {
    return QueueBuilder.durable(ORDER_QUEUE_PARKING_LOT_NAME).build();
  }

  // Binding

  @Bean
  Binding bindingOrderQueue(@Qualifier("orderQueue") Queue queue, DirectExchange exchange) {
    return BindingBuilder.bind(queue).to(exchange).with(ORDER_QUEUE_NAME);
  }

  @Bean
  Binding bindingOrderDeadLetterQueue(@Qualifier("orderDeadLetterQueue") Queue queue, DirectExchange exchange) {
    return BindingBuilder.bind(queue).to(exchange).with(ORDER_QUEUE_DLQ_NAME);
  }

  @Bean
  Binding bindingOrderParkingLotQueue(@Qualifier("orderParkingLotQueue") Queue queue, DirectExchange exchange) {
    return BindingBuilder.bind(queue).to(exchange).with(ORDER_QUEUE_PARKING_LOT_NAME);
  }
}
