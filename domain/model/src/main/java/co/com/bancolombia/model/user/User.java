package co.com.bancolombia.model.user;

import co.com.bancolombia.model.user.values.Email;
import co.com.bancolombia.model.user.values.Id;
import co.com.bancolombia.model.user.values.Name;
import java.util.ArrayList;
import java.util.List;

public class User {
  private Id id;
  private Name name;
  private Email email;
  private List<Bootcamp> bootcamps;

  public User(Long id, String name, String email) {
    this.id = new Id(id);
    this.name = new Name(name);
    this.email = new Email(email);
    this.bootcamps = new ArrayList<>();
  }

  public User(String name, String email) {
    this.name = new Name(name);
    this.email = new Email(email);
    this.bootcamps = new ArrayList<>();
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

  public Email getEmail() {
    return email;
  }

  public void setEmail(Email email) {
    this.email = email;
  }

  public List<Bootcamp> getBootcamps() {
    return bootcamps;
  }

  public void setBootcamps(List<Bootcamp> bootcamps) {
    this.bootcamps = bootcamps;
  }

  public void addBootcamp(Bootcamp bootcamp) {
    if (this.bootcamps == null) {
      this.bootcamps = new ArrayList<>();
    }
    this.bootcamps.add(bootcamp);
  }
}


