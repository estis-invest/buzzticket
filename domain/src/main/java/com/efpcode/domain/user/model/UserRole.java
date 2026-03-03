package com.efpcode.domain.user.model;

import com.efpcode.domain.user.exceptions.IllegalRoleTransitionException;

public enum UserRole {
  CUSTOMER,
  SUPPORT,
  ADMIN;

  public boolean requiresPartner() {
    return this == SUPPORT || this == ADMIN;
  }

  public boolean canAssignTicket() {
    return this == SUPPORT || this == ADMIN;
  }

  public UserRole canChangeRoleTo() {

    return switch (this) {
      case ADMIN -> SUPPORT;
      case SUPPORT -> ADMIN;
      case CUSTOMER ->
          throw new IllegalRoleTransitionException(
              String.format("User role %s cannot be changed", this));
    };
  }
}
