package com.efpcode.application.usecase.user;

import com.efpcode.application.context.RequestContext;
import com.efpcode.application.policy.admin.AdminActionPolicy;
import com.efpcode.application.policy.admin.dto.AdminContext;
import com.efpcode.application.usecase.user.exceptions.InvitationForbiddenException;
import com.efpcode.application.usecase.user.exceptions.InvitationNotFoundException;
import com.efpcode.domain.staffinvitation.StaffInvitation;
import com.efpcode.domain.staffinvitation.StaffInvitationId;
import com.efpcode.domain.staffinvitation.port.StaffInvitationRepository;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;

public class CancelStaffInvitationUseCase {
  private final StaffInvitationRepository staffInvitationRepository;
  private final AdminActionPolicy adminActionPolicy;
  private final Clock clock;

  public CancelStaffInvitationUseCase(
      StaffInvitationRepository staffInvitationRepository,
      AdminActionPolicy adminActionPolicy,
      Clock clock) {
    this.staffInvitationRepository = staffInvitationRepository;
    this.adminActionPolicy = adminActionPolicy;
    this.clock = clock;
  }

  public void execute(StaffInvitationId id, RequestContext context) {
    AdminContext adminContext = adminActionPolicy.adminValidator(context);
    StaffInvitation invitation =
        staffInvitationRepository
            .findById(id)
            .orElseThrow(() -> new InvitationNotFoundException("No invite found"));

    if (!adminContext.partner().id().equals(invitation.partnerId())) {
      throw new InvitationForbiddenException("Cannot update expiration");
    }

    Instant now = Instant.now(clock);
    Instant effectiveExpiry = now.plus(Duration.ofMinutes(1));

    StaffInvitation setInviteToExpire = invitation.cancel(effectiveExpiry);

    staffInvitationRepository.save(setInviteToExpire);
  }
}
