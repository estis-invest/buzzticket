package com.efpcode.domain.user.model;

import java.util.UUID;

public record UserId(UUID id) {

  public UserId {
    if (id == null) {
      throw new IllegalArgumentException("UserId cannot pass null");
    }
  }

  public static UserId createRandom() {

    return new UserId(UUID.randomUUID());
  }

  public static UserId fromString(String uuid) {
    return new UserId(UUID.fromString(uuid));
  }
}
