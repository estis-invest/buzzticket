package com.efpcode.infrastructure.security;

import com.efpcode.application.port.out.security.StaffInvitationTokenHasher;
import com.efpcode.domain.common.model.PlainStaffInvitationToken;
import com.efpcode.domain.staffinvitation.model.StaffInvitationTokenHash;
import com.efpcode.infrastructure.config.properties.StaffInvitationTokenProperties;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.stereotype.Component;

@Component
public class HmacStaffInvitationTokenHasher implements StaffInvitationTokenHasher {

  private static final String ALGORITHM = "HmacSHA256";

  private final byte[] secretKeyBytes;

  public HmacStaffInvitationTokenHasher(StaffInvitationTokenProperties properties) {
    this.secretKeyBytes = properties.hmacSecret().getBytes(StandardCharsets.UTF_8);
  }

  @Override
  public StaffInvitationTokenHash hash(PlainStaffInvitationToken plainToken) {
    byte[] digest = computeHmac(plainToken.plainToken());
    return new StaffInvitationTokenHash(encode(digest));
  }

  @Override
  public boolean matches(PlainStaffInvitationToken plainToken, StaffInvitationTokenHash hash) {
    byte[] computed = computeHmac(plainToken.plainToken());
    byte[] stored = decode(hash.value());

    return MessageDigest.isEqual(computed, stored);
  }

  private byte[] computeHmac(String value) {
    try {
      Mac mac = Mac.getInstance(ALGORITHM);
      mac.init(new SecretKeySpec(secretKeyBytes, ALGORITHM));
      return mac.doFinal(value.getBytes(StandardCharsets.UTF_8));
    } catch (Exception e) {
      throw new IllegalStateException("Failed to compute staff invitation HMAC", e);
    }
  }

  private String encode(byte[] bytes) {
    return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
  }

  private byte[] decode(String value) {
    return Base64.getUrlDecoder().decode(value);
  }
}
