package br.com.facef.rabbitmqdlq.producer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

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
        ORDER_QUEUE_NAME,
        message
    );
  }

  /* public void sendMessageToDlqQueue(Message message) {
    log.info("Sending a order message to DLQ queue: " + new String(message.getBody(), StandardCharsets.UTF_8));

    this.rabbitTemplate.send(
        DIRECT_EXCHANGE_NAME,
        ORDER_QUEUE_DLQ_NAME,
        message
    );
  } */

  public void sendMessageToParkingLotQueue(Message message) {
    log.info("Sending a order message to parking lot queue: " + new String(message.getBody(), StandardCharsets.UTF_8));

    this.rabbitTemplate.send(
        DIRECT_EXCHANGE_NAME,
        ORDER_QUEUE_PARKING_LOT_NAME,
        message
    );
  }
}
