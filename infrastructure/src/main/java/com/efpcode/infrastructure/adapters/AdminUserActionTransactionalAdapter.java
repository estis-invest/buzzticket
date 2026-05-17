package com.efpcode.infrastructure.adapters;

import com.efpcode.application.context.RequestContext;
import com.efpcode.application.port.in.user.AdminAccountCommands;
import com.efpcode.application.usecase.user.AdminAccountDeactivationUseCase;
import com.efpcode.application.usecase.user.dto.ActivateUserCommand;
import com.efpcode.application.usecase.user.dto.DeactivateCommand;
import com.efpcode.application.usecase.user.dto.DemoteCommand;
import com.efpcode.application.usecase.user.dto.PromoteCommand;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
class AdminUserActionTransactionalAdapter implements AdminAccountCommands {

  private final RequestContext requestContext;
  private final AdminAccountDeactivationUseCase adminAccountDeactivationUseCase;

  public AdminUserActionTransactionalAdapter(
      RequestContext requestContext,
      AdminAccountDeactivationUseCase adminAccountDeactivationUseCase) {
    this.requestContext = requestContext;
    this.adminAccountDeactivationUseCase = adminAccountDeactivationUseCase;
  }

  @Override
  @Transactional
  public void activateAccount(ActivateUserCommand command) {}

  @Override
  @Transactional
  public void deactivateAccount(DeactivateCommand command) {
    adminAccountDeactivationUseCase.execute(command, requestContext);
  }

  @Override
  @Transactional
  public void promoteUser(PromoteCommand command) {}

  @Override
  @Transactional
  public void demoteUser(DemoteCommand command) {}
}
