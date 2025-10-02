package co.com.bancolombia.usecase;

import co.com.bancolombia.model.user.Bootcamp;
import co.com.bancolombia.model.user.User;
import co.com.bancolombia.model.user.gateway.BootcampGateway;
import co.com.bancolombia.model.user.gateway.UserGateway;
import co.com.bancolombia.usecase.response.BootcampResponse;
import co.com.bancolombia.usecase.response.CapacityResponse;
import co.com.bancolombia.usecase.response.TechnologyResponse;
import co.com.bancolombia.usecase.response.UserResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public class FindBootcampWithMostUsersUseCase {

  private final UserGateway userGateway;
  private final BootcampGateway bootcampGateway;

  public FindBootcampWithMostUsersUseCase(UserGateway userGateway, BootcampGateway bootcampGateway) {
    this.userGateway = userGateway;
    this.bootcampGateway = bootcampGateway;
  }

  public Mono<AggregatedBootcampResponse> execute() {
    return userGateway.findBootcampIdWithMostUsers()
        .flatMap(bootcampId -> Mono.zip(
            bootcampGateway.findById(bootcampId),
            userGateway.findByBootcampId(bootcampId).collectList()
        ))
        .map(tuple -> {
          Bootcamp bootcamp = tuple.getT1();
          List<User> users = tuple.getT2();
          return new AggregatedBootcampResponse(
              mapBootcamp(bootcamp),
              users.stream().map(this::mapUser).toList()
          );
        });
  }

  private BootcampResponse mapBootcamp(Bootcamp bootcamp) {
    List<CapacityResponse> capacityResponses = bootcamp.getCapacities() == null ? List.of() :
        bootcamp.getCapacities().stream().map(cap -> new CapacityResponse(
            cap.getId() != null ? cap.getId().getValue() : null,
            cap.getName().getValue(),
            cap.getDescription().getValue(),
            cap.getTechnologies() == null ? List.of() : cap.getTechnologies().stream().map(tech -> new TechnologyResponse(
                tech.getId() != null ? tech.getId().getValue() : null,
                tech.getName().getValue(),
                tech.getDescription().getValue()
            )).toList()
        )).toList();

    return new BootcampResponse(
        bootcamp.getId().getValue(),
        bootcamp.getName().getValue(),
        bootcamp.getDescription().getValue(),
        bootcamp.getLaunchDate().getValue(),
        bootcamp.getDuration().getValue(),
        capacityResponses
    );
  }

  private UserResponse mapUser(User user) {
    return new UserResponse(
        user.getId().getValue(),
        user.getName().getValue(),
        user.getEmail().getValue()
    );
  }

  public static class AggregatedBootcampResponse {
    private final BootcampResponse bootcamp;
    private final List<UserResponse> users;

    public AggregatedBootcampResponse(BootcampResponse bootcamp, List<UserResponse> users) {
      this.bootcamp = bootcamp;
      this.users = users;
    }

    public BootcampResponse getBootcamp() {
      return bootcamp;
    }

    public List<UserResponse> getUsers() {
      return users;
    }
  }
}


