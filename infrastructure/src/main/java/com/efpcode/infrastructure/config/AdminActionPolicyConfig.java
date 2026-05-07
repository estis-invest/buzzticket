package com.efpcode.infrastructure.config;

import com.efpcode.application.policy.admin.AdminActionPolicy;
import com.efpcode.domain.partner.port.PartnerRepository;
import com.efpcode.domain.user.port.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AdminActionPolicyConfig {

  @Bean
  public AdminActionPolicy adminActionPolicy(
      UserRepository userRepository, PartnerRepository partnerRepository) {
    return new AdminActionPolicy(userRepository, partnerRepository);
  }
}
