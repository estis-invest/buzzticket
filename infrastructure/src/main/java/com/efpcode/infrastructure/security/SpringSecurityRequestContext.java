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

    if (!(authentication instanceof JwtAuthenticationToken jwtAuth)) {
      throw new IllegalStateException("Authentication is not JWT-based");
    }

    return jwtAuth.getToken();
  }

  @Override
  public UserId userId() {
    String userId = jwt().getSubject();
    if (userId == null) {
      throw new IllegalStateException("JWT is missing required claim 'userId'");
    }

    return UserId.fromString(userId);
  }

  @Override
  public UserRole role() {

    String role = jwt().getClaimAsString("role");
    if (role == null) {
      throw new IllegalStateException("JWT is missing required claim 'role'");
    }

    return UserRole.valueOf(role);
  }
}
