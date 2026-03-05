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

  public UserRole promote() {
    return switch (this) {
      case SUPPORT -> ADMIN;
      case ADMIN ->
          throw new IllegalRoleTransitionException(
              String.format("User is already %s. Cannot be promoted", this));
      case CUSTOMER ->
          throw new IllegalRoleTransitionException(
              String.format("User role %s cannot be promoted", this));
    };
  }

  public UserRole demote() {
    return switch (this) {
      case ADMIN -> SUPPORT;
      case SUPPORT ->
          throw new IllegalRoleTransitionException(
              String.format("User is already %s. Cannot be demoted", this));
      case CUSTOMER ->
          throw new IllegalRoleTransitionException(
              String.format("User role %s cannot be demoted", this));
    };
  }
}
