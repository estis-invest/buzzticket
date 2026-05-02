package com.efpcode.infrastructure.config;

import com.efpcode.application.port.security.PasswordHasher;
import com.efpcode.application.usecase.user.RegisterStaffUseCase;
import com.efpcode.domain.common.port.IdGenerator;
import com.efpcode.domain.partner.port.PartnerRepository;
import com.efpcode.domain.user.model.UserId;
import com.efpcode.domain.user.port.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserConfig {

  @Bean
  public RegisterStaffUseCase registerStaffUseCase(
      IdGenerator<UserId> idGenerator,
      UserRepository userRepository,
      PartnerRepository partnerRepository,
      PasswordHasher passwordHasher) {
    return new RegisterStaffUseCase(idGenerator, userRepository, partnerRepository, passwordHasher);
  }
}
