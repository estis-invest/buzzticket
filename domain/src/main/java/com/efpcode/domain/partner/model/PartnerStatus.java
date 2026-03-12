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

  public boolean canBeEdit() {
    return this == ACTIVE;
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
    throw new IllegalPartnerStatusTransitionException(
        String.format(
            "PartnerStatus %s cannot be deleted. Only DEACTIVATED PartnerStatus can transition to DELETED",
            this));
  }

  public PartnerStatus toActivate() {
    return switch (this) {
      case DEACTIVATED, EDIT -> ACTIVE;
      default ->
          throw new IllegalPartnerStatusTransitionException(
              String.format(
                  "PartnerStatus %s cannot be activated. Only DEACTIVATED and EDIT PartnerStatus can transition to ACTIVE",
                  this));
    };
  }

  public PartnerStatus toEdit() {
    if (this == ACTIVE) return EDIT;
    throw new IllegalPartnerStatusTransitionException(
        String.format(
            "PartnerStatus %s cannot be edited. Only ACTIVE PartnerStatus can transition to EDIT",
            this));
  }

  public void partnerStatusEditGuard() {
    if (!canBeEdit()) {
      throw new IllegalPartnerStatusTransitionException(
          String.format("PartnerStatus: %s cannot be edited.", this));
    }
  }
}
