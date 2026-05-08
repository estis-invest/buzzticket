package com.efpcode.domain.staffinvitation;

import com.efpcode.domain.staffinvitation.exceptions.InvalidStaffInvitationStatusException;

public enum StaffInvitationStatus {
  PENDING {
    @Override
    public StaffInvitationStatus accept() {
      return ACCEPTED;
    }

    @Override
    public StaffInvitationStatus expire() {
      return EXPIRED;
    }
  },
  ACCEPTED,
  EXPIRED;

  public StaffInvitationStatus accept() {
    throw new InvalidStaffInvitationStatusException(
        "Invitation cannot be accepted when status is " + this);
  }

  public StaffInvitationStatus expire() {
    throw new InvalidStaffInvitationStatusException(
        "Invitation cannot be expired when status is " + this);
  }
}
