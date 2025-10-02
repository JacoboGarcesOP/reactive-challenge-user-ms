package co.com.bancolombia.usecase.response;

import java.time.LocalDate;
import java.util.List;

public class BootcampResponse {
  private final Long id;
  private final String name;
  private final String description;
  private final LocalDate launchDate;
  private final Integer duration;
  private final List<CapacityResponse> capacities;

  public BootcampResponse(Long id, String name, String description, LocalDate launchDate, Integer duration, List<CapacityResponse> capacities) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.launchDate = launchDate;
    this.duration = duration;
    this.capacities = capacities;
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

  public LocalDate getLaunchDate() {
    return launchDate;
  }

  public Integer getDuration() {
    return duration;
  }

  public List<CapacityResponse> getCapacities() {
    return capacities;
  }
}
