package com.efpcode.infrastructure.config.properties;

import jakarta.validation.constraints.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "ticket.slug")
@Validated
public record TicketSlugProperties(
    @NotBlank @Pattern(regexp = "^[A-Z]{3}$", message = "Expected: 3 uppercase letters.")
        String prefix,
    @Min(8) @Max(28) int lengthPadding) {}
