package com.efpcode.application.usecase.user;

import com.efpcode.application.context.RequestContext;
import com.efpcode.application.policy.user.UserAuthenticationPolicy;
import com.efpcode.application.port.out.security.PasswordHasher;
import com.efpcode.application.usecase.auth.exceptions.InvalidAuthenticationException;
import com.efpcode.application.usecase.user.dto.UserAccountDeactivationCommand;
import com.efpcode.application.usecase.user.exceptions.PasswordFailException;
import com.efpcode.domain.common.model.PlainPassword;
import com.efpcode.domain.user.model.User;
import com.efpcode.domain.user.port.UserRepository;

public class AccountDeactivationUseCase {

  private final UserRepository userRepository;
  private final UserAuthenticationPolicy authenticationPolicy;
  private final PasswordHasher passwordHasher;

  public AccountDeactivationUseCase(
      UserRepository userRepository,
      UserAuthenticationPolicy authenticationPolicy,
      PasswordHasher passwordHasher) {
    this.userRepository = userRepository;
    this.authenticationPolicy = authenticationPolicy;
    this.passwordHasher = passwordHasher;
  }

  public void execute(UserAccountDeactivationCommand command, RequestContext requestContext) {

    User user = authenticationPolicy.userAccountValidator(requestContext);

    if (command.currentPassword() == null || command.currentPassword().trim().isBlank()) {
      throw new PasswordFailException("Required field 'current password' missing");
    }

    PlainPassword plainPassword = new PlainPassword(command.currentPassword());

    if (!passwordHasher.matches(plainPassword, user.password())) {
      throw new InvalidAuthenticationException("Authentication failed");
    }

    User deactivatedUser = user.deactivate();

    userRepository.save(deactivatedUser);
  }
}
