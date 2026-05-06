package com.efpcode.infrastructure.security;

import com.efpcode.infrastructure.security.exceptions.JwtTokenRequiredFieldMissingException;
import java.security.interfaces.RSAPublicKey;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;

@Configuration
@Profile("!test")
class JwtDecoderConfiguration {
  private final KeyLoader keyLoader;

  public JwtDecoderConfiguration(KeyLoader keyLoader) {
    this.keyLoader = keyLoader;
  }

  @Bean
  JwtDecoder jwtDecoder(
      @Value("${spring.security.jwt.public-key-path}") Resource publicKeyResource,
      @Value("${spring.security.jwt.issuer}") String issuer,
      @Value("${spring.security.jwt.audience}") String audience)
      throws Exception {
    configValidator(issuer, audience);
    RSAPublicKey publicKey = keyLoader.loadPublicKey(publicKeyResource);
    NimbusJwtDecoder decoder = NimbusJwtDecoder.withPublicKey(publicKey).build();
    OAuth2TokenValidator<Jwt> issueValidator = JwtValidators.createDefaultWithIssuer(issuer);
    OAuth2TokenValidator<Jwt> audienceValidator =
        new JwtClaimValidator<List<String>>(
            JwtClaimNames.AUD, aud -> aud != null && aud.contains(audience));

    decoder.setJwtValidator(
        new DelegatingOAuth2TokenValidator<>(issueValidator, audienceValidator));

    return decoder;
  }

  private void configValidator(String issuer, String audience) {
    if (issuer == null || issuer.isBlank()) {
      throw new JwtTokenRequiredFieldMissingException(
          "JWT issuer must be configured (spring.security.jwt.issuer)", null);
    }
    if (audience == null || audience.isBlank()) {
      throw new JwtTokenRequiredFieldMissingException(
          "JWT audience must be configured (spring.security.jwt.audience)", null);
    }
  }
}
