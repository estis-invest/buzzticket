package com.efpcode.infrastructure.config;

import com.efpcode.application.port.out.security.DummyPasswordHashProvider;
import com.efpcode.application.port.out.security.JwtTokenIssuer;
import com.efpcode.application.port.out.security.PasswordHasher;
import com.efpcode.application.usecase.auth.GetAuthenticatedUserUseCase;
import com.efpcode.application.usecase.auth.GetLoginUseCase;
import com.efpcode.application.usecase.auth.RefreshSessionUseCase;
import com.efpcode.domain.user.port.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
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

  @Bean
  public GetAuthenticatedUserUseCase getAuthenticatedUserUseCase(UserRepository userRepository) {
    return new GetAuthenticatedUserUseCase(userRepository);
  }

  @Bean
  RefreshSessionUseCase refreshSessionUseCase(
      UserRepository userRepository, JwtTokenIssuer jwtTokenIssuer) {
    return new RefreshSessionUseCase(userRepository, jwtTokenIssuer);
  }
}
