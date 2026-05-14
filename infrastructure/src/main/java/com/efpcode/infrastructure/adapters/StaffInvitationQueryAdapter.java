package com.efpcode.infrastructure.adapters;

import com.efpcode.application.context.RequestContext;
import com.efpcode.application.port.in.user.StaffInvitationQueryCommands;
import com.efpcode.application.usecase.user.GetStaffInvitationUseCase;
import com.efpcode.application.usecase.user.dto.StaffInvitationQueryResult;
import com.efpcode.domain.staffinvitation.StaffInvitationId;
import org.springframework.stereotype.Service;

@Service
public class StaffInvitationQueryAdapter implements StaffInvitationQueryCommands {
  private final RequestContext request;
  private final GetStaffInvitationUseCase getStaffInvitationUseCase;

  public StaffInvitationQueryAdapter(
      GetStaffInvitationUseCase getStaffInvitationUseCase, RequestContext request) {
    this.getStaffInvitationUseCase = getStaffInvitationUseCase;
    this.request = request;
  }

  @Override
  public StaffInvitationQueryResult getStaffInvitation(StaffInvitationId invitationId) {
    return getStaffInvitationUseCase.execute(invitationId, request);
  }
}
