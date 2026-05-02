package com.efpcode.infrastructure.security;

import com.efpcode.application.port.context.RequestContext;
import com.efpcode.domain.user.model.UserId;
import com.efpcode.domain.user.model.UserRole;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
class SpringSecurityRequestContext implements RequestContext {
  private Jwt jwt() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null) {
      throw new IllegalStateException("Authentication cannot be null");
    }

    if (!(authentication.getPrincipal() instanceof JwtAuthenticationToken jwtAuth)) {
      throw new IllegalStateException("Authentication is not JWT-based");
    }

    return jwtAuth.getToken();
  }

  @Override
  public UserId userId() {
    return UserId.fromString(jwt().getClaimAsString("userId"));
  }

  @Override
  public UserRole role() {
    return UserRole.valueOf(jwt().getClaimAsString("role"));
  }
}
