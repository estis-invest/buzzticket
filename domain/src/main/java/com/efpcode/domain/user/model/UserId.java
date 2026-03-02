package com.efpcode.domain.user.model;

import com.efpcode.domain.user.exceptions.InvalidUserIdException;
import java.util.UUID;

public record UserId(UUID id) {

  public UserId {
    if (id == null) {
      throw new InvalidUserIdException("UserId cannot pass null");
    }
  }

  public static UserId createRandom() {

    return new UserId(UUID.randomUUID());
  }

  public static UserId fromString(String uuid) {
    return new UserId(UUID.fromString(uuid));
  }
}
