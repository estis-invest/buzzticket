package com.efpcode.infrastructure.config;

import com.efpcode.application.policy.partner.PartnerAdminActionPolicy;
import com.efpcode.domain.partner.port.PartnerRepository;
import com.efpcode.domain.user.port.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class PartnerAdminActionConfig {

  @Bean
  public PartnerAdminActionPolicy partnerAdminActionPolicy(
      UserRepository userRepository, PartnerRepository partnerRepository) {
    return new PartnerAdminActionPolicy(userRepository, partnerRepository);
  }
}
