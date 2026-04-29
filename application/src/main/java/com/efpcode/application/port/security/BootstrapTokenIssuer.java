package com.efpcode.application.port.security;

public interface BootstrapTokenIssuer {
  String issueToken();

  void validateToken(String token);
}
