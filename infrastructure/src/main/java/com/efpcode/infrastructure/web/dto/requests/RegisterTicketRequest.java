package com.efpcode.infrastructure.web.dto.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record RegisterTicketRequest(
    @NotBlank String title,
    @NotBlank String description,
    @NotBlank String priority,
    @NotNull UUID partnerId) {}
