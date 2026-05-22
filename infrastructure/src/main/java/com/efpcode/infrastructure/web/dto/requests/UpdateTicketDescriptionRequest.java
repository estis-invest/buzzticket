package com.efpcode.infrastructure.web.dto.requests;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;

public record UpdateTicketDescriptionRequest(@NotBlank @Max(1800) String description) {}
