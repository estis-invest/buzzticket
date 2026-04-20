package com.efpcode.domain.user.model;

import com.efpcode.domain.user.exceptions.InvalidUserUpdateAtException;
import com.efpcode.domain.user.exceptions.UserUpdateAtDateException;
import java.time.Instant;

public record UserUpdateAt(Instant updatedAt) {

  private static final int GRACE_PERIOD = 60;

  public UserUpdateAt {
    if (updatedAt == null || updatedAt.equals(Instant.ofEpochMilli(0))) {
      throw new InvalidUserUpdateAtException("Update timestamp is required");
    }

    if (updatedAt.isAfter(Instant.now().plusSeconds(GRACE_PERIOD))) {
      throw new UserUpdateAtDateException("Update timestamp cannot be in the future");
    }
  }

  public static UserUpdateAt createNow() {
    return new UserUpdateAt(Instant.now());
  }
}
