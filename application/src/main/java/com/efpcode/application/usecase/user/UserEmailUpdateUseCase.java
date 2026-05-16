package com.efpcode.application.usecase.user;

import com.efpcode.application.context.RequestContext;
import com.efpcode.application.policy.user.UserAuthenticationPolicy;
import com.efpcode.application.policy.user.dto.UserContext;
import com.efpcode.application.usecase.user.dto.ChangeUserEmailCommand;
import com.efpcode.application.usecase.user.exceptions.IllegalUserEmailDuplicatedException;
import com.efpcode.domain.user.model.User;
import com.efpcode.domain.user.model.UserEmail;
import com.efpcode.domain.user.port.UserRepository;

public class UserEmailUpdateUseCase {

  private final UserRepository userRepository;
  private final UserAuthenticationPolicy authenticationPolicy;

  public UserEmailUpdateUseCase(
      UserRepository userRepository, UserAuthenticationPolicy authenticationPolicy) {
    this.userRepository = userRepository;
    this.authenticationPolicy = authenticationPolicy;
  }

  public void execute(ChangeUserEmailCommand command, RequestContext requestContext) {
    UserContext userContext = authenticationPolicy.userValidator(requestContext);

    UserEmail email = new UserEmail(command.email());

    if (!email.equals(userContext.user().email())) {

      if (userRepository.existsByEmail(email)) {
        throw new IllegalUserEmailDuplicatedException("Duplicated email cannot update");
      }
    }

    User updatedUser = userContext.user().changeEmail(email);

    userRepository.save(updatedUser);
  }
}
