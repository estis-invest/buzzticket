package com.efpcode.infrastructure.web;

import com.efpcode.infrastructure.security.exceptions.JwtTokenRequiredFieldMissingException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import java.security.interfaces.RSAPublicKey;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@Profile("!test")
class JwksController {

  private final Map<String, Object> jwks;

  JwksController(RSAPublicKey publicKey, @Value("${spring.security.jwt.key-id}") String keyId) {

    if (keyId == null || keyId.isBlank()) {
      throw new JwtTokenRequiredFieldMissingException(
          "KeyId is not being loaded or not configured (spring.security.jwt-key-id)");
    }

    RSAKey jwk =
        new RSAKey.Builder(publicKey)
            .keyID(keyId)
            .algorithm(JWSAlgorithm.RS256)
            .keyUse(KeyUse.SIGNATURE)
            .build();
    this.jwks = Map.of("keys", List.of(jwk.toPublicJWK().toJSONObject()));
  }

  @GetMapping("/.well-known/jwks.json")
  Map<String, Object> jwks() {

    return jwks;
  }
}
