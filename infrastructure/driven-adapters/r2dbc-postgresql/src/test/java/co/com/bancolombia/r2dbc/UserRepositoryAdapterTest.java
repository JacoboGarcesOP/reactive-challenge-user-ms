package co.com.bancolombia.r2dbc;

import co.com.bancolombia.model.user.User;
import co.com.bancolombia.model.user.gateway.UserGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

class UserRepositoryAdapterTest {

  private UserRepository userRepository;
  private UserBootcampRepository userBootcampRepository;
  private UserRepositoryAdapter adapter;

  @BeforeEach
  void setUp() {
    userRepository = Mockito.mock(UserRepository.class);
    userBootcampRepository = Mockito.mock(UserBootcampRepository.class);
    adapter = new UserRepositoryAdapter(userRepository, userBootcampRepository);
  }

  @Test
  void shouldFindBootcampIdWithMostUsers() {
    when(userBootcampRepository.findAll()).thenReturn(Flux.just(
        new UserBootcampEntity(1L, 1L, 10L),
        new UserBootcampEntity(2L, 2L, 10L),
        new UserBootcampEntity(3L, 3L, 20L)
    ));

    Long bootcampId = adapter.findBootcampIdWithMostUsers().block();
    assertEquals(10L, bootcampId);
  }
}


