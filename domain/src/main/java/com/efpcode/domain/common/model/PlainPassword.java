package com.efpcode.domain.common.model;

import com.efpcode.domain.common.exceptions.InvalidCommonPasswordException;

public record PlainPassword(String plainPassword) {

  public PlainPassword {
    if (plainPassword == null || plainPassword.isBlank()) {
      throw new InvalidCommonPasswordException("Plain password cannot be null or blank");
    }
  }

  @Override
  public String toString() {
    return "PlainPassword{" + "plainPassword='" + "*".repeat(16) + '\'' + '}';
  }
}
