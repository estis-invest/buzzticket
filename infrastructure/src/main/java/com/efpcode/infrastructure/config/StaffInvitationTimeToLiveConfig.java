package com.efpcode.infrastructure.config;

import com.efpcode.infrastructure.config.properties.StaffInvitationTimeToLiveProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(StaffInvitationTimeToLiveProperties.class)
public class StaffInvitationTimeToLiveConfig {}
