package com.efpcode.infrastructure.web.dto.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterUserPasswordChangeRequest(
    @NotBlank @Size(min = 8, max = 72) String currentPassword,
    @NotBlank @Size(min = 8, max = 72) String newPassword) {}
