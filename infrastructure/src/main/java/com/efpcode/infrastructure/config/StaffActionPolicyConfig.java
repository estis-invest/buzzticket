package com.efpcode.infrastructure.config;

import com.efpcode.application.policy.staff.dto.StaffActionPolicy;
import com.efpcode.domain.partner.port.PartnerRepository;
import com.efpcode.domain.user.port.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class StaffActionPolicyConfig {

  @Bean
  public StaffActionPolicy staffActionPolicy(
      UserRepository userRepository, PartnerRepository partnerRepository) {
    return new StaffActionPolicy(userRepository, partnerRepository);
  }
}
