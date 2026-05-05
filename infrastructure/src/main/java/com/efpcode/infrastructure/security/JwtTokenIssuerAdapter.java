package com.efpcode.infrastructure.security;

import com.efpcode.application.port.security.JwtTokenIssuer;
import com.efpcode.domain.user.model.User;
import com.efpcode.infrastructure.security.exceptions.JwtIllegalFileIOException;
import com.efpcode.infrastructure.security.exceptions.JwtTokenRequiredFieldMissingException;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Clock;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Profile("!test")
@Component
class JwtTokenIssuerAdapter implements JwtTokenIssuer {

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
      Clock clock) {

    configValidator(issuer, audience, keyId, ttlSeconds, privateKeyResource);

    try {
      PrivateKey privateKey = loadPrivateKey(privateKeyResource);

      RSAPrivateKey rsa = (RSAPrivateKey) privateKey;
      if (rsa.getModulus().bitLength() < 2048) {
        throw new JwtIllegalFileIOException("RSA key must be at least 2048 bits", null);
      }

      this.signer = new RSASSASigner(privateKey);

    } catch (Exception e) {
      throw new JwtIllegalFileIOException("Failed to initialize JWT signer", e);
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
              .claim("role", user.role().name())
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
          "JWT issuer must be configured (spring.security.jwt.issuer)");
    }
    if (audience == null || audience.isBlank()) {
      throw new JwtTokenRequiredFieldMissingException(
          "JWT audience must be configured (spring.security.jwt.audience)");
    }
    if (key == null || keyId.isBlank()) {
      throw new JwtTokenRequiredFieldMissingException(
          "JWT key_id must be configured (spring.security.jwt.key-id)");
    }

    if (ttlSeconds <= 0) {
      throw new JwtTokenRequiredFieldMissingException(
          "JWT ttl-seconds must be configured (spring.security.jwt.ttl-seconds)");
    }

    if (!key.exists() || !key.isReadable()) {
      System.out.println("#### ".repeat(12) + key.toString());

      throw new JwtTokenRequiredFieldMissingException("JWT private key is missing or unreadable");
    }
  }

  private PrivateKey loadPrivateKey(Resource key) throws Exception {
    String pem;

    try (InputStream keyStream = key.getInputStream()) {
      pem = new String(keyStream.readAllBytes(), StandardCharsets.UTF_8);
    }

    if (!pem.contains("BEGIN PRIVATE KEY")) {
      throw new JwtIllegalFileIOException("Invalid key format: only PKCS#8 is supported", null);
    }

    String base64Key =
        pem.replace("-----BEGIN PRIVATE KEY-----", "")
            .replace("-----END PRIVATE KEY-----", "")
            .replace("\r", "")
            .replace("\n", "")
            .trim();

    byte[] decoded = Base64.getDecoder().decode(base64Key);
    PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);

    return KeyFactory.getInstance("RSA").generatePrivate(spec);
  }
}
