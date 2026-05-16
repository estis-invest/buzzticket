package com.efpcode.infrastructure.web.dto.requests;

import jakarta.validation.constraints.NotBlank;

public record RegisterUserNameChangeRequest(@NotBlank String name) {}
