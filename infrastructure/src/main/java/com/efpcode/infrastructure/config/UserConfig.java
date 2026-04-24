package com.efpcode.infrastructure.config;

import com.efpcode.application.port.security.PasswordHasher;
import com.efpcode.application.usecase.user.RegisterAdminUserUseCase;
import com.efpcode.domain.common.port.IdGenerator;
import com.efpcode.domain.user.model.UserId;
import com.efpcode.domain.user.port.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserConfig {

  @Bean
  public RegisterAdminUserUseCase registerAdminUserUseCase(
      UserRepository userRepository,
      IdGenerator<UserId> idGenerator,
      PasswordHasher passwordHasher) {
    return new RegisterAdminUserUseCase(userRepository, idGenerator, passwordHasher);
  }
}
