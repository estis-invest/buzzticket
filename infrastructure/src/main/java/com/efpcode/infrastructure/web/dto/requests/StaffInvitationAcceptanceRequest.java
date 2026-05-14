package com.efpcode.infrastructure.web.dto.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record StaffInvitationAcceptanceRequest(
    @NotBlank String rawToken,
    @NotBlank String name,
    @NotBlank @Size(min = 8, max = 72) String password) {}
