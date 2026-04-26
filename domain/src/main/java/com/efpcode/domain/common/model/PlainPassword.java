package com.efpcode.domain.common.model;

import com.efpcode.domain.common.exceptions.InvalidCommonPasswordException;

public record PlainPassword(String plainPassword) {

  public PlainPassword {
    if (plainPassword == null || plainPassword.trim().isBlank()) {
      throw new InvalidCommonPasswordException(
          "PlainPassword cannot pass null or blank as argument");
    }
  }

  @Override
  public String toString() {
    return "PlainPassword{" + "plainPassword='" + "*".repeat(16) + '\'' + '}';
  }
}
