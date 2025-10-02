package co.com.bancolombia.model.user;

import co.com.bancolombia.model.user.values.Description;
import co.com.bancolombia.model.user.values.Duration;
import co.com.bancolombia.model.user.values.Id;
import co.com.bancolombia.model.user.values.LaunchDate;
import co.com.bancolombia.model.user.values.Name;

import java.time.LocalDate;

public class Bootcamp {
  private Id id;
  private Name name;
  private Description description;
  private LaunchDate launchDate;
  private Duration duration;

  public Bootcamp(Long id, String name, String description, LocalDate launchDate, Integer duration) {
    this.id = new Id(id);
    this.name = new Name(name);
    this.description = new Description(description);
    this.launchDate = new LaunchDate(launchDate);
    this.duration = new Duration(duration);
  }

  public Bootcamp(String name, String description, LocalDate launchDate, Integer duration) {
    this.name = new Name(name);
    this.description = new Description(description);
    this.launchDate = new LaunchDate(launchDate);
    this.duration = new Duration(duration);
  }

  public Id getId() {
    return id;
  }

  public void setId(Id id) {
    this.id = id;
  }

  public Name getName() {
    return name;
  }

  public void setName(Name name) {
    this.name = name;
  }

  public Description getDescription() {
    return description;
  }

  public void setDescription(Description description) {
    this.description = description;
  }

  public LaunchDate getLaunchDate() {
    return launchDate;
  }

  public void setLaunchDate(LaunchDate launchDate) {
    this.launchDate = launchDate;
  }

  public Duration getDuration() {
    return duration;
  }

  public void setDuration(Duration duration) {
    this.duration = duration;
  }
}
