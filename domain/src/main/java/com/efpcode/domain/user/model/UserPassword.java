package com.efpcode.domain.user.model;

import com.efpcode.domain.user.exceptions.InvalidUserPasswordException;

public record UserPassword(String hashedPassword) {

  public UserPassword {
    if (hashedPassword == null || hashedPassword.isBlank())
      throw new InvalidUserPasswordException("UserPassword cannot be null or blank");
  }

  public static UserPassword fromString(String hashedValue) {
    return new UserPassword(hashedValue);
  }

  public static UserPassword fromHash(String newHash) {
    return new UserPassword(newHash);
  }
}
