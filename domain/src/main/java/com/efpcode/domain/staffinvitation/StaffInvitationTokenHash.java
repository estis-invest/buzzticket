package com.efpcode.domain.staffinvitation;

import com.efpcode.domain.staffinvitation.exceptions.InvalidStaffInvitationTokenException;

public record StaffInvitationTokenHash(String value) {

  public StaffInvitationTokenHash {
    if (value == null || value.trim().isBlank()) {
      throw new InvalidStaffInvitationTokenException("Invitation token hash is required", null);
    }
  }

  public static StaffInvitationTokenHash of(String hash) {
    return new StaffInvitationTokenHash(hash);
  }
}
