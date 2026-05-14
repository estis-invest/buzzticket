package com.efpcode.application.port.in.user;

import com.efpcode.application.usecase.user.dto.StaffInvitationQueryResult;
import com.efpcode.domain.staffinvitation.StaffInvitationId;

public interface StaffInvitationQueryCommands {

  StaffInvitationQueryResult getStaffInvitation(StaffInvitationId invitationId);
}
