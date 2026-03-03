package com.efpcode.domain.user.model;

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
}
