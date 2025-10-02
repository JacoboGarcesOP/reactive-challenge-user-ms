package co.com.bancolombia.usecase.response;

import java.time.LocalDate;

public class BootcampResponse {
  private final Long id;
  private final String name;
  private final String description;
  private final LocalDate launchDate;
  private final Integer duration;

  public BootcampResponse(Long id, String name, String description, LocalDate launchDate, Integer duration) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.launchDate = launchDate;
    this.duration = duration;
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
}
