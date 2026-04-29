package com.efpcode.infrastructure.security;

import com.efpcode.application.port.security.BootstrapTokenIssuer;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import java.time.Clock;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("!test")
@Component
public class JwtBootstrapTokenIssuerApdapter implements BootstrapTokenIssuer {

  private static final String ISSUER = "buzzticket-bootstrap";
  private static final String SCOPE = "BOOTSTRAP";

  private final BootstrapKeyProvider keyProvider;
  private final Clock clock;

  public JwtBootstrapTokenIssuerApdapter(BootstrapKeyProvider keyProvider, Clock clock) {
    this.keyProvider = keyProvider;
    this.clock = clock;
  }

  @Override
  public String issueToken() {
    try {
      Instant now = clock.instant();

      JWTClaimsSet claims =
          new JWTClaimsSet.Builder()
              .issuer(ISSUER)
              .claim("scope", SCOPE)
              .issueTime(Date.from(now))
              .expirationTime(Date.from(now.plusSeconds(300)))
              .jwtID(UUID.randomUUID().toString())
              .build();

      SignedJWT jwt = new SignedJWT(new JWSHeader(JWSAlgorithm.RS256), claims);
      JWSSigner signer = new RSASSASigner(keyProvider.privateKey());
      jwt.sign(signer);
      return jwt.serialize();
    } catch (JOSEException e) {
      throw new IllegalStateException("Failed to issue bootstrap token", e);
    }
  }

  @Override
  public void validateToken(String token) {
    try {
      SignedJWT jwt = SignedJWT.parse(token);
      JWSVerifier verifier = new RSASSAVerifier(keyProvider.publicKey());

      if (!jwt.verify(verifier)) {
        throw new SecurityException("Invalid bootstrap JWT signature");
      }

      JWTClaimsSet claims = jwt.getJWTClaimsSet();

      if (!ISSUER.equals(claims.getIssuer())) {
        throw new SecurityException("Invalid bootstrap JWT issuer");
      }

      if (!SCOPE.equals(claims.getStringClaim("scope"))) {
        throw new SecurityException("Invalid bootstrap JWT scope");
      }

      if (claims.getExpirationTime().before(Date.from(clock.instant()))) {
        throw new SecurityException("Bootstrap JWT expired");
      }

    } catch (Exception e) {
      throw new SecurityException("Invalid bootstrap token", e);
    }
  }
}
