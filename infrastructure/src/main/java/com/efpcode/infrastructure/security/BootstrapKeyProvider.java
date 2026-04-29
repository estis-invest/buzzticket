package com.efpcode.infrastructure.security;

import com.efpcode.infrastructure.security.exceptions.BootstrapKeyMissingException;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.RSAKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("!test")
@Component
public class BootstrapKeyProvider {

  private final RSAKey rsaKey;

  public BootstrapKeyProvider(
      @Value("${buzzticket.bootstrap.private-key:}") String privateKeyPem,
      @Value("${buzzticket.bootstrap.public-key:}") String publicKeyPem) {

    if (privateKeyPem == null || privateKeyPem.isBlank()) {
      throw new BootstrapKeyMissingException(
          "Missing required configuration: BUZZTICKET_BOOTSTRAP_PRIVATE_KEY");
    }

    if (publicKeyPem == null || publicKeyPem.isBlank()) {
      throw new BootstrapKeyMissingException(
          "Missing required configuration: BUZZTICKET_BOOTSTRAP_PUBLIC_KEY");
    }

    try {
      this.rsaKey =
          RSAKey.parseFromPEMEncodedObjects(privateKeyPem + "\n" + publicKeyPem).toRSAKey();
    } catch (JOSEException e) {
      throw new IllegalStateException("Invalid bootstrap RSA keys", e);
    }
  }

  public RSAPrivateKey privateKey() {
    try {
      return rsaKey.toRSAPrivateKey();

    } catch (JOSEException e) {
      throw new IllegalStateException("Failed to extract RSA private key", e);
    }
  }

  public RSAPublicKey publicKey() {
    try {
      return rsaKey.toRSAPublicKey();

    } catch (JOSEException e) {
      throw new IllegalStateException("Failed to extract RSA public key", e);
    }
  }
}
