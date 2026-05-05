package com.efpcode.infrastructure.config;

import com.efpcode.application.port.security.DummyPasswordHashProvider;
import com.efpcode.application.port.security.JwtTokenIssuer;
import com.efpcode.application.port.security.PasswordHasher;
import com.efpcode.application.usecase.auth.GetLoginUseCase;
import com.efpcode.domain.user.port.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!test")
public class AuthConfig {

  @Bean
  public GetLoginUseCase getLoginUseCase(
      UserRepository userRepository,
      PasswordHasher passwordHasher,
      JwtTokenIssuer tokenIssuer,
      DummyPasswordHashProvider dummyPasswordHasHProvider) {
    return new GetLoginUseCase(
        userRepository, passwordHasher, tokenIssuer, dummyPasswordHasHProvider);
  }
}
