package com.efpcode.infrastructure.config.properties;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "security.staff-invite")
public record StaffInvitationTokenProperties(@NotBlank String hmacSecret) {

  @Override
  public String toString() {
    return "StaffInvitationTokenProperties hmacSecret='**********************************'";
  }
}
