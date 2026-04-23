package com.efpcode.domain.common.model;

import com.efpcode.domain.user.exceptions.InvalidUserPasswordException;

public record PlainPassword(String plainPassword) {

  public PlainPassword {
    if (plainPassword == null || plainPassword.isBlank()) {
      throw new InvalidUserPasswordException("Plain password cannot be null or blank");
    }
  }
}
