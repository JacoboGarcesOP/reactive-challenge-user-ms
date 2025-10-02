package co.com.bancolombia.usecase;

import co.com.bancolombia.model.bootcamp.Capacity;
import co.com.bancolombia.model.bootcamp.Technology;
import co.com.bancolombia.model.user.Bootcamp;
import co.com.bancolombia.model.user.User;
import co.com.bancolombia.model.user.gateway.BootcampGateway;
import co.com.bancolombia.model.user.gateway.UserGateway;
import co.com.bancolombia.usecase.FindBootcampWithMostUsersUseCase.AggregatedBootcampResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

class FindBootcampWithMostUsersUseCaseTest {

  private UserGateway userGateway;
  private BootcampGateway bootcampGateway;
  private FindBootcampWithMostUsersUseCase useCase;

  @BeforeEach
  void setUp() {
    userGateway = Mockito.mock(UserGateway.class);
    bootcampGateway = Mockito.mock(BootcampGateway.class);
    useCase = new FindBootcampWithMostUsersUseCase(userGateway, bootcampGateway);
  }

  @Test
  void shouldAggregateBootcampUsersCapacitiesAndTechnologies() {
    when(userGateway.findBootcampIdWithMostUsers()).thenReturn(Mono.just(5L));

    Bootcamp bootcamp = new Bootcamp(5L, "Java Bootcamp", "Desc", LocalDate.now().plusDays(4), 12);
    Capacity capacity = new Capacity(1L, "Backend", "Servicios", List.of(new Technology(10L, "Spring", "Spring FW")));
    bootcamp.setCapacities(List.of(capacity));

    when(bootcampGateway.findById(5L)).thenReturn(Mono.just(bootcamp));

    User u1 = new User(1L, "Alice", "alice@example.com");
    User u2 = new User(2L, "Bob", "bob@example.com");
    when(userGateway.findByBootcampId(anyLong())).thenReturn(Flux.just(u1, u2));

    AggregatedBootcampResponse response = useCase.execute().block();

    assertNotNull(response);
    assertNotNull(response.getBootcamp());
    assertEquals(5L, response.getBootcamp().getId());
    assertEquals("Java Bootcamp", response.getBootcamp().getName());
    assertNotNull(response.getBootcamp().getCapacities());
    assertEquals(1, response.getBootcamp().getCapacities().size());
    assertEquals("Backend", response.getBootcamp().getCapacities().get(0).getName());
    assertEquals(1, response.getBootcamp().getCapacities().get(0).getTechnologies().size());
    assertEquals("Spring", response.getBootcamp().getCapacities().get(0).getTechnologies().get(0).getName());

    assertEquals(2, response.getUsers().size());
    assertEquals("Alice", response.getUsers().get(0).getName());
    assertEquals("Bob", response.getUsers().get(1).getName());
  }
}


