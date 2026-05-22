package com.efpcode.infrastructure.web.dto.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateTicketDescriptionRequest(@NotBlank @Size(max = 1800) String description) {}
