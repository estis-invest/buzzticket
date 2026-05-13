package com.efpcode.infrastructure.config;

import com.efpcode.infrastructure.config.properties.StaffInvitationTokenProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(StaffInvitationTokenProperties.class)
class StaffInvitationSecurityConfiguration {}
