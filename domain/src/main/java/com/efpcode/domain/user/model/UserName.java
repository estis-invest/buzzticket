package com.efpcode.domain.user.model;

import com.efpcode.domain.user.exceptions.InvalidUserNameException;

public record UserName(String name) {

  public UserName {
    if (name == null || name.isBlank())
      throw new InvalidUserNameException("Name cannot be null or blank");
  }
}
