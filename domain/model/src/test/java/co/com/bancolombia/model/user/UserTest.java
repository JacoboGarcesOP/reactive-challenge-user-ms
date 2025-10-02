package co.com.bancolombia.model.user;

import static org.junit.jupiter.api.Assertions.*;

import co.com.bancolombia.model.user.values.Email;
import co.com.bancolombia.model.user.values.Id;
import co.com.bancolombia.model.user.values.Name;
import java.util.List;
import org.junit.jupiter.api.Test;

class UserTest {

  @Test
  void shouldCreateUserWithoutId() {
    User user = new User("Alice", "alice@example.com");
    assertNull(user.getId());
    assertEquals("Alice", user.getName().getValue());
    assertEquals("alice@example.com", user.getEmail().getValue());
    assertNotNull(user.getBootcamps());
    assertTrue(user.getBootcamps().isEmpty());
  }

  @Test
  void shouldCreateUserWithId() {
    User user = new User(1L, "Bob", "bob@example.com");
    assertEquals(1L, user.getId().getValue());
  }

  @Test
  void shouldAllowSettingIdLater() {
    User user = new User("Charlie", "charlie@example.com");
    user.setId(new Id(2L));
    assertEquals(2L, user.getId().getValue());
  }

  @Test
  void shouldSetBootcampsList() {
    User user = new User("Dave", "dave@example.com");
    Bootcamp bootcamp = new Bootcamp(10L, "BC", "Bootcamp description", java.time.LocalDate.now().plusDays(1), 5);
    user.setBootcamps(List.of(bootcamp));
    assertEquals(1, user.getBootcamps().size());
    assertEquals(10L, user.getBootcamps().get(0).getId().getValue());
  }
}


