package co.com.bancolombia.model.bootcamp.values;

import co.com.bancolombia.model.user.exception.DomainException;

public class Id {
  private static final String NULL_ID_ERROR_MESSAGE = "The id cannot be null.";
  private static final String MIN_ID_ERROR_MESSAGE = "The id must be greater than zero.";

  private final Long value;

  public Id(final Long value) {
    if (value == null) {
      throw new DomainException(NULL_ID_ERROR_MESSAGE);
    }
    if (value <= 0) {
      throw new DomainException(MIN_ID_ERROR_MESSAGE);
    }
    this.value = value;
  }

  public Long getValue() {
    return value;
  }
}


