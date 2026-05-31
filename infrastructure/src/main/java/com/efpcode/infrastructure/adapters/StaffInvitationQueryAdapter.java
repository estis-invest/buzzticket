package com.efpcode.infrastructure.adapters;

import com.efpcode.application.context.RequestContext;
import com.efpcode.application.port.in.user.StaffInvitationQueryReads;
import com.efpcode.application.usecase.user.GetAllStaffInvitationByStatusUseCase;
import com.efpcode.application.usecase.user.GetStaffInvitationUseCase;
import com.efpcode.application.usecase.user.HasStaffInvitationUseCase;
import com.efpcode.application.usecase.user.dto.StaffInvitationQueryResult;
import com.efpcode.domain.staffinvitation.model.StaffInvitationId;
import com.efpcode.domain.staffinvitation.model.StaffInvitationStatus;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class StaffInvitationQueryAdapter implements StaffInvitationQueryReads {
  private final RequestContext request;
  private final GetStaffInvitationUseCase getStaffInvitationUseCase;
  private final GetAllStaffInvitationByStatusUseCase getAllStaffInvitationByStatusUseCase;
  private final HasStaffInvitationUseCase hasStaffInvitationUseCase;

  public StaffInvitationQueryAdapter(
      GetStaffInvitationUseCase getStaffInvitationUseCase,
      GetAllStaffInvitationByStatusUseCase getAllStaffInvitationByStatusUseCase,
      HasStaffInvitationUseCase hasStaffInvitationUseCase,
      RequestContext request) {
    this.getStaffInvitationUseCase = getStaffInvitationUseCase;
    this.getAllStaffInvitationByStatusUseCase = getAllStaffInvitationByStatusUseCase;
    this.hasStaffInvitationUseCase = hasStaffInvitationUseCase;
    this.request = request;
  }

  @Override
  public StaffInvitationQueryResult getStaffInvitation(StaffInvitationId invitationId) {
    return getStaffInvitationUseCase.execute(invitationId, request);
  }

  @Override
  public List<StaffInvitationQueryResult> getAllStaffInvitationByStatus(
      StaffInvitationStatus status) {
    return getAllStaffInvitationByStatusUseCase.execute(status, request);
  }

  @Override
  public boolean hasPendingInvite(String inviteeEmail) {
    return hasStaffInvitationUseCase.execute(inviteeEmail, request);
  }
}
