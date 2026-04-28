package com.efpcode.infrastructure.config;

import com.efpcode.application.port.security.PasswordHasher;
import com.efpcode.application.usecase.bootstrap.RegisterBootstrapUseCase;
import com.efpcode.domain.common.port.IdGenerator;
import com.efpcode.domain.partner.model.PartnerId;
import com.efpcode.domain.partner.port.PartnerRepository;
import com.efpcode.domain.user.model.UserId;
import com.efpcode.domain.user.port.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

@Configuration
public class BootstrapConfig {

  @Bean
  @Transactional
  public RegisterBootstrapUseCase bootstrapUseCase(
      IdGenerator<PartnerId> idPartnerGenerator,
      IdGenerator<UserId> idUserGenerator,
      PartnerRepository partnerRepository,
      UserRepository userRepository,
      PasswordHasher passwordHasher) {
    return new RegisterBootstrapUseCase(
        idPartnerGenerator, idUserGenerator, partnerRepository, userRepository, passwordHasher);
  }
}
