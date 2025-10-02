package co.com.bancolombia.usecase;

import co.com.bancolombia.model.user.User;
import co.com.bancolombia.model.user.gateway.UserGateway;
import co.com.bancolombia.usecase.command.CreateUserCommand;
import co.com.bancolombia.usecase.response.UserResponse;
import java.util.List;
import reactor.core.publisher.Mono;

public class CreateUserUseCase {

  private final UserGateway userGateway;

  public CreateUserUseCase(UserGateway userGateway) {
    this.userGateway = userGateway;
  }

  public Mono<UserResponse> execute(CreateUserCommand command) {
    User user = new User(command.getName(), command.getEmail());
    return userGateway.save(user)
        .map(saved -> new UserResponse(
            saved.getId().getValue(),
            saved.getName().getValue(),
            saved.getEmail().getValue()
        ));
  }
}


