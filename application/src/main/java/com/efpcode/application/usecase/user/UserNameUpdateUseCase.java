package com.efpcode.application.usecase.user;

import com.efpcode.application.context.RequestContext;
import com.efpcode.application.policy.user.UserAuthenticationPolicy;
import com.efpcode.application.policy.user.dto.UserContext;
import com.efpcode.application.usecase.user.dto.ChangeUserNameCommand;
import com.efpcode.domain.user.model.User;
import com.efpcode.domain.user.model.UserName;
import com.efpcode.domain.user.port.UserRepository;

public class UserNameUpdateUseCase {
  private final UserRepository userRepository;
  private final UserAuthenticationPolicy authenticationPolicy;

  public UserNameUpdateUseCase(
      UserRepository userRepository, UserAuthenticationPolicy userAuthenticationPolicy) {
    this.userRepository = userRepository;
    this.authenticationPolicy = userAuthenticationPolicy;
  }

  public void execute(ChangeUserNameCommand command, RequestContext requestContext) {
    UserContext userContext = authenticationPolicy.userValidator(requestContext);
    UserName userName = new UserName(command.name());

    User updatedUser = userContext.user().changeName(userName);

    userRepository.save(updatedUser);
  }
}
