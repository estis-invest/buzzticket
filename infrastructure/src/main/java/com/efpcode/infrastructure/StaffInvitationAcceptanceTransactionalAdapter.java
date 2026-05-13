package com.efpcode.infrastructure;

import com.efpcode.application.port.in.user.StaffInvitationAcceptanceCommands;
import com.efpcode.application.usecase.user.AcceptStaffInvitationUseCase;
import com.efpcode.application.usecase.user.dto.RegisterStaffInvitationAccountCommand;
import com.efpcode.application.usecase.user.dto.StaffInvitationAcceptanceResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
class StaffInvitationAcceptanceTransactionalAdapter implements StaffInvitationAcceptanceCommands {
  private final AcceptStaffInvitationUseCase acceptStaffInvitationUseCase;

  public StaffInvitationAcceptanceTransactionalAdapter(
      AcceptStaffInvitationUseCase acceptStaffInvitationUseCase) {
    this.acceptStaffInvitationUseCase = acceptStaffInvitationUseCase;
  }

  @Override
  @Transactional
  public StaffInvitationAcceptanceResult acceptInvitation(
      RegisterStaffInvitationAccountCommand command) {
    return acceptStaffInvitationUseCase.execute(command);
  }
}
