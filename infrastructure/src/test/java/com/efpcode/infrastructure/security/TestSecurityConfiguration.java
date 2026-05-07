package com.efpcode.infrastructure.security;

import com.efpcode.application.context.RequestContext;
import com.efpcode.application.port.out.security.JwtTokenIssuer;
import com.efpcode.application.port.out.security.PasswordHasher;
import com.efpcode.domain.user.model.UserId;
import com.efpcode.domain.user.model.UserRole;
import com.efpcode.infrastructure.testhelper.TestPasswordHasher;
import java.util.UUID;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@TestConfiguration
public class TestSecurityConfiguration {
  @Bean
  @Order(Ordered.HIGHEST_PRECEDENCE)
  SecurityFilterChain securityFilterChainTest(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
    return http.build();
  }

  @Bean
  JwtTokenIssuer jwtTokenIssuer() {
    return user -> "test-token";
  }

  @Bean
  PasswordHasher passwordHasher() {
    return new TestPasswordHasher();
  }

  @Bean
  @Primary
  RequestContext requestContext() {
    return new RequestContext() {
      @Override
      public UserId userId() {
        return new UserId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
      }

      @Override
      public UserRole role() {
        return UserRole.ADMIN;
      }
    };
  }
}
