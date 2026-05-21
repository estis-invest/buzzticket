package com.efpcode.infrastructure.web.dto.requests;

import jakarta.validation.constraints.NotBlank;

public record UpdateTicketStatusRequest(@NotBlank String status) {}
