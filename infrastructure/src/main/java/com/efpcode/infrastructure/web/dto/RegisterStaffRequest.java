package com.efpcode.infrastructure.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterStaffRequest(
    @NotBlank String name,
    @NotBlank @Email String email,
    @NotBlank @Size(min = 8) String password,
    @NotBlank String role) {

  public RegisterStaffRequest {
    if (role != null) {
      role = role.toUpperCase();
    }
  }
}
