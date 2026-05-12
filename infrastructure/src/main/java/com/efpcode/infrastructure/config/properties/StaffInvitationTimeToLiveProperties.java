package com.efpcode.infrastructure.config.properties;

import jakarta.validation.constraints.Positive;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.staff-invite")
public record StaffInvitationTimeToLiveProperties(@Positive long ttlDays) {}
