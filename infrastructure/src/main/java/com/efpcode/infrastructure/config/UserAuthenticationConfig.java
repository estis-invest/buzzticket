package com.efpcode.infrastructure.config;

import com.efpcode.application.policy.user.UserAuthenticationPolicy;
import com.efpcode.domain.partner.port.PartnerRepository;
import com.efpcode.domain.user.port.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class UserAuthenticationConfig {

  @Bean
  public UserAuthenticationPolicy userAuthenticationPolicy(
      UserRepository userRepository, PartnerRepository partnerRepository) {
    return new UserAuthenticationPolicy(userRepository, partnerRepository);
  }
}
