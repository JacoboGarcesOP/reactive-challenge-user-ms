package co.com.bancolombia.r2dbc;

import co.com.bancolombia.model.user.User;
import co.com.bancolombia.model.user.gateway.UserGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserGateway {

  private final UserRepository userRepository;
  private final UserBootcampRepository userBootcampRepository;

  @Override
  public Mono<User> save(User user) {
    UserEntity entity = new UserEntity(
        null,
        user.getName().getValue(),
        user.getEmail().getValue());
    return userRepository.save(entity)
        .map(saved -> new User(
            saved.getUserId(),
            saved.getName(),
            saved.getEmail()
        ));
  }

  @Override
  public Mono<User> findById(Long id) {
    return userRepository.findById(id)
        .map(found -> new User(
            found.getUserId(),
            found.getName(),
            found.getEmail()
        ));
  }

  @Override
  public Flux<User> findByBootcampId(Long bootcampId) {
    return userBootcampRepository.findByBootcampId(bootcampId)
        .flatMap(link -> userRepository.findById(link.getUserId()))
        .map(entity -> new User(
            entity.getUserId(),
            entity.getName(),
            entity.getEmail()
        ));
  }

  @Override
  public Mono<User> enrollUserInBootcamp(Long userId, Long bootcampId) {
    UserBootcampEntity userBootcampEntity = new UserBootcampEntity(null, userId, bootcampId);
    
    return userBootcampRepository.save(userBootcampEntity)
        .then(userRepository.findById(userId))
        .map(userEntity -> new User(
          userEntity.getUserId(),
          userEntity.getName(),
          userEntity.getEmail()
        ));
  }

  @Override
  public Mono<Boolean> existsUserBootcampRelation(Long userId, Long bootcampId) {
    return userBootcampRepository.findByUserId(userId)
        .any(link -> link.getBootcampId().equals(bootcampId));
  }

  @Override
  public Mono<Long> countUserBootcamps(Long userId) {
    return userBootcampRepository.findByUserId(userId)
        .count();
  }

  @Override
  public Flux<Long> findUserBootcampIds(Long userId) {
    return userBootcampRepository.findByUserId(userId)
        .map(link -> link.getBootcampId());
  }
}
