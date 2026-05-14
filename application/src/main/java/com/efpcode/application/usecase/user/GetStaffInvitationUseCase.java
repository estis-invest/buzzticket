package com.efpcode.application.usecase.user;

import com.efpcode.application.context.RequestContext;
import com.efpcode.application.policy.admin.AdminActionPolicy;
import com.efpcode.application.policy.admin.dto.AdminContext;
import com.efpcode.application.usecase.user.dto.StaffInvitationQueryResult;
import com.efpcode.application.usecase.user.exceptions.InvitationForbiddenException;
import com.efpcode.application.usecase.user.exceptions.InvitationNotFoundException;
import com.efpcode.domain.staffinvitation.StaffInvitation;
import com.efpcode.domain.staffinvitation.StaffInvitationId;
import com.efpcode.domain.staffinvitation.port.StaffInvitationRepository;

public class GetStaffInvitationUseCase {
  private final StaffInvitationRepository staffInvitationRepository;
  private final AdminActionPolicy adminActionPolicy;

  public GetStaffInvitationUseCase(
      StaffInvitationRepository staffInvitationRepository, AdminActionPolicy adminActionPolicy) {
    this.staffInvitationRepository = staffInvitationRepository;
    this.adminActionPolicy = adminActionPolicy;
  }

  public StaffInvitationQueryResult execute(
      StaffInvitationId invitationId, RequestContext requestContext) {
    AdminContext adminContext = adminActionPolicy.adminValidator(requestContext);

    StaffInvitation invitation =
        staffInvitationRepository
            .findById(invitationId)
            .orElseThrow(() -> new InvitationNotFoundException("Invitation not found"));

    if (!adminContext.partner().id().equals(invitation.partnerId())) {
      throw new InvitationForbiddenException("Invitation no read permit");
    }

    return new StaffInvitationQueryResult(
        invitationId.invitationId(),
        invitation.inviteeEmail().email(),
        invitation.role().name(),
        invitation.invitationStatus().name(),
        invitation.invitationExpiresAt().time(),
        invitation.invitationUpdatedAt().time());
  }
}
