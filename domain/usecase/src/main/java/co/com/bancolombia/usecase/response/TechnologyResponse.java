package co.com.bancolombia.usecase.response;

public class TechnologyResponse {
  private final Long id;
  private final String name;
  private final String description;

  public TechnologyResponse(Long id, String name, String description) {
    this.id = id;
    this.name = name;
    this.description = description;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }
}


