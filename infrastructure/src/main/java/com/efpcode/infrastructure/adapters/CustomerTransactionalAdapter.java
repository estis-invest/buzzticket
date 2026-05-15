package com.efpcode.infrastructure.adapters;

import com.efpcode.application.port.in.user.CustomerRegistrationCommands;
import com.efpcode.application.usecase.user.RegisterCustomerUseCase;
import com.efpcode.application.usecase.user.dto.RegisterCustomerCommand;
import com.efpcode.domain.user.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
class CustomerTransactionalAdapter implements CustomerRegistrationCommands {
  private final RegisterCustomerUseCase registerCustomerUseCase;

  public CustomerTransactionalAdapter(RegisterCustomerUseCase registerCustomerUseCase) {
    this.registerCustomerUseCase = registerCustomerUseCase;
  }

  @Override
  @Transactional
  public User register(RegisterCustomerCommand command) {
    return registerCustomerUseCase.execute(command);
  }
}
