package com.efpcode.infrastructure.config;

import com.efpcode.infrastructure.config.properties.FrontendProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(FrontendProperties.class)
public class FrontendConfig {}
