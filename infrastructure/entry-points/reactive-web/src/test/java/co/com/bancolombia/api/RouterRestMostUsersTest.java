package co.com.bancolombia.api;

import co.com.bancolombia.usecase.FindBootcampWithMostUsersUseCase;
import co.com.bancolombia.usecase.FindBootcampWithMostUsersUseCase.AggregatedBootcampResponse;
import co.com.bancolombia.usecase.response.BootcampResponse;
import co.com.bancolombia.usecase.response.CapacityResponse;
import co.com.bancolombia.usecase.response.TechnologyResponse;
import co.com.bancolombia.usecase.response.UserResponse;
import jakarta.validation.Validator;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.when;

@WebFluxTest
@ContextConfiguration(classes = {RouterRestMostUsersTest.TestConfig.class})
class RouterRestMostUsersTest {

  @MockBean
  private FindBootcampWithMostUsersUseCase findBootcampWithMostUsersUseCase;

  @Autowired
  private WebTestClient webTestClient;

  @BeforeEach
  void setUp() {
    BootcampResponse bootcamp = new BootcampResponse(5L, "Java", "Desc", LocalDate.now(), 12,
        List.of(new CapacityResponse(1L, "Backend", "Servicios",
            List.of(new TechnologyResponse(10L, "Spring", "Spring FW")))));
    List<UserResponse> users = List.of(
        new UserResponse(1L, "Alice", "alice@example.com"),
        new UserResponse(2L, "Bob", "bob@example.com")
    );
    AggregatedBootcampResponse aggregated = new AggregatedBootcampResponse(bootcamp, users);
    when(findBootcampWithMostUsersUseCase.execute()).thenReturn(Mono.just(aggregated));
  }

  @Test
  void shouldReturnAggregatedBootcamp() {
    webTestClient.get()
      .uri("/v1/api/bootcamps/most-users")
      .accept(MediaType.APPLICATION_JSON)
      .exchange()
      .expectStatus().isOk()
      .expectHeader().contentType(MediaType.APPLICATION_JSON)
      .expectBody()
      .jsonPath("$.bootcamp.id").isEqualTo(5)
      .jsonPath("$.users.length()").isEqualTo(2);
  }

  @Configuration
  static class TestConfig {
    @Bean
    public RouterRest routerRest() {
      return new RouterRest();
    }

    @Bean
    public Handler handler(Validator validator, FindBootcampWithMostUsersUseCase findBootcampWithMostUsersUseCase) {
      // other use cases are not exercised here, can be mocked by Spring
      return new Handler(null, null, validator, findBootcampWithMostUsersUseCase);
    }

    @Bean
    public org.springframework.web.reactive.function.server.RouterFunction<org.springframework.web.reactive.function.server.ServerResponse> findBootcampWithMostUsersRouter(Handler handler) {
      RouterRest routerRest = new RouterRest();
      return routerRest.findBootcampWithMostUsersRouter(handler);
    }

    @Bean
    public jakarta.validation.Validator validator() {
      return jakarta.validation.Validation.buildDefaultValidatorFactory().getValidator();
    }
  }
}


