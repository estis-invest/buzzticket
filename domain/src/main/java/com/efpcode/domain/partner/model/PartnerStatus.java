package com.efpcode.domain.partner.model;

import com.efpcode.domain.partner.exceptions.IllegalPartnerStatusTransitionException;

public enum PartnerStatus {
  EDIT,
  ACTIVE,
  DEACTIVATED,
  DELETED;

  public boolean isActive() {
    return this == ACTIVE;
  }

  public boolean isDeleted() {
    return this == DELETED;
  }

  public boolean isEdit() {
    return this == EDIT;
  }

  public boolean isDeactivated() {
    return this == DEACTIVATED;
  }

  public PartnerStatus toDeactivate() {
    if (this == ACTIVE) return DEACTIVATED;
    throw new IllegalPartnerStatusTransitionException(
        String.format(
            "PartnerStatus %s cannot be deactivated. Only ACTIVE PartnerStatus can transition to DEACTIVATED",
            this));
  }

  public PartnerStatus toDelete() {
    if (this == DEACTIVATED) return DELETED;
    return this;
  }

  public PartnerStatus toActivate() {
    return switch (this) {
      case DEACTIVATED, EDIT -> ACTIVE;
      default -> this;
    };
  }

  public PartnerStatus toEdit() {
    return switch (this) {
      case ACTIVE -> EDIT;
      default -> this;
    };
  }
}
