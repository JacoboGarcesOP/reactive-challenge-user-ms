package co.com.bancolombia.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import co.com.bancolombia.model.user.User;
import co.com.bancolombia.model.user.gateway.UserGateway;
import co.com.bancolombia.usecase.command.CreateUserCommand;
import co.com.bancolombia.usecase.response.UserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;

class CreateUserUseCaseTest {

  private UserGateway userGateway;
  private CreateUserUseCase useCase;

  @BeforeEach
  void setUp() {
    userGateway = Mockito.mock(UserGateway.class);
    useCase = new CreateUserUseCase(userGateway);
  }

  @Test
  void shouldCreateUserSuccessfully() {
    when(userGateway.save(any(User.class))).thenAnswer(invocation -> {
      User u = invocation.getArgument(0);
      // simulate persistence assigning id
      return Mono.just(new User(1L, u.getName().getValue(), u.getEmail().getValue()));
    });

    CreateUserCommand command = new CreateUserCommand("Alice", "alice@example.com");

    UserResponse response = useCase.execute(command).block();

    assertNotNull(response);
    assertEquals(1L, response.getId());
    assertEquals("Alice", response.getName());
    assertEquals("alice@example.com", response.getEmail());
    // UserResponse doesn't have bootcamps in this version
  }
}


