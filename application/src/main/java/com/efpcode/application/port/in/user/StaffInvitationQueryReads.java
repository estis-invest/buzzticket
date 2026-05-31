package com.efpcode.application.port.in.user;

import com.efpcode.application.usecase.user.dto.StaffInvitationQueryResult;
import com.efpcode.domain.staffinvitation.model.StaffInvitationId;
import com.efpcode.domain.staffinvitation.model.StaffInvitationStatus;
import java.util.List;

public interface StaffInvitationQueryReads {

  StaffInvitationQueryResult getStaffInvitation(StaffInvitationId invitationId);

  List<StaffInvitationQueryResult> getAllStaffInvitationByStatus(StaffInvitationStatus status);

  boolean hasPendingInvite(String inviteeEmail);
}
