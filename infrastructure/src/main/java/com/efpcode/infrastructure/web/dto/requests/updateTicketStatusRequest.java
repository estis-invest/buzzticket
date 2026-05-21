package com.efpcode.infrastructure.web.dto.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record updateTicketStatusRequest(@NotBlank String status, @NotNull UUID ticketId) {}
