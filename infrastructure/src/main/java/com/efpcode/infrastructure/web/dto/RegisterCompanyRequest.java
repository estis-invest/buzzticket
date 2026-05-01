package com.efpcode.infrastructure.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.util.Locale;

public record RegisterCompanyRequest(
    @NotBlank String name,
    @NotBlank String city,
    @NotBlank String country,
    @NotBlank @Pattern(regexp = "^[A-Z]{3}$") String isoCode,
    @NotBlank String userName,
    @NotBlank String userPassword,
    @NotBlank @Email String userEmail) {

  public RegisterCompanyRequest {
    country = country.toUpperCase(Locale.ROOT);
  }
}
