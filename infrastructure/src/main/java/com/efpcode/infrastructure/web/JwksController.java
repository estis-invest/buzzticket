package com.efpcode.infrastructure.web;

import com.efpcode.infrastructure.security.KeyLoader;
import com.efpcode.infrastructure.security.exceptions.JwtTokenRequiredFieldMissingException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/.well-known/jwks.json")
@Profile("!test")
class JwksController {

  private final KeyLoader keyLoader;
  private final String keyId;
  private final Resource publicKeyResource;

  JwksController(
      KeyLoader keyLoader,
      @Value("${spring.security.jwt.key-id}") String keyId,
      @Value("${spring.security.jwt.public-key-path}") Resource publicKeyResource) {

    if (keyId == null || keyId.isBlank()) {
      throw new JwtTokenRequiredFieldMissingException(
          "KeyId is not being loaded or not configured (spring.security.jwt-key-id)", null);
    }

    this.keyId = keyId;
    this.publicKeyResource = publicKeyResource;
    this.keyLoader = keyLoader;
  }

  @GetMapping
  Map<String, Object> jwks() {
    RSAPublicKey publicKey = keyLoader.loadPublicKey(publicKeyResource);
    RSAKey jwk =
        new RSAKey.Builder(publicKey)
            .keyID(keyId)
            .algorithm(JWSAlgorithm.RS256)
            .keyUse(KeyUse.SIGNATURE)
            .build();
    return new JWKSet(jwk).toJSONObject();
  }
}
