package com.efpcode.infrastructure.config;

import com.efpcode.infrastructure.config.properties.TicketSlugProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(TicketSlugProperties.class)
public class TicketSlugConfiguration {}
