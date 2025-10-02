package co.com.bancolombia.usecase;

import co.com.bancolombia.model.user.Bootcamp;
import co.com.bancolombia.model.user.User;
import co.com.bancolombia.model.user.gateway.BootcampGateway;
import co.com.bancolombia.model.user.gateway.PublisherGateway;
import co.com.bancolombia.model.user.gateway.UserGateway;
import co.com.bancolombia.usecase.command.EnrollUserInBootcampCommand;
import co.com.bancolombia.usecase.exception.BussinessException;
import co.com.bancolombia.usecase.response.EnrollmentResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class EnrollUserInBootcampUseCaseTest {

  private UserGateway userGateway;
  private BootcampGateway bootcampGateway;
  private PublisherGateway publisherGateway;
  private EnrollUserInBootcampUseCase useCase;

  @BeforeEach
  void setUp() {
    userGateway = Mockito.mock(UserGateway.class);
    bootcampGateway = Mockito.mock(BootcampGateway.class);
    publisherGateway = Mockito.mock(PublisherGateway.class);
    useCase = new EnrollUserInBootcampUseCase(userGateway, bootcampGateway, publisherGateway);
  }

  @Test
  void shouldEnrollUserSuccessfully() {
    Long userId = 1L;
    Long bootcampId = 10L;

    Bootcamp target = new Bootcamp(bootcampId, "Java", "Desc", LocalDate.now().plusDays(5), 8);
    when(bootcampGateway.findById(bootcampId)).thenReturn(Mono.just(target));

    User existingUser = new User(userId, "Alice", "alice@example.com");
    when(userGateway.findById(userId)).thenReturn(Mono.just(existingUser));

    when(userGateway.countUserBootcamps(userId)).thenReturn(Mono.just(0L));
    when(userGateway.existsUserBootcampRelation(userId, bootcampId)).thenReturn(Mono.just(false));
    when(userGateway.findUserBootcampIds(userId)).thenReturn(Flux.empty());

    User afterEnroll = new User(userId, "Alice", "alice@example.com");
    when(userGateway.enrollUserInBootcamp(userId, bootcampId)).thenReturn(Mono.just(afterEnroll));

    when(userGateway.findByBootcampId(bootcampId)).thenReturn(Flux.just(afterEnroll));
    doNothing().when(publisherGateway).publish(eq(bootcampId), anyInt());

    EnrollmentResponse response = useCase.execute(new EnrollUserInBootcampCommand(userId, bootcampId)).block();

    assertNotNull(response);
    assertEquals(userId, response.getUser().getId());
    assertEquals(bootcampId, response.getBootcamp().getId());
    verify(publisherGateway, times(1)).publish(eq(bootcampId), anyInt());
  }

  @Test
  void shouldFailWhenBootcampNotFound() {
    Long userId = 1L;
    Long bootcampId = 99L;

    when(bootcampGateway.findById(bootcampId)).thenReturn(Mono.empty());

    Mono<EnrollmentResponse> mono = useCase.execute(new EnrollUserInBootcampCommand(userId, bootcampId));
    BussinessException ex = assertThrows(BussinessException.class, () -> mono.block());
    assertTrue(ex.getMessage().contains("Bootcamp with id"));
  }

  @Test
  void shouldFailWhenUserNotFound() {
    Long userId = 1L;
    Long bootcampId = 10L;

    when(bootcampGateway.findById(bootcampId)).thenReturn(Mono.just(new Bootcamp(bootcampId, "Java", "Desc", LocalDate.now().plusDays(5), 8)));
    when(userGateway.findById(userId)).thenReturn(Mono.empty());

    Mono<EnrollmentResponse> mono = useCase.execute(new EnrollUserInBootcampCommand(userId, bootcampId));
    BussinessException ex = assertThrows(BussinessException.class, () -> mono.block());
    assertTrue(ex.getMessage().contains("User with id"));
  }

  @Test
  void shouldFailWhenUserReachedMaxBootcamps() {
    Long userId = 1L;
    Long bootcampId = 10L;

    when(bootcampGateway.findById(bootcampId)).thenReturn(Mono.just(new Bootcamp(bootcampId, "Java", "Desc", LocalDate.now().plusDays(7), 8)));
    when(userGateway.findById(userId)).thenReturn(Mono.just(new User(userId, "Alice", "alice@example.com")));
    when(userGateway.countUserBootcamps(userId)).thenReturn(Mono.just(5L));
    when(userGateway.existsUserBootcampRelation(userId, bootcampId)).thenReturn(Mono.just(false));
    when(userGateway.findUserBootcampIds(userId)).thenReturn(Flux.empty());
    when(userGateway.enrollUserInBootcamp(userId, bootcampId)).thenReturn(Mono.just(new User(userId, "Alice", "alice@example.com")));
    when(userGateway.findByBootcampId(bootcampId)).thenReturn(Flux.empty());
    doNothing().when(publisherGateway).publish(anyLong(), anyInt());

    Mono<EnrollmentResponse> mono = useCase.execute(new EnrollUserInBootcampCommand(userId, bootcampId));
    BussinessException ex = assertThrows(BussinessException.class, () -> mono.block());
    assertTrue(ex.getMessage().contains("maximum limit"));
  }

  @Test
  void shouldFailWhenUserAlreadyEnrolled() {
    Long userId = 1L;
    Long bootcampId = 10L;

    when(bootcampGateway.findById(bootcampId)).thenReturn(Mono.just(new Bootcamp(bootcampId, "Java", "Desc", LocalDate.now().plusDays(9), 8)));
    when(userGateway.findById(userId)).thenReturn(Mono.just(new User(userId, "Alice", "alice@example.com")));
    when(userGateway.countUserBootcamps(userId)).thenReturn(Mono.just(0L));
    when(userGateway.existsUserBootcampRelation(userId, bootcampId)).thenReturn(Mono.just(true));
    when(userGateway.findUserBootcampIds(userId)).thenReturn(Flux.empty());
    when(userGateway.enrollUserInBootcamp(userId, bootcampId)).thenReturn(Mono.just(new User(userId, "Alice", "alice@example.com")));
    when(userGateway.findByBootcampId(bootcampId)).thenReturn(Flux.empty());
    doNothing().when(publisherGateway).publish(anyLong(), anyInt());

    Mono<EnrollmentResponse> mono = useCase.execute(new EnrollUserInBootcampCommand(userId, bootcampId));
    BussinessException ex = assertThrows(BussinessException.class, () -> mono.block());
    assertTrue(ex.getMessage().contains("already enrolled"));
  }

  @Test
  void shouldFailWhenScheduleConflict() {
    Long userId = 1L;
    Long bootcampId = 10L;

    Bootcamp newBootcamp = new Bootcamp(bootcampId, "Java", "Desc", LocalDate.now().plusDays(14), 8);
    when(bootcampGateway.findById(bootcampId)).thenReturn(Mono.just(newBootcamp));
    when(userGateway.findById(userId)).thenReturn(Mono.just(new User(userId, "Alice", "alice@example.com")));
    when(userGateway.countUserBootcamps(userId)).thenReturn(Mono.just(0L));
    when(userGateway.existsUserBootcampRelation(userId, bootcampId)).thenReturn(Mono.just(false));

    // Existing bootcamp that overlaps with newBootcamp
    Long existingBootcampId = 20L;
    when(userGateway.findUserBootcampIds(userId)).thenReturn(Flux.just(existingBootcampId));
    when(bootcampGateway.findById(existingBootcampId)).thenReturn(Mono.just(new Bootcamp(existingBootcampId, "React", "Desc", LocalDate.now().plusDays(10), 4)));
    when(userGateway.enrollUserInBootcamp(userId, bootcampId)).thenReturn(Mono.just(new User(userId, "Alice", "alice@example.com")));
    when(userGateway.findByBootcampId(bootcampId)).thenReturn(Flux.empty());
    doNothing().when(publisherGateway).publish(anyLong(), anyInt());

    Mono<EnrollmentResponse> mono = useCase.execute(new EnrollUserInBootcampCommand(userId, bootcampId));
    BussinessException ex = assertThrows(BussinessException.class, () -> mono.block());
    assertTrue(ex.getMessage().contains("conflicts with another bootcamp schedule"));
  }
}
