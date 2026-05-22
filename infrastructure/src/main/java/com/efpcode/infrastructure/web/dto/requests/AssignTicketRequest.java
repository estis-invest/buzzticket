package com.efpcode.infrastructure.web.dto.requests;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record AssignTicketRequest(@NotNull UUID assigneeId) {}
