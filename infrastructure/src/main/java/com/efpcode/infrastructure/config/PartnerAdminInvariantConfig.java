package com.efpcode.infrastructure.config;

import com.efpcode.application.policy.partner.PartnerAdminInvariantPolicy;
import com.efpcode.domain.user.port.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class PartnerAdminInvariantConfig {

  @Bean
  public PartnerAdminInvariantPolicy partnerAdminInvariantPolicy(UserRepository userRepository) {
    return new PartnerAdminInvariantPolicy(userRepository);
  }
}
