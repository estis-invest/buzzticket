package com.efpcode.domain.common.model;

import com.efpcode.domain.common.exceptions.InvalidCommonInvitationTokenException;

public record PlainStaffInvitationToken(String plainToken) {

  private static final int MINIMUM_LENGTH = 32;

  public PlainStaffInvitationToken {
    if (plainToken == null || plainToken.trim().isBlank()) {
      throw new InvalidCommonInvitationTokenException("Plain token cannot pass null or blank");
    }
    String normalized = plainToken.trim();

    if (normalized.trim().length() < MINIMUM_LENGTH) {
      throw new InvalidCommonInvitationTokenException(
          String.format("Plain token must be at least %d characters long", MINIMUM_LENGTH));
    }

    plainToken = normalized;
  }
}
