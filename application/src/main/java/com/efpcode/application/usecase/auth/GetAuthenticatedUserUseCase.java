package com.efpcode.application.usecase.auth;

import com.efpcode.application.context.RequestContext;
import com.efpcode.application.usecase.auth.dto.AuthenticatedUserResult;
import com.efpcode.application.usecase.auth.exceptions.AuthenticatedUserNotFoundException;
import com.efpcode.application.usecase.auth.exceptions.AuthenticatedUserStatusException;
import com.efpcode.domain.user.model.User;
import com.efpcode.domain.user.port.UserRepository;

public class GetAuthenticatedUserUseCase {
  private final UserRepository userRepository;

  public GetAuthenticatedUserUseCase(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public AuthenticatedUserResult execute(RequestContext requestContext) {
    User user =
        userRepository
            .findUserById(requestContext.userId())
            .orElseThrow(() -> new AuthenticatedUserNotFoundException("User not found"));

    if (!user.isActive()) {
      throw new AuthenticatedUserStatusException("User not active");
    }

    return AuthenticatedUserResult.from(user);
  }
}
