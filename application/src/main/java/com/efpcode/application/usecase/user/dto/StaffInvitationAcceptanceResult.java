package com.efpcode.application.usecase.user.dto;

import java.util.UUID;

public record StaffInvitationAcceptanceResult(UUID uuid, String name, String role) {}
