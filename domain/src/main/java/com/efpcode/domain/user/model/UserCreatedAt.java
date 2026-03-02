package com.efpcode.domain.user.model;

import com.efpcode.domain.user.exceptions.InvalidUserCreatedAtException;
import com.efpcode.domain.user.exceptions.UserCreatedAtDateException;
import java.time.Instant;

public record UserCreatedAt(Instant time) {

  private static final int GRACE_PERIOD = 60;

  public UserCreatedAt {
    if (time == null || time.equals(Instant.ofEpochMilli(0)))
      throw new InvalidUserCreatedAtException("Time is required");

    if (time.isAfter(Instant.now().plusSeconds(GRACE_PERIOD)))
      throw new UserCreatedAtDateException("User cannot be created in the future");
  }

  public static UserCreatedAt createNow() {
    return new UserCreatedAt(Instant.now());
  }
}
