package com.efpcode.infrastructure.adapters;

import com.efpcode.application.context.RequestContext;
import com.efpcode.application.port.in.user.UserAccountCommands;
import com.efpcode.application.usecase.user.AccountDeactivationUseCase;
import com.efpcode.application.usecase.user.UserEmailUpdateUseCase;
import com.efpcode.application.usecase.user.UserNameUpdateUseCase;
import com.efpcode.application.usecase.user.UserPasswordUpdateUseCase;
import com.efpcode.application.usecase.user.dto.ChangeUserEmailCommand;
import com.efpcode.application.usecase.user.dto.ChangeUserNameCommand;
import com.efpcode.application.usecase.user.dto.ChangeUserPasswordCommand;
import com.efpcode.application.usecase.user.dto.UserAccountDeactivationCommand;
import com.efpcode.application.usecase.user.exceptions.IllegalUserEmailDuplicatedException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserUpdateTransactionalAdapter implements UserAccountCommands {

  private final RequestContext requestContext;
  private final UserNameUpdateUseCase userNameUpdateUseCase;
  private final UserEmailUpdateUseCase userEmailUpdateUseCase;
  private final UserPasswordUpdateUseCase userPasswordUpdateUseCase;
  private final AccountDeactivationUseCase accountDeactivationUseCase;

  public UserUpdateTransactionalAdapter(
      RequestContext requestContext,
      UserNameUpdateUseCase userNameUpdateUseCase,
      UserEmailUpdateUseCase userEmailUpdateUseCase,
      UserPasswordUpdateUseCase userPasswordUpdateUseCase,
      AccountDeactivationUseCase accountDeactivationUseCase) {

    this.requestContext = requestContext;
    this.userNameUpdateUseCase = userNameUpdateUseCase;
    this.userEmailUpdateUseCase = userEmailUpdateUseCase;
    this.userPasswordUpdateUseCase = userPasswordUpdateUseCase;
    this.accountDeactivationUseCase = accountDeactivationUseCase;
  }

  @Override
  @Transactional
  public void updateUserName(ChangeUserNameCommand command) {
    userNameUpdateUseCase.execute(command, requestContext);
  }

  @Override
  @Transactional
  public void updateUserEmail(ChangeUserEmailCommand command) {
    try {
      userEmailUpdateUseCase.execute(command, requestContext);

    } catch (DataIntegrityViolationException ex) {
      throw new IllegalUserEmailDuplicatedException("Duplicate email cannot update");
    }
  }

  @Override
  @Transactional
  public void updateUserPassword(ChangeUserPasswordCommand command) {
    userPasswordUpdateUseCase.execute(command, requestContext);
  }

  @Override
  @Transactional
  public void deactivateAccount(UserAccountDeactivationCommand command) {
    accountDeactivationUseCase.execute(command, requestContext);
  }
}
