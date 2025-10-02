package co.com.bancolombia.model.user.gateway;

import co.com.bancolombia.model.user.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserGateway {
  Mono<User> save(User user);
  Mono<User> findById(Long id);
  Flux<User> findByBootcampId(Long bootcampId);
  Mono<User> enrollUserInBootcamp(Long userId, Long bootcampId);
  Mono<Boolean> existsUserBootcampRelation(Long userId, Long bootcampId);
  Mono<Long> countUserBootcamps(Long userId);
  Flux<Long> findUserBootcampIds(Long userId);
  Mono<Long> findBootcampIdWithMostUsers();
}
