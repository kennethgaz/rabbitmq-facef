package br.com.facef.rabbitmqdlq.consumer;

import br.com.facef.rabbitmqdlq.configuration.DirectExchangeConfiguration;
import br.com.facef.rabbitmqdlq.producer.MessageProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.ImmediateAcknowledgeAmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;

@Configuration
@Slf4j
public class MessageConsumer {
  @Autowired
  private MessageProducer messageProducer;

  @RabbitListener(queues = DirectExchangeConfiguration.ORDER_QUEUE_NAME)
  public void processOrderMessageFromOriginalQueue(Message message) {
    log.info("Processing message from original queue: {}", message.toString());

    String messageBody = new String(message.getBody(), StandardCharsets.UTF_8);

    int orderNumber = Integer.parseInt(messageBody.split(" ")[2]);

    if (orderNumber <= 3) {
      log.info("Message {} processed with success", messageBody); // success - remove message from queue
    } else if (orderNumber <= 5) {
      log.info("Sending message {} to dlq", messageBody);
      throw new AmqpRejectAndDontRequeueException("Unexpected Error"); // error - send message to dlq
    } else {
      log.info("Removing message {} from queue", messageBody);
      throw new ImmediateAcknowledgeAmqpException("Unable to process message"); // error - remove message from queue
    }
  }

  @RabbitListener(queues = DirectExchangeConfiguration.ORDER_QUEUE_DLQ_NAME)
  public void processOrderMessageFromDlqQueue(Message message) {
    log.info("Processing message from dlq queue: {}", message.toString());

    String messageBody = new String(message.getBody(), StandardCharsets.UTF_8);

    int orderNumber = Integer.parseInt(messageBody.split(" ")[2]);

    if (orderNumber == 5) {
      long count = message.getMessageProperties().getDeliveryTag();

      if (count <= 3) {
        throw new RuntimeException("Unexpected Error"); // error - send message to dlq again
      } else {
        this.messageProducer.sendMessageToParkingLotQueue(message);

        log.info("Removing message {} from queue", messageBody);
        throw new ImmediateAcknowledgeAmqpException("Unable to process message"); // error - remove message from queue
      }
    } else {
      log.info("Message {} processed with success", messageBody); // success - remove message from queue
    }
  }
}
