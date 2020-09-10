package br.com.facef.rabbitmqdlq.producer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static br.com.facef.rabbitmqdlq.configuration.DirectExchangeConfiguration.*;

@Service
@Slf4j
public class MessageProducer {
  @Autowired
  private RabbitTemplate rabbitTemplate;

  public void sendMessageToOriginalQueue(String message) {
    log.info("Sending a order message to original queue: " + message);

    this.rabbitTemplate.convertAndSend(
        DIRECT_EXCHANGE_NAME,
        ORDER_MESSAGES_QUEUE_NAME,
        message
    );
  }

  public void sendMessageToParkingLot(String message) {
    log.info("Sending a order message to parking lot queue: " + message);

    this.rabbitTemplate.convertAndSend(
        DIRECT_EXCHANGE_NAME,
        ORDER_MESSAGES_QUEUE_PARKING_LOT_NAME,
        message
    );
  }
}
