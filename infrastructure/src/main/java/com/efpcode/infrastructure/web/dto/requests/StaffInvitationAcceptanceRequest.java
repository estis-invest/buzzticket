package com.efpcode.infrastructure.web.dto.requests;

import jakarta.validation.constraints.NotBlank;

public record StaffInvitationAcceptanceRequest(
    @NotBlank String rawToken, @NotBlank String name, @NotBlank String password) {}
