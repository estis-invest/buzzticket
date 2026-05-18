package com.efpcode.application.port.in.user;

import com.efpcode.application.usecase.user.dto.ActivateUserCommand;
import com.efpcode.application.usecase.user.dto.DeactivateCommand;
import com.efpcode.application.usecase.user.dto.DemoteCommand;
import com.efpcode.application.usecase.user.dto.PromoteCommand;

public interface AdminAccountCommands {
  void activateAccount(ActivateUserCommand command);

  void deactivateAccount(DeactivateCommand command);

  void promoteUser(PromoteCommand command);

  void demoteUser(DemoteCommand command);
}
