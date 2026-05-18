package com.efpcode.infrastructure.web.dto.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterAccountDeactivationRequest(
    @NotBlank @Size(min = 8, max = 72) String currentPassword) {
  @Override
  public String toString() {
    return "RegisterAccountDeactivationRequest{" + "currentPassword='" + " REDACTED " + '\'' + '}';
  }
}
