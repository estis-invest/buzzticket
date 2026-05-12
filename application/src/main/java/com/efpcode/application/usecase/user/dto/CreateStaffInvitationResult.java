package com.efpcode.application.usecase.user.dto;

import java.time.Instant;
import java.util.UUID;

public record CreateStaffInvitationResult(
    UUID invitationId,
    String inviteeEmail,
    String role,
    String status,
    Instant expiresAt,
    String rawToken) {}
