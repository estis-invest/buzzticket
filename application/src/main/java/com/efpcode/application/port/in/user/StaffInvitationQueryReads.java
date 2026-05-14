package com.efpcode.application.port.in.user;

import com.efpcode.application.usecase.user.dto.StaffInvitationQueryResult;
import com.efpcode.domain.staffinvitation.StaffInvitationId;
import com.efpcode.domain.staffinvitation.StaffInvitationStatus;
import java.util.List;

public interface StaffInvitationQueryReads {

  StaffInvitationQueryResult getStaffInvitation(StaffInvitationId invitationId);

  List<StaffInvitationQueryResult> getAllStaffInvitationByStatus(StaffInvitationStatus status);
}
