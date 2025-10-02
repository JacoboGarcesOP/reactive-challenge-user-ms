package co.com.bancolombia.model.user.values;

import co.com.bancolombia.model.user.exception.DomainException;
import java.util.regex.Pattern;

public class Email {

  private static final String NULL_EMAIL_ERROR_MESSAGE = "The email cannot be null.";
  private static final String MAX_LENGTH_ERROR_MESSAGE = "The email cannot be greater than 100.";
  private static final String INVALID_EMAIL_ERROR_MESSAGE = "The email format is invalid.";
  private static final int MAX_EMAIL_LENGTH = 100;

  private static final Pattern EMAIL_PATTERN =
      Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

  private final String value;

  public Email(final String value) {

    if (value == null || value.trim().isEmpty()) {
      throw new DomainException(NULL_EMAIL_ERROR_MESSAGE);
    }

    final String trimmedValue = value.trim();

    if (trimmedValue.length() > MAX_EMAIL_LENGTH) {
      throw new DomainException(MAX_LENGTH_ERROR_MESSAGE);
    }

    if (!EMAIL_PATTERN.matcher(trimmedValue).matches()) {
      throw new DomainException(INVALID_EMAIL_ERROR_MESSAGE);
    }

    this.value = trimmedValue;
  }

  public String getValue() {
    return value;
  }
}
