package com.efpcode.application.usecase.user;

import com.efpcode.application.context.RequestContext;
import com.efpcode.application.policy.user.UserAuthenticationPolicy;
import com.efpcode.application.policy.user.dto.UserContext;
import com.efpcode.application.port.out.security.PasswordHasher;
import com.efpcode.application.usecase.auth.exceptions.InvalidAuthenticationException;
import com.efpcode.application.usecase.auth.exceptions.PasswordFailException;
import com.efpcode.application.usecase.user.dto.ChangeUserPasswordCommand;
import com.efpcode.domain.common.model.PlainPassword;
import com.efpcode.domain.user.model.User;
import com.efpcode.domain.user.model.UserPassword;
import com.efpcode.domain.user.port.UserRepository;

public class UserPasswordUpdateUseCase {
  private final UserRepository userRepository;
  private final UserAuthenticationPolicy authenticationPolicy;
  private final PasswordHasher passwordHasher;

  public UserPasswordUpdateUseCase(
      UserRepository userRepository,
      UserAuthenticationPolicy authenticationPolicy,
      PasswordHasher passwordHasher) {
    this.userRepository = userRepository;
    this.authenticationPolicy = authenticationPolicy;
    this.passwordHasher = passwordHasher;
  }

  public void execute(ChangeUserPasswordCommand command, RequestContext requestContext) {
    UserContext userContext = authenticationPolicy.userValidator(requestContext);

    if (command.currentPassword() == null
        || command.currentPassword().trim().isBlank()
        || command.newPassword() == null
        || command.newPassword().trim().isBlank()) {
      throw new PasswordFailException(
          "Required fields 'current password' and 'new password' are missing");
    }

    if (command.currentPassword().equals(command.newPassword())) {
      throw new PasswordFailException("New password must be different from current password");
    }

    PlainPassword plainCurrentPassword = new PlainPassword(command.currentPassword());

    if (!passwordHasher.matches(plainCurrentPassword, userContext.user().password())) {
      throw new InvalidAuthenticationException("Incorrect credentials");
    }

    PlainPassword plainNewPassword = new PlainPassword(command.newPassword());
    UserPassword newPassword = passwordHasher.hash(plainNewPassword);
    User updatedUser = userContext.user().changePassword(newPassword);
    userRepository.save(updatedUser);
  }
}
