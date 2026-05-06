package com.efpcode.infrastructure.security;

import com.efpcode.infrastructure.security.exceptions.JwtIllegalFileIOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
public final class KeyLoader {

  public RSAPublicKey loadPublicKey(Resource keyResource) {

    try {
      if (keyResource == null || !keyResource.exists() || !keyResource.isReadable()) {
        throw new JwtIllegalFileIOException("Jwt public key is missing or unreadable", null);
      }

      String pem;

      try (InputStream keyStream = keyResource.getInputStream()) {
        pem = new String(keyStream.readAllBytes(), StandardCharsets.UTF_8);
      }

      String base64Key =
          extractBase64Key(pem, "-----BEGIN PUBLIC KEY-----", "-----END PUBLIC KEY-----");

      byte[] decoded = Base64.getDecoder().decode(base64Key);
      X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);

      KeyFactory factory = KeyFactory.getInstance("RSA");

      var publicKey = factory.generatePublic(spec);

      if (!(publicKey instanceof RSAPublicKey rsaPublicKey)) {
        throw new JwtIllegalFileIOException("Public key is not an RSA key", null);
      }

      return rsaPublicKey;
    } catch (JwtIllegalFileIOException e) {
      throw e;
    } catch (Exception e) {
      throw new JwtIllegalFileIOException("Failed to load JWT public key reason: ", e);
    }
  }

  public RSAPrivateKey loadPrivateKey(Resource key) {
    try {
      String pem;

      if (key == null || !key.exists() || !key.isReadable()) {
        throw new JwtIllegalFileIOException("JWT private key is missing or unreadable", null);
      }

      try (InputStream keyStream = key.getInputStream()) {
        pem = new String(keyStream.readAllBytes(), StandardCharsets.UTF_8);
      }

      String base64Key =
          extractBase64Key(pem, "-----BEGIN PRIVATE KEY-----", "-----END PRIVATE KEY-----");

      byte[] decoded = Base64.getDecoder().decode(base64Key);
      PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);

      var privateKey = KeyFactory.getInstance("RSA").generatePrivate(spec);

      if (!(privateKey instanceof RSAPrivateKey rsaPrivateKey)) {
        throw new JwtIllegalFileIOException("Private key is not a RSA key", null);
      }

      if (rsaPrivateKey.getModulus().bitLength() < 2048) {
        throw new JwtIllegalFileIOException("RSA key must be at least 2048 bits", null);
      }

      return rsaPrivateKey;
    } catch (JwtIllegalFileIOException e) {
      throw e;
    } catch (Exception e) {
      throw new JwtIllegalFileIOException("Failed to load private jwt key reason: ", e);
    }
  }

  private String extractBase64Key(String pem, String header, String footer) {
    if (!pem.contains(header) || !pem.contains(footer)) {
      throw new JwtIllegalFileIOException("Invalid key format: expected " + header, null);
    }

    return pem.replace(header, "").replace(footer, "").replace("\r", "").replace("\n", "").trim();
  }
}
