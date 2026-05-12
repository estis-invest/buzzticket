package com.efpcode.infrastructure.web.dto.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;

public record RegisterStaffInvitationRequest(
    @NotBlank @Email String inviteeEmail,
    @NotBlank String role,
    @NotNull @Future(message = "Date for expiresAt must be in the future") Instant expiresAt) {}
