package com.efpcode.infrastructure.web.dto.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CustomerRegistrationRequest(
    @NotBlank(message = "Required field 'name' cannot be null or blank") String name,
    @NotBlank(message = "Required field 'email' cannot be null or blank")
        @Email(message = "Email must be valid")
        String email,
    @NotBlank(message = "Required field 'password' cannot be null or blank")
        @Size(min = 8, max = 72, message = "Password must be between 8 to 72 characters long")
        String password) {}
