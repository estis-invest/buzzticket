package com.efpcode.infrastructure.adapters;

import com.efpcode.application.context.RequestContext;
import com.efpcode.application.port.in.user.StaffRegistrationCommands;
import com.efpcode.application.usecase.user.RegisterStaffUseCase;
import com.efpcode.application.usecase.user.dto.RegisterStaffCommand;
import com.efpcode.domain.user.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserTransactionalAdapter implements StaffRegistrationCommands {
  private final RegisterStaffUseCase registerStaffUseCase;

  public UserTransactionalAdapter(RegisterStaffUseCase registerStaffUseCase) {
    this.registerStaffUseCase = registerStaffUseCase;
  }

  @Override
  @Transactional
  public User register(RequestContext request, RegisterStaffCommand command) {
    return registerStaffUseCase.execute(request, command);
  }
}
