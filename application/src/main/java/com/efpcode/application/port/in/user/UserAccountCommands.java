package com.efpcode.application.port.in.user;

import com.efpcode.application.usecase.user.dto.ChangeUserEmailCommand;
import com.efpcode.application.usecase.user.dto.ChangeUserNameCommand;
import com.efpcode.application.usecase.user.dto.ChangeUserPasswordCommand;
import com.efpcode.application.usecase.user.dto.UserAccountDeactivationCommand;

public interface UserAccountCommands {
  void updateUserName(ChangeUserNameCommand command);

  void updateUserEmail(ChangeUserEmailCommand command);

  void updateUserPassword(ChangeUserPasswordCommand command);

  void deactivateAccount(UserAccountDeactivationCommand command);
}
