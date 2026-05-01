package com.efpcode.infrastructure.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterCompanyRequest(
    @NotBlank String name,
    @NotBlank String city,
    @NotBlank String country,
    @NotBlank @Size(min = 3, max = 3) String isoCode,
    @NotBlank String userName,
    @NotBlank String userPassword,
    @NotBlank @Email String userEmail) {}
