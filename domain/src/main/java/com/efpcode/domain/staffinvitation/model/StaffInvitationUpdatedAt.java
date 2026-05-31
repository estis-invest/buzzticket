package com.efpcode.domain.staffinvitation.model;

import com.efpcode.domain.staffinvitation.exceptions.InvalidStaffInvitationDateException;
import java.time.Instant;

public record StaffInvitationUpdatedAt(Instant time) {
  private static final int GRACE_PERIOD = 60;

  public StaffInvitationUpdatedAt {
    if (time == null || time.equals(Instant.ofEpochMilli(0))) {
      throw new InvalidStaffInvitationDateException("Staff invitation requires date", null);
    }

    if (time.isAfter(Instant.now().plusSeconds(GRACE_PERIOD))) {
      throw new InvalidStaffInvitationDateException(
          "Staff invitation updated-at cannot be in the future", null);
    }
  }
}
