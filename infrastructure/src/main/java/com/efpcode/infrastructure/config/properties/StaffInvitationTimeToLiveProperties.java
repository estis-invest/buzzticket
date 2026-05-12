package com.efpcode.infrastructure.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.staff-invite")
public record StaffInvitationTimeToLiveProperties(int ttlDays) {}
