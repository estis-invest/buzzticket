package com.efpcode.infrastructure.security;

import com.efpcode.application.port.security.JwtTokenIssuer;
import com.efpcode.domain.user.model.User;
import com.efpcode.infrastructure.security.exceptions.JwtIllegalFileIOException;
import com.efpcode.infrastructure.security.exceptions.JwtTokenRequiredFieldMissingException;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import java.security.PrivateKey;
import java.time.Clock;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Profile("!test")
@Component
public class JwtTokenIssuerAdapter implements JwtTokenIssuer {

  private final JWSSigner signer;
  private final String issuer;
  private final String audience;
  private final String keyId;
  private final long ttlSeconds;
  private final Clock clock;

  JwtTokenIssuerAdapter(
      @Value("${spring.security.jwt.private-key-path}") Resource privateKeyResource,
      @Value("${spring.security.jwt.issuer}") String issuer,
      @Value("${spring.security.jwt.audience}") String audience,
      @Value("${spring.security.jwt.key-id}") String keyId,
      @Value("${spring.security.jwt.ttl-seconds}") long ttlSeconds,
      Clock clock,
      KeyLoader keyLoader) {

    configValidator(issuer, audience, keyId, ttlSeconds, privateKeyResource);

    try {
      PrivateKey privateKey = keyLoader.loadPrivateKey(privateKeyResource);
      this.signer = new RSASSASigner(privateKey);
    } catch (JwtIllegalFileIOException e) {
      throw e;
    } catch (Exception e) {
      throw new JwtIllegalFileIOException("Failed to initialize or load JWT signing", e);
    }

    this.issuer = issuer;
    this.ttlSeconds = ttlSeconds;
    this.clock = clock;
    this.audience = audience;
    this.keyId = keyId;
  }

  @Override
  public String issueToken(User user) {
    try {
      Instant now = Instant.now(clock);

      JWTClaimsSet claims =
          new JWTClaimsSet.Builder()
              .issuer(issuer)
              .subject(user.id().id().toString())
              .audience(audience)
              .jwtID(UUID.randomUUID().toString())
              .issueTime(Date.from(now))
              .expirationTime(Date.from(now.plusSeconds(ttlSeconds)))
              .claim("role", List.of(user.role().name()))
              .build();

      JWSHeader header =
          new JWSHeader.Builder(JWSAlgorithm.RS256).keyID(keyId).type(JOSEObjectType.JWT).build();

      SignedJWT jwt = new SignedJWT(header, claims);
      jwt.sign(signer);

      return jwt.serialize();

    } catch (JOSEException e) {
      throw new JwtIllegalFileIOException("Jwt signing failed", e);
    }
  }

  private void configValidator(
      String issuer, String audience, String keyId, long ttlSeconds, Resource key) {

    if (issuer == null || issuer.isBlank()) {
      throw new JwtTokenRequiredFieldMissingException(
          "JWT issuer must be configured (spring.security.jwt.issuer)", null);
    }
    if (audience == null || audience.isBlank()) {
      throw new JwtTokenRequiredFieldMissingException(
          "JWT audience must be configured (spring.security.jwt.audience)", null);
    }
    if (keyId == null || keyId.isBlank()) {
      throw new JwtTokenRequiredFieldMissingException(
          "JWT key_id must be configured (spring.security.jwt.key-id)", null);
    }

    if (ttlSeconds <= 0) {
      throw new JwtTokenRequiredFieldMissingException(
          "JWT ttl-seconds must be configured (spring.security.jwt.ttl-seconds)", null);
    }

    if (key == null || !key.exists() || !key.isReadable()) {
      throw new JwtTokenRequiredFieldMissingException(
          "JWT private key is missing or unreadable", null);
    }
  }
}
