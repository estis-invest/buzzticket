package com.efpcode.infrastructure.security;

import com.efpcode.infrastructure.security.exceptions.JwtIllegalFileIOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

@Configuration
@Profile("!test")
class JwtDecoderConfiguration {

  @Bean
  JwtDecoder jwtDecoder(@Value("${spring.security.jwt.public-key-path}") Resource publicKeyResource)
      throws Exception {
    RSAPublicKey publicKey = loadPublicKey(publicKeyResource);
    return NimbusJwtDecoder.withPublicKey(publicKey).build();
  }

  private RSAPublicKey loadPublicKey(Resource key) throws Exception {
    String pem;

    try (InputStream keyStream = key.getInputStream()) {
      pem = new String(keyStream.readAllBytes(), StandardCharsets.UTF_8);
    }

    if (!pem.contains("BEGIN PUBLIC KEY")) {
      throw new JwtIllegalFileIOException(
          "Invalid key format: only X.509 RSA public keys are supported", null);
    }

    String base64Key =
        pem.replace("-----BEGIN PUBLIC KEY-----", "")
            .replace("-----END PUBLIC KEY-----", "")
            .replace("\r", "")
            .replace("\n", "")
            .trim();

    byte[] decoded = Base64.getDecoder().decode(base64Key);
    X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);

    KeyFactory factory = KeyFactory.getInstance("RSA");
    return (RSAPublicKey) factory.generatePublic(spec);
  }
}
