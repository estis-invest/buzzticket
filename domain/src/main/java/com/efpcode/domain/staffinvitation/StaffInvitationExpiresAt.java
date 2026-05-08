package com.efpcode.domain.staffinvitation;

import com.efpcode.domain.staffinvitation.exceptions.InvalidStaffInvitationDateException;
import java.time.Instant;

public record StaffInvitationExpiresAt(Instant time) {
  private static final int MINIMUM_EXPIRY_SECONDS = 60;

  public StaffInvitationExpiresAt {
    if (time == null || time.equals(Instant.ofEpochMilli(0))) {
      throw new InvalidStaffInvitationDateException("Staff invitation expiry date is required");
    }
  }

  public static StaffInvitationExpiresAt of(Instant time, Instant now) {
    StaffInvitationExpiresAt expiresAt = new StaffInvitationExpiresAt(time);
    if (now == null || now.equals(Instant.ofEpochMilli(0))) {
      throw new InvalidStaffInvitationDateException("Current time is required");
    }
    if (expiresAt.time().isBefore(now.plusSeconds(MINIMUM_EXPIRY_SECONDS))) {
      throw new InvalidStaffInvitationDateException(
          String.format(
              "Staff invitation expiry must be at least %d seconds in the future",
              MINIMUM_EXPIRY_SECONDS));
    }

    return expiresAt;
  }
}
