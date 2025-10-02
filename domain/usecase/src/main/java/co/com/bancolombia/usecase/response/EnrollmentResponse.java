package co.com.bancolombia.usecase.response;

public class EnrollmentResponse {
  private final UserResponse user;
  private final BootcampResponse bootcamp;

  public EnrollmentResponse(UserResponse user, BootcampResponse bootcamp) {
    this.user = user;
    this.bootcamp = bootcamp;
  }

  public UserResponse getUser() {
    return user;
  }

  public BootcampResponse getBootcamp() {
    return bootcamp;
  }
}
