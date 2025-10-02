package co.com.bancolombia.api;

import co.com.bancolombia.api.request.CreateUserRequest;
import co.com.bancolombia.api.request.EnrollUserInBootcampRequest;
import co.com.bancolombia.api.response.ErrorResponse;
import co.com.bancolombia.model.user.exception.DomainException;
import co.com.bancolombia.usecase.CreateUserUseCase;
import co.com.bancolombia.usecase.EnrollUserInBootcampUseCase;
import co.com.bancolombia.usecase.command.CreateUserCommand;
import co.com.bancolombia.usecase.FindBootcampWithMostUsersUseCase;
import co.com.bancolombia.usecase.command.EnrollUserInBootcampCommand;
import co.com.bancolombia.usecase.exception.BussinessException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class Handler {
  private static final String VALIDATION_ERROR_TEXT = "VALIDATION_ERROR";
  private static final String DOMAIN_ERROR_TEXT = "DOMAIN_ERROR";
  private static final String BUSINESS_ERROR_TEXT = "BUSINESS_ERROR";
  private static final String INTERNAL_ERROR_TEXT = "INTERNAL_ERROR";
  private static final String GENERIC_ERROR_MESSAGE = "An unexpected error occurred";

  private final CreateUserUseCase createUserUseCase;
  private final EnrollUserInBootcampUseCase enrollUserInBootcampUseCase;
  private final Validator validator;
  private final FindBootcampWithMostUsersUseCase findBootcampWithMostUsersUseCase;

  public Mono<ServerResponse> createUser(ServerRequest serverRequest) {
    return serverRequest.bodyToMono(CreateUserRequest.class)
      .doOnNext(this::validateRequest)
      .map(this::mapToCommand)
      .flatMap(createUserUseCase::execute)
      .flatMap(this::buildSuccessResponse)
      .onErrorResume(ConstraintViolationException.class, this::handleValidationException)
      .onErrorResume(DomainException.class, this::handleDomainException)
      .onErrorResume(BussinessException.class, this::handleBusinessException)
      .onErrorResume(org.springframework.web.server.ServerWebInputException.class, this::handleServerWebInputException)
      .onErrorResume(org.springframework.web.server.UnsupportedMediaTypeStatusException.class, this::handleUnsupportedMediaTypeException)
      .onErrorResume(Exception.class, this::handleGenericException)
      .doOnError(error -> log.error(GENERIC_ERROR_MESSAGE, error));
  }

  public Mono<ServerResponse> enrollUserInBootcamp(ServerRequest serverRequest) {
    return serverRequest.bodyToMono(EnrollUserInBootcampRequest.class)
      .doOnNext(this::validateEnrollRequest)
      .map(this::mapToEnrollCommand)
      .flatMap(enrollUserInBootcampUseCase::execute)
      .flatMap(this::buildSuccessResponse)
      .onErrorResume(ConstraintViolationException.class, this::handleValidationException)
      .onErrorResume(DomainException.class, this::handleDomainException)
      .onErrorResume(BussinessException.class, this::handleBusinessException)
      .onErrorResume(org.springframework.web.server.ServerWebInputException.class, this::handleServerWebInputException)
      .onErrorResume(org.springframework.web.server.UnsupportedMediaTypeStatusException.class, this::handleUnsupportedMediaTypeException)
      .onErrorResume(Exception.class, this::handleGenericException)
      .doOnError(error -> log.error(GENERIC_ERROR_MESSAGE, error));
  }

  public Mono<ServerResponse> findBootcampWithMostUsers(ServerRequest serverRequest) {
    return findBootcampWithMostUsersUseCase.execute()
      .flatMap(this::buildSuccessResponse)
      .onErrorResume(Exception.class, this::handleGenericException)
      .doOnError(error -> log.error(GENERIC_ERROR_MESSAGE, error));
  }

  private void validateRequest(CreateUserRequest request) {
    Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(request);
    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(violations);
    }
  }

  private void validateEnrollRequest(EnrollUserInBootcampRequest request) {
    Set<ConstraintViolation<EnrollUserInBootcampRequest>> violations = validator.validate(request);
    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(violations);
    }
  }

  private CreateUserCommand mapToCommand(CreateUserRequest request) {
    return new CreateUserCommand(request.getName(), request.getEmail());
  }

  private EnrollUserInBootcampCommand mapToEnrollCommand(EnrollUserInBootcampRequest request) {
    return new EnrollUserInBootcampCommand(request.getUserId(), request.getBootcampId());
  }

  private Mono<ServerResponse> buildSuccessResponse(Object response) {
    return ServerResponse.ok()
      .contentType(MediaType.APPLICATION_JSON)
      .bodyValue(response);
  }

  private Mono<ServerResponse> handleValidationException(ConstraintViolationException ex) {
    String errorMessage = ex.getConstraintViolations().stream()
      .map(ConstraintViolation::getMessage)
      .collect(Collectors.joining(", "));

    log.warn("Validation error: {}", errorMessage);

    return ServerResponse.badRequest()
      .contentType(MediaType.APPLICATION_JSON)
      .bodyValue(new ErrorResponse(VALIDATION_ERROR_TEXT, errorMessage));
  }

  private Mono<ServerResponse> handleDomainException(DomainException ex) {
    log.warn("Domain error: {}", ex.getMessage());

    return ServerResponse.badRequest()
      .contentType(MediaType.APPLICATION_JSON)
      .bodyValue(new ErrorResponse(DOMAIN_ERROR_TEXT, ex.getMessage()));
  }

  private Mono<ServerResponse> handleBusinessException(BussinessException ex) {
    log.warn("Business error: {}", ex.getMessage());

    return ServerResponse.badRequest()
      .contentType(MediaType.APPLICATION_JSON)
      .bodyValue(new ErrorResponse(BUSINESS_ERROR_TEXT, ex.getMessage()));
  }

  private Mono<ServerResponse> handleServerWebInputException(org.springframework.web.server.ServerWebInputException ex) {
    log.warn("Server web input error: {}", ex.getMessage());

    return ServerResponse.badRequest()
      .contentType(MediaType.APPLICATION_JSON)
      .bodyValue(new ErrorResponse(VALIDATION_ERROR_TEXT, "Invalid request format or content"));
  }

  private Mono<ServerResponse> handleUnsupportedMediaTypeException(org.springframework.web.server.UnsupportedMediaTypeStatusException ex) {
    log.warn("Unsupported media type error: {}", ex.getMessage());

    return ServerResponse.badRequest()
      .contentType(MediaType.APPLICATION_JSON)
      .bodyValue(new ErrorResponse(VALIDATION_ERROR_TEXT, "Content type not supported"));
  }

  private Mono<ServerResponse> handleGenericException(Exception ex) {
    log.error("Unexpected error", ex);

    return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
      .contentType(MediaType.APPLICATION_JSON)
      .bodyValue(new ErrorResponse(INTERNAL_ERROR_TEXT, GENERIC_ERROR_MESSAGE));
  }
}
