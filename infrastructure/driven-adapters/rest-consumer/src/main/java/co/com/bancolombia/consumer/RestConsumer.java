package co.com.bancolombia.consumer;

import co.com.bancolombia.model.user.Bootcamp;
import co.com.bancolombia.model.user.gateway.BootcampGateway;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RestConsumer implements BootcampGateway {
    private final WebClient client;

  @CircuitBreaker(name = "findById")
  @Override
  public Mono<Bootcamp> findById(Long id) {
    return client
      .get()
      .uri("/" + id)
      .retrieve()
      .onStatus(HttpStatusCode::is4xxClientError, this::map4xx)
      .onStatus(HttpStatusCode::is5xxServerError, this::map5xx)
      .bodyToMono(ObjectResponse.class)
      .map(resp -> new Bootcamp(
        resp.getBootcampId(),
        resp.getName(),
        resp.getDescription(),
        resp.getLaunchDate(),
        resp.getDuration()));
  }

  private Mono<? extends Throwable> map4xx(ClientResponse response) {
    return response.bodyToMono(String.class)
      .flatMap(body -> Mono.error(new RuntimeException("Client error: " + response.statusCode() + " - " + body)));
  }

  private Mono<? extends Throwable> map5xx(ClientResponse response) {
    return response.bodyToMono(String.class)
      .flatMap(body -> Mono.error(new RuntimeException("Server error: " + response.statusCode() + " - " + body)));
  }
}
