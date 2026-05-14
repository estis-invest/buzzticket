package com.efpcode.application.usecase.user;

import com.efpcode.application.context.RequestContext;
import com.efpcode.application.policy.admin.AdminActionPolicy;
import com.efpcode.application.policy.admin.dto.AdminContext;
import com.efpcode.domain.staffinvitation.port.StaffInvitationRepository;
import com.efpcode.domain.user.model.UserEmail;

public class HasStaffInvitationUseCase {
  private final StaffInvitationRepository staffInvitationRepository;
  private final AdminActionPolicy adminActionPolicy;

  public HasStaffInvitationUseCase(
      StaffInvitationRepository staffInvitationRepository, AdminActionPolicy adminActionPolicy) {

    this.staffInvitationRepository = staffInvitationRepository;
    this.adminActionPolicy = adminActionPolicy;
  }

  public boolean execute(String inviteeEmail, RequestContext requestContext) {
    AdminContext adminContext = adminActionPolicy.adminValidator(requestContext);
    UserEmail userEmail = new UserEmail(inviteeEmail);
    return staffInvitationRepository.existsPendingByEmailAndPartnerId(
        userEmail, adminContext.partner().id());
  }
}
