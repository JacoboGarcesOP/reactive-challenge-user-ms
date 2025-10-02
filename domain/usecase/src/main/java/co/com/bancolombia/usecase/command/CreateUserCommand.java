package co.com.bancolombia.usecase.command;

public class CreateUserCommand {
  private final String name;
  private final String email;

  public CreateUserCommand(String name, String email) {
    this.name = name;
    this.email = email;
  }

  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }
}


