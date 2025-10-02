package co.com.bancolombia.usecase;

import co.com.bancolombia.model.user.Bootcamp;
import co.com.bancolombia.model.user.User;
import co.com.bancolombia.model.user.gateway.BootcampGateway;
import co.com.bancolombia.model.user.gateway.PublisherGateway;
import co.com.bancolombia.model.user.gateway.UserGateway;
import co.com.bancolombia.usecase.command.EnrollUserInBootcampCommand;
import co.com.bancolombia.usecase.exception.BussinessException;
import co.com.bancolombia.usecase.response.BootcampResponse;
import co.com.bancolombia.usecase.response.EnrollmentResponse;
import co.com.bancolombia.usecase.response.UserResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.LocalDate;
import java.util.List;

public class EnrollUserInBootcampUseCase {

  private static final Integer MAX_BOOTCAMPS_PER_USER = 5;
  private static final String BOOTCAMP_NOT_FOUND_MESSAGE = "Bootcamp with id %d not found";
  private static final String USER_NOT_FOUND_MESSAGE = "User with id %d not found";
  private static final String MAX_BOOTCAMPS_LIMIT_MESSAGE = "User has reached the maximum limit of %d bootcamps";
  private static final String USER_ALREADY_ENROLLED_MESSAGE = "User is already enrolled in bootcamp with id %d";
  private static final String BOOTCAMP_SCHEDULE_CONFLICT_MESSAGE = "User cannot be enrolled in bootcamp with id %d because it conflicts with another bootcamp schedule";

  private final UserGateway userGateway;
  private final BootcampGateway bootcampGateway;
  private final PublisherGateway publisherGateway;

  public EnrollUserInBootcampUseCase(UserGateway userGateway, BootcampGateway bootcampGateway, PublisherGateway publisherGateway) {
    this.userGateway = userGateway;
    this.bootcampGateway = bootcampGateway;
    this.publisherGateway = publisherGateway;
  }

  public Mono<EnrollmentResponse> execute(EnrollUserInBootcampCommand command) {
    return validateBootcampExists(command.getBootcampId())
      .flatMap(bootcamp ->
        validateUserExists(command.getUserId())
          .flatMap(user ->
            validateUserBootcampLimit(command.getUserId())
              .then(validateUserNotAlreadyEnrolled(command.getUserId(), command.getBootcampId()))
              .then(validateNoScheduleConflict(command.getUserId(), bootcamp))
              .then(userGateway.enrollUserInBootcamp(command.getUserId(), command.getBootcampId()))
              .flatMap(updatedUser -> 
                getBootcampEnrollmentCount(command.getBootcampId())
                  .doOnNext(count -> publisherGateway.publish(command.getBootcampId(), count))
                  .then(Mono.just(new EnrollmentResponse(
                    mapToUserResponse(updatedUser),
                    mapToBootcampResponse(bootcamp)
                  )))
              )
          )
      );
  }

  private Mono<Bootcamp> validateBootcampExists(Long bootcampId) {
    return bootcampGateway.findById(bootcampId)
      .switchIfEmpty(Mono.error(new BussinessException(String.format(BOOTCAMP_NOT_FOUND_MESSAGE, bootcampId))));
  }

  private Mono<User> validateUserExists(Long userId) {
    return userGateway.findById(userId)
      .switchIfEmpty(Mono.error(new BussinessException(String.format(USER_NOT_FOUND_MESSAGE, userId))));
  }

  private Mono<Void> validateUserBootcampLimit(Long userId) {
    return userGateway.countUserBootcamps(userId)
      .flatMap(currentBootcampCount -> {
        if (currentBootcampCount >= MAX_BOOTCAMPS_PER_USER) {
          return Mono.error(new BussinessException(
            String.format(MAX_BOOTCAMPS_LIMIT_MESSAGE, MAX_BOOTCAMPS_PER_USER)));
        }
        return Mono.empty();
      });
  }

  private Mono<Void> validateUserNotAlreadyEnrolled(Long userId, Long bootcampId) {
    return userGateway.existsUserBootcampRelation(userId, bootcampId)
      .flatMap(exists -> {
        if (exists) {
          return Mono.error(new BussinessException(
            String.format(USER_ALREADY_ENROLLED_MESSAGE, bootcampId)));
        }
        return Mono.empty();
      });
  }

  private Mono<Void> validateNoScheduleConflict(Long userId, Bootcamp newBootcamp) {
    return userGateway.findUserBootcampIds(userId)
      .flatMap(bootcampGateway::findById)
      .any(existingBootcamp -> hasScheduleConflict(newBootcamp, existingBootcamp))
      .flatMap(hasConflict -> hasConflict 
        ? Mono.error(new BussinessException(
            String.format(BOOTCAMP_SCHEDULE_CONFLICT_MESSAGE, newBootcamp.getId().getValue())))
        : Mono.empty());
  }

  private boolean hasScheduleConflict(Bootcamp newBootcamp, Bootcamp existingBootcamp) {
    LocalDate newStart = newBootcamp.getLaunchDate().getValue();
    LocalDate newEnd = newStart.plusWeeks(newBootcamp.getDuration().getValue());
    
    LocalDate existingStart = existingBootcamp.getLaunchDate().getValue();
    LocalDate existingEnd = existingStart.plusWeeks(existingBootcamp.getDuration().getValue());
    
    return newStart.isBefore(existingEnd) && newEnd.isAfter(existingStart);
  }

  private UserResponse mapToUserResponse(User user) {
    return new UserResponse(
      user.getId().getValue(),
      user.getName().getValue(),
      user.getEmail().getValue()
    );
  }

  private BootcampResponse mapToBootcampResponse(Bootcamp bootcamp) {
    return new BootcampResponse(
      bootcamp.getId().getValue(),
      bootcamp.getName().getValue(),
      bootcamp.getDescription().getValue(),
      bootcamp.getLaunchDate().getValue(),
      bootcamp.getDuration().getValue(),
      null
    );
  }

  private Mono<Integer> getBootcampEnrollmentCount(Long bootcampId) {
    return userGateway.findByBootcampId(bootcampId)
      .count()
      .map(Long::intValue);
  }
}
