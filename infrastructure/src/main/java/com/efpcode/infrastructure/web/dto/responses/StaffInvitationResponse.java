package com.efpcode.infrastructure.web.dto.responses;

import java.time.Instant;
import java.util.UUID;

public record StaffInvitationResponse(
    UUID invitationId,
    String inviteeEmail,
    String role,
    String status,
    Instant expiresAt,
    String inviteLink) {}
