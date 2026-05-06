package com.efpcode.application.port.security;

import com.efpcode.domain.user.model.User;

public interface JwtTokenIssuer {
  String issueToken(User user);
}
