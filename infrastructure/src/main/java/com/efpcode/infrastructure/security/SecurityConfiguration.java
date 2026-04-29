package com.efpcode.infrastructure.security;

import static org.springframework.security.config.Customizer.withDefaults;

import com.efpcode.infrastructure.security.filter.BootstrapAuthorizationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@Profile("!test")
public class SecurityConfiguration {
  @Bean
  SecurityFilterChain securityFilterChain(
      HttpSecurity http, BootstrapAuthorizationFilter bootstrapAuthorizationFilter)
      throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers(HttpMethod.POST, "/api/v1/bootstrap")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .addFilterBefore(bootstrapAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
        .httpBasic(withDefaults());

    return http.build();
  }
}
