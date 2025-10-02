package co.com.bancolombia.events;

import co.com.bancolombia.model.user.Bootcamp;
import co.com.bancolombia.model.user.gateway.PublisherGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RabbitMQPublisherAdapter implements PublisherGateway {
  private final String MESSAGE_SENDED_MESSAGE = "Bootcamp has been published successfully";
  @Value("${adapter.rabbit.exchange}")
  private String exchange;
  @Value("${adapter.rabbit.routingkey}")
  private String routingKey;

  private final RabbitTemplate rabbitTemplate;

  @Override
  public void publish(Long bootcampId, Integer peopleEnrolledCount) {
    BootcampMessage message = new BootcampMessage(
      bootcampId,
      peopleEnrolledCount
    );
    rabbitTemplate.convertAndSend(exchange, routingKey, message);
    log.info(MESSAGE_SENDED_MESSAGE);
  }
}
