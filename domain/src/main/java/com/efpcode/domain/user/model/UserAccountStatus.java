package com.efpcode.domain.user.model;

public enum UserAccountStatus {
  ACTIVATED,
  DEACTIVATED;

  public boolean isActive() {
    return this == ACTIVATED;
  }
}
