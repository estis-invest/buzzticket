package com.efpcode.application.port.in.user;

import com.efpcode.application.usecase.user.dto.RegisterStaffInvitationAccountCommand;
import com.efpcode.application.usecase.user.dto.StaffInvitationAcceptanceResult;

public interface StaffInvitationAcceptanceCommands {

  StaffInvitationAcceptanceResult acceptInvitation(RegisterStaffInvitationAccountCommand command);
}
