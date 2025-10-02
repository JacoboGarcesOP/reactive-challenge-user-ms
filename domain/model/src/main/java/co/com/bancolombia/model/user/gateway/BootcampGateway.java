package co.com.bancolombia.model.user.gateway;

import co.com.bancolombia.model.user.Bootcamp;
import reactor.core.publisher.Mono;

public interface BootcampGateway {
  Mono<Bootcamp> findById(Long id);
}
