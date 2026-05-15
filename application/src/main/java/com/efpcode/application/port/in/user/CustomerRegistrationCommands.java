package com.efpcode.application.port.in.user;

import com.efpcode.application.usecase.user.dto.RegisterCustomerCommand;
import com.efpcode.domain.user.model.User;

public interface CustomerRegistrationCommands {
  User register(RegisterCustomerCommand command);
}
