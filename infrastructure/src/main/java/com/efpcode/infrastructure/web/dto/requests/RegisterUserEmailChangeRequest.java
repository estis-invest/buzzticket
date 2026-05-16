package com.efpcode.infrastructure.web.dto.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterUserEmailChangeRequest(@NotBlank @Email String email) {}
