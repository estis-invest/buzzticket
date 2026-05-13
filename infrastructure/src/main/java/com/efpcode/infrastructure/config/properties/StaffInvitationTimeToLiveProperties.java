package com.efpcode.infrastructure.config.properties;

import jakarta.validation.constraints.Positive;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "app.staff-invite")
@Validated
public record StaffInvitationTimeToLiveProperties(@Positive long ttlDays) {}
