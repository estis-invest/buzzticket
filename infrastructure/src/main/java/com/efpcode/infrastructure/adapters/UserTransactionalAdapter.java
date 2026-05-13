package com.efpcode.infrastructure.adapters;

import com.efpcode.application.context.RequestContext;
import com.efpcode.application.port.in.user.StaffRegistrationCommands;
import com.efpcode.application.usecase.user.CreateStaffInvitationUseCase;
import com.efpcode.application.usecase.user.RegisterStaffUseCase;
import com.efpcode.application.usecase.user.dto.*;
import com.efpcode.domain.user.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserTransactionalAdapter implements StaffRegistrationCommands {
  private final RegisterStaffUseCase registerStaffUseCase;
  private final CreateStaffInvitationUseCase createStaffInvitationUseCase;
  private final RequestContext requestContext;

  public UserTransactionalAdapter(
      RegisterStaffUseCase registerStaffUseCase,
      CreateStaffInvitationUseCase createStaffInvitationUseCase,
      RequestContext requestContext) {
    this.registerStaffUseCase = registerStaffUseCase;
    this.createStaffInvitationUseCase = createStaffInvitationUseCase;
    this.requestContext = requestContext;
  }

  @Override
  @Transactional
  public User register(RegisterStaffCommand command) {
    return registerStaffUseCase.execute(command, requestContext);
  }

  @Override
  @Transactional
  public CreateStaffInvitationResult sendInvitation(RegisterStaffInvitationCommand command) {
    return createStaffInvitationUseCase.execute(command, requestContext);
  }
}
