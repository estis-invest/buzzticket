package com.efpcode.infrastructure.security;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@Profile("!test")
public class SecurityConfiguration {
  @Bean
  @Order(1)
  SecurityFilterChain jwksChain(HttpSecurity http) throws Exception {
    http.securityMatcher("/.well-known/**")
        .csrf(AbstractHttpConfigurer::disable)
        .cors(withDefaults())
        .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
        .requestCache(AbstractHttpConfigurer::disable)
        .securityContext(AbstractHttpConfigurer::disable);
    return http.build();
  }

  @Bean
  @Order(2)
  SecurityFilterChain publicChain(HttpSecurity http) throws Exception {
    http.securityMatcher("/public/**")
        .csrf(AbstractHttpConfigurer::disable)
        .cors(withDefaults())
        .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
        .requestCache(AbstractHttpConfigurer::disable)
        .securityContext(AbstractHttpConfigurer::disable);
    return http.build();
  }

  @Bean
  @Order(3)
  SecurityFilterChain authChain(HttpSecurity http) throws Exception {
    http.securityMatcher("/auth/**")
        .csrf(AbstractHttpConfigurer::disable)
        .cors(withDefaults())
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers("/auth/login")
                    .permitAll()
                    .requestMatchers("/auth/logout", "/auth/refresh", "/auth/me")
                    .authenticated()
                    .anyRequest()
                    .denyAll())
        .oauth2ResourceServer(oauth -> oauth.jwt(withDefaults()));

    return http.build();
  }

  @Bean
  @Order(4)
  SecurityFilterChain apiChain(
      HttpSecurity http, JwtAuthenticationConverter jwtAuthenticationConverter) throws Exception {
    http.securityMatcher("/api/**")
        .csrf(AbstractHttpConfigurer::disable)
        .cors(withDefaults())
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
        .oauth2ResourceServer(
            oauth2 ->
                oauth2.jwt(
                    jwtConfigurer ->
                        jwtConfigurer.jwtAuthenticationConverter(jwtAuthenticationConverter)));
    return http.build();
  }
}
