package co.com.bancolombia.r2dbc;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface UserBootcampRepository extends ReactiveCrudRepository<UserBootcampEntity, Long> {
  Flux<UserBootcampEntity> findByBootcampId(Long bootcampId);
  Flux<UserBootcampEntity> findByUserId(Long userId);
}
