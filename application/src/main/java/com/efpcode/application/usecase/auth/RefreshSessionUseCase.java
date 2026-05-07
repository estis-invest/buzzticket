package com.efpcode.application.usecase.auth;

import com.efpcode.application.context.RequestContext;
import com.efpcode.application.port.out.security.JwtTokenIssuer;
import com.efpcode.application.usecase.auth.dto.AuthResult;
import com.efpcode.application.usecase.auth.exceptions.AuthenticatedUserNotFoundException;
import com.efpcode.application.usecase.auth.exceptions.AuthenticatedUserStatusException;
import com.efpcode.domain.user.model.User;
import com.efpcode.domain.user.port.UserRepository;

public class RefreshSessionUseCase {

  private final UserRepository userRepository;
  private final JwtTokenIssuer jwtTokenIssuer;

  public RefreshSessionUseCase(UserRepository userRepository, JwtTokenIssuer jwtTokenIssuer) {
    this.userRepository = userRepository;
    this.jwtTokenIssuer = jwtTokenIssuer;
  }

  public AuthResult execute(RequestContext requestContext) {

    User user =
        userRepository
            .findUserById(requestContext.userId())
            .orElseThrow(() -> new AuthenticatedUserNotFoundException("User not found"));

    if (!user.isActive()) {
      throw new AuthenticatedUserStatusException("User not active");
    }

    if (!user.role().toString().equals(requestContext.role().toString())) {
      throw new AuthenticatedUserStatusException("Claim mismatch");
    }

    return new AuthResult(jwtTokenIssuer.issueToken(user));
  }
}
