package com.efpcode.infrastructure.security;

import com.efpcode.application.port.out.security.StaffInvitationTokenGenerator;
import com.efpcode.domain.common.model.PlainStaffInvitationToken;
import java.security.SecureRandom;
import java.util.Base64;
import org.springframework.stereotype.Component;

@Component
class RandomStaffInvitationTokenGenerator implements StaffInvitationTokenGenerator {

  private static final int TOKEN_BYTES = 32;
  private static final Base64.Encoder ENCODER = Base64.getUrlEncoder().withoutPadding();

  private final SecureRandom random = new SecureRandom();

  @Override
  public PlainStaffInvitationToken generate() {
    byte[] bytes = new byte[TOKEN_BYTES];
    random.nextBytes(bytes);
    return new PlainStaffInvitationToken(ENCODER.encodeToString(bytes));
  }
}
