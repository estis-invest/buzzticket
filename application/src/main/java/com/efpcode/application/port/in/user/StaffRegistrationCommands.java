package com.efpcode.application.port.in.user;

import com.efpcode.application.usecase.user.dto.*;
import com.efpcode.domain.user.model.User;

public interface StaffRegistrationCommands {
  User register(RegisterStaffCommand command);

  CreateStaffInvitationResult sendInvitation(RegisterStaffInvitationCommand command);
}
