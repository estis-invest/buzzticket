package com.efpcode.application.usecase.user;

import com.efpcode.application.context.RequestContext;
import com.efpcode.application.policy.admin.AdminActionPolicy;
import com.efpcode.application.policy.admin.dto.AdminContext;
import com.efpcode.application.usecase.user.dto.StaffInvitationQueryResult;
import com.efpcode.domain.staffinvitation.StaffInvitation;
import com.efpcode.domain.staffinvitation.StaffInvitationStatus;
import com.efpcode.domain.staffinvitation.port.StaffInvitationRepository;
import java.util.List;

public class GetAllStaffInvitationByStatusUseCase {
  private final StaffInvitationRepository staffInvitationRepository;
  private final AdminActionPolicy adminActionPolicy;

  public GetAllStaffInvitationByStatusUseCase(
      StaffInvitationRepository staffInvitationRepository, AdminActionPolicy adminActionPolicy) {
    this.staffInvitationRepository = staffInvitationRepository;
    this.adminActionPolicy = adminActionPolicy;
  }

  public List<StaffInvitationQueryResult> execute(
      StaffInvitationStatus status, RequestContext requestContext) {

    AdminContext adminContext = adminActionPolicy.adminValidator(requestContext);

    List<StaffInvitation> invitations =
        staffInvitationRepository.findByPartnerIdAndStatus(adminContext.partner().id(), status);

    return invitations.stream()
        .map(
            invitation ->
                new StaffInvitationQueryResult(
                    invitation.invitationId().invitationId(),
                    invitation.inviteeEmail().email(),
                    invitation.role().name(),
                    invitation.invitationStatus().name(),
                    invitation.invitationExpiresAt().time(),
                    invitation.invitationUpdatedAt().time()))
        .toList();
  }
}
