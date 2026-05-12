package com.efpcode.infrastructure.adapters;

import com.efpcode.application.port.in.user.StaffRegistrationCommands;
import com.efpcode.application.usecase.user.CreateStaffInvitationUseCase;
import com.efpcode.application.usecase.user.RegisterStaffUseCase;
import com.efpcode.application.usecase.user.dto.CreateStaffInvitationResult;
import com.efpcode.application.usecase.user.dto.RegisterStaffCommand;
import com.efpcode.application.usecase.user.dto.RegisterStaffInvitationCommand;
import com.efpcode.domain.user.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserTransactionalAdapter implements StaffRegistrationCommands {
  private final RegisterStaffUseCase registerStaffUseCase;
  private final CreateStaffInvitationUseCase createStaffInvitationUseCase;

  public UserTransactionalAdapter(
      RegisterStaffUseCase registerStaffUseCase,
      CreateStaffInvitationUseCase createStaffInvitationUseCase) {
    this.registerStaffUseCase = registerStaffUseCase;
    this.createStaffInvitationUseCase = createStaffInvitationUseCase;
  }

  @Override
  @Transactional
  public User register(RegisterStaffCommand command) {
    return registerStaffUseCase.execute(command);
  }

  @Override
  @Transactional
  public CreateStaffInvitationResult sendInvitation(RegisterStaffInvitationCommand command) {
    return createStaffInvitationUseCase.execute(command);
  }
}
