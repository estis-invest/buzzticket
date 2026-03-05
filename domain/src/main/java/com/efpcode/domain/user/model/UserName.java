package com.efpcode.domain.user.model;

import com.efpcode.domain.user.exceptions.InvalidUserNameException;
import com.efpcode.domain.user.exceptions.UserNameLengthException;

public record UserName(String name) {
  private static final int MAX_CHARACTER_LIMIT = 50;

  public UserName {
    if (name == null || name.isBlank())
      throw new InvalidUserNameException("Name cannot be null or blank");

    if (name.length() > MAX_CHARACTER_LIMIT)
      throw new UserNameLengthException("Username cannot exceed " + MAX_CHARACTER_LIMIT + " limit");
  }
}
