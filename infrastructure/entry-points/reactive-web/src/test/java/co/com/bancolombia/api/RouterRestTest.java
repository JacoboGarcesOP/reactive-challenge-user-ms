package co.com.bancolombia.api;

import co.com.bancolombia.api.request.EnrollUserInBootcampRequest;
import co.com.bancolombia.usecase.CreateUserUseCase;
import co.com.bancolombia.usecase.EnrollUserInBootcampUseCase;
import jakarta.validation.Validator;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;

@WebFluxTest
@ContextConfiguration(classes = {RouterRestTest.TestConfig.class})
class RouterRestTest {

  @MockBean
  private CreateUserUseCase createUserUseCase;
  
  @MockBean
  private EnrollUserInBootcampUseCase enrollUserInBootcampUseCase;

  @Autowired
  private WebTestClient webTestClient;

  @BeforeEach
  void setUp() {
    // Mock the use case to return a successful response for valid requests
    co.com.bancolombia.usecase.response.UserResponse userResponse = 
      new co.com.bancolombia.usecase.response.UserResponse(1L, "Test User", "test@example.com");
    co.com.bancolombia.usecase.response.BootcampResponse bootcampResponse = 
      new co.com.bancolombia.usecase.response.BootcampResponse(5L, "Test Bootcamp", "Test Description", 
        java.time.LocalDate.now().plusDays(1), 10);
    
    when(enrollUserInBootcampUseCase.execute(any())).thenReturn(
      Mono.just(new co.com.bancolombia.usecase.response.EnrollmentResponse(userResponse, bootcampResponse))
    );
  }

  @Test
  void shouldReturnBadRequestWhenUserIdIsNull() throws Exception {
    // Given
    EnrollUserInBootcampRequest request = new EnrollUserInBootcampRequest(null, 5L);

    // When & Then
    webTestClient.post()
        .uri("/v1/api/users/enroll")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(request)
        .exchange()
        .expectStatus().isBadRequest();
  }

  @Test
  void shouldReturnBadRequestWhenBootcampIdIsNull() throws Exception {
    // Given
    EnrollUserInBootcampRequest request = new EnrollUserInBootcampRequest(1L, null);

    // When & Then
    webTestClient.post()
        .uri("/v1/api/users/enroll")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(request)
        .exchange()
        .expectStatus().isBadRequest();
  }

  @Test
  void shouldReturnBadRequestWhenUserIdIsNegative() throws Exception {
    // Given
    EnrollUserInBootcampRequest request = new EnrollUserInBootcampRequest(-1L, 5L);

    // When & Then
    webTestClient.post()
        .uri("/v1/api/users/enroll")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(request)
        .exchange()
        .expectStatus().isBadRequest();
  }

  @Test
  void shouldReturnBadRequestWhenBootcampIdIsNegative() throws Exception {
    // Given
    EnrollUserInBootcampRequest request = new EnrollUserInBootcampRequest(1L, -5L);

    // When & Then
    webTestClient.post()
        .uri("/v1/api/users/enroll")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(request)
        .exchange()
        .expectStatus().isBadRequest();
  }

  @Test
  void shouldReturnBadRequestWhenUserIdIsZero() throws Exception {
    // Given
    EnrollUserInBootcampRequest request = new EnrollUserInBootcampRequest(0L, 5L);

    // When & Then
    webTestClient.post()
        .uri("/v1/api/users/enroll")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(request)
        .exchange()
        .expectStatus().isBadRequest();
  }

  @Test
  void shouldReturnBadRequestWhenBootcampIdIsZero() throws Exception {
    // Given
    EnrollUserInBootcampRequest request = new EnrollUserInBootcampRequest(1L, 0L);

    // When & Then
    webTestClient.post()
        .uri("/v1/api/users/enroll")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(request)
        .exchange()
        .expectStatus().isBadRequest();
  }

  @Test
  void shouldReturnBadRequestWhenRequestBodyIsInvalid() throws Exception {
    // Given
    String invalidJson = "{ \"userId\": \"invalid\", \"bootcampId\": 5 }";

    // When & Then
    webTestClient.post()
        .uri("/v1/api/users/enroll")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(invalidJson)
        .exchange()
        .expectStatus().isBadRequest();
  }

  @Test
  void shouldReturnBadRequestWhenRequestBodyIsEmpty() throws Exception {
    // When & Then
    webTestClient.post()
        .uri("/v1/api/users/enroll")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue("")
        .exchange()
        .expectStatus().isBadRequest();
  }

  @Test
  void shouldReturnBadRequestWhenContentTypeIsNotJson() throws Exception {
    // Given
    EnrollUserInBootcampRequest request = new EnrollUserInBootcampRequest(1L, 5L);

    // When & Then
    webTestClient.post()
        .uri("/v1/api/users/enroll")
        .contentType(MediaType.TEXT_PLAIN)
        .bodyValue("invalid content")
        .exchange()
        .expectStatus().isBadRequest();
  }

  @Test
  void shouldReturnOkWhenRequestIsValid() throws Exception {
    // Given
    EnrollUserInBootcampRequest request = new EnrollUserInBootcampRequest(1L, 5L);

    // When & Then
    webTestClient.post()
        .uri("/v1/api/users/enroll")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(request)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$.user.id").isEqualTo(1)
        .jsonPath("$.user.name").isEqualTo("Test User")
        .jsonPath("$.user.email").isEqualTo("test@example.com")
        .jsonPath("$.bootcamp.id").isEqualTo(5)
        .jsonPath("$.bootcamp.name").isEqualTo("Test Bootcamp");
  }

  @Configuration
  static class TestConfig {
    @Bean
    public RouterRest routerRest() {
      return new RouterRest();
    }
    
    @Bean
    public Handler handler(CreateUserUseCase createUserUseCase, 
                          EnrollUserInBootcampUseCase enrollUserInBootcampUseCase, 
                          Validator validator) {
      return new Handler(createUserUseCase, enrollUserInBootcampUseCase, validator);
    }
    
    @Bean
    public org.springframework.web.reactive.function.server.RouterFunction<org.springframework.web.reactive.function.server.ServerResponse> enrollUserInBootcampRouter(Handler handler) {
      RouterRest routerRest = new RouterRest();
      return routerRest.enrollUserInBootcampRouter(handler);
    }
    
    @Bean
    public jakarta.validation.Validator validator() {
      return jakarta.validation.Validation.buildDefaultValidatorFactory().getValidator();
    }
  }
}