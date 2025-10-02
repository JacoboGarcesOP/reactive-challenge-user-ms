package co.com.bancolombia.usecase.response;

import java.util.List;

public class CapacityResponse {
  private final Long id;
  private final String name;
  private final String description;
  private final List<TechnologyResponse> technologies;

  public CapacityResponse(Long id, String name, String description, List<TechnologyResponse> technologies) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.technologies = technologies;
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

  public List<TechnologyResponse> getTechnologies() {
    return technologies;
  }
}


