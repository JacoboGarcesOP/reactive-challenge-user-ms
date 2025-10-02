package co.com.bancolombia.usecase.command;

public class EnrollUserInBootcampCommand {
  private final Long userId;
  private final Long bootcampId;

  public EnrollUserInBootcampCommand(Long userId, Long bootcampId) {
    this.userId = userId;
    this.bootcampId = bootcampId;
  }

  public Long getUserId() {
    return userId;
  }

  public Long getBootcampId() {
    return bootcampId;
  }
}
