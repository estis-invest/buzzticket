package com.efpcode.domain.user.model;

import com.efpcode.domain.user.exceptions.IllegalUserIdArgumentException;
import com.efpcode.domain.user.exceptions.InvalidUserIdException;
import java.util.UUID;

public record UserId(UUID id) {

  public UserId {
    if (id == null) {
      throw new InvalidUserIdException("UserId cannot pass null");
    }
  }

  public static UserId of(UUID value) {
    return new UserId(value);
  }

  public static UserId fromString(String uuid) {
    if (uuid == null || uuid.trim().isBlank()) {
      throw new IllegalUserIdArgumentException("fromString method cannot pass null or blank");
    }

    try {
      return new UserId(UUID.fromString(uuid.trim()));

    } catch (IllegalArgumentException e) {
      throw new IllegalUserIdArgumentException("Invalid or malformatted uuid");
    }
  }
}
