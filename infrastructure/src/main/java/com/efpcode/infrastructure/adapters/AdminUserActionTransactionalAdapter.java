package com.efpcode.infrastructure.adapters;

import com.efpcode.application.context.RequestContext;
import com.efpcode.application.port.in.user.AdminAccountCommands;
import com.efpcode.application.usecase.user.AdminAccountActivateUseCase;
import com.efpcode.application.usecase.user.AdminAccountDeactivationUseCase;
import com.efpcode.application.usecase.user.AdminAccountDemotionUseCase;
import com.efpcode.application.usecase.user.AdminAccountPromotionUseCase;
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
  private final AdminAccountActivateUseCase adminAccountActivateUseCase;
  private final AdminAccountPromotionUseCase adminAccountPromotionUseCase;
  private final AdminAccountDemotionUseCase adminAccountDemotionUseCase;

  public AdminUserActionTransactionalAdapter(
      RequestContext requestContext,
      AdminAccountDeactivationUseCase adminAccountDeactivationUseCase,
      AdminAccountActivateUseCase adminAccountActivateUseCase,
      AdminAccountPromotionUseCase adminAccountPromotionUseCase,
      AdminAccountDemotionUseCase adminAccountDemotionUseCase) {
    this.requestContext = requestContext;
    this.adminAccountDeactivationUseCase = adminAccountDeactivationUseCase;
    this.adminAccountActivateUseCase = adminAccountActivateUseCase;
    this.adminAccountPromotionUseCase = adminAccountPromotionUseCase;
    this.adminAccountDemotionUseCase = adminAccountDemotionUseCase;
  }

  @Override
  @Transactional
  public void activateAccount(ActivateUserCommand command) {
    adminAccountActivateUseCase.execute(command, requestContext);
  }

  @Override
  @Transactional
  public void deactivateAccount(DeactivateCommand command) {
    adminAccountDeactivationUseCase.execute(command, requestContext);
  }

  @Override
  @Transactional
  public void promoteUser(PromoteCommand command) {
    adminAccountPromotionUseCase.execute(command, requestContext);
  }

  @Override
  @Transactional
  public void demoteUser(DemoteCommand command) {
    adminAccountDemotionUseCase.execute(command, requestContext);
  }
}
