package co.com.bancolombia.model.user.values;

import static org.junit.jupiter.api.Assertions.*;

import co.com.bancolombia.model.user.exception.DomainException;
import org.junit.jupiter.api.Test;

class EmailTest {

  @Test
  void shouldCreateValidEmail() {
    Email email = new Email("john.doe@example.com");
    assertEquals("john.doe@example.com", email.getValue());
  }

  @Test
  void shouldFailWhenEmailIsNullOrEmpty() {
    assertThrows(DomainException.class, () -> new Email(null));
    assertThrows(DomainException.class, () -> new Email("   "));
  }

  @Test
  void shouldFailWhenEmailIsTooLong() {
    String localPart = "a".repeat(95);
    String tooLong = localPart + "@x.com"; // length > 100
    assertThrows(DomainException.class, () -> new Email(tooLong));
  }

  @Test
  void shouldFailWhenEmailHasInvalidFormat() {
    assertThrows(DomainException.class, () -> new Email("john.doe_at_example.com"));
    assertThrows(DomainException.class, () -> new Email("john@doe"));
  }
}


