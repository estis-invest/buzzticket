package com.efpcode.domain.staffinvitation;

import com.efpcode.domain.staffinvitation.exceptions.IllegalStaffInvitationIdArgumentException;
import com.efpcode.domain.staffinvitation.exceptions.InvalidStaffInvitationIdException;
import java.util.UUID;

public record StaffInvitationId(UUID invitationId) {
  public StaffInvitationId {
    if (invitationId == null) {
      throw new InvalidStaffInvitationIdException("UUID is required and cannot be null", null);
    }
  }

  public static StaffInvitationId of(UUID value) {
    return new StaffInvitationId(value);
  }

  public static StaffInvitationId fromString(String uuid) {
    if (uuid == null || uuid.trim().isBlank()) {
      throw new IllegalStaffInvitationIdArgumentException(
          "fromString method cannot pass null or blank", null);
    }
    try {
      return new StaffInvitationId(UUID.fromString(uuid.trim()));

    } catch (IllegalArgumentException e) {
      throw new InvalidStaffInvitationIdException("Invalid or malformatted uuid", null);
    }
  }
}
