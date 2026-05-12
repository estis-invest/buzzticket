package com.efpcode.application.usecase.user;

import com.efpcode.application.context.RequestContext;
import com.efpcode.application.policy.StaffInvitationTimeToLivePolicy;
import com.efpcode.application.policy.admin.AdminActionPolicy;
import com.efpcode.application.port.out.security.StaffInvitationTokenGenerator;
import com.efpcode.application.port.out.security.StaffInvitationTokenHasher;
import com.efpcode.application.usecase.user.dto.CreateStaffInvitationResult;
import com.efpcode.application.usecase.user.dto.RegisterStaffInvitationCommand;
import com.efpcode.application.usecase.user.exceptions.IllegalStaffInvitationExpirationDateArgumentException;
import com.efpcode.application.usecase.user.exceptions.IllegalUserRoleException;
import com.efpcode.domain.common.model.PlainStaffInvitationToken;
import com.efpcode.domain.common.port.IdGenerator;
import com.efpcode.domain.partner.model.PartnerId;
import com.efpcode.domain.staffinvitation.StaffInvitation;
import com.efpcode.domain.staffinvitation.StaffInvitationId;
import com.efpcode.domain.staffinvitation.StaffInvitationTokenHash;
import com.efpcode.domain.staffinvitation.port.StaffInvitationRepository;
import com.efpcode.domain.user.model.UserEmail;
import com.efpcode.domain.user.model.UserId;
import com.efpcode.domain.user.model.UserRole;
import java.time.Clock;
import java.time.Instant;

public class CreateStaffInvitationUseCase {
  private final IdGenerator<StaffInvitationId> staffInvitationIdGenerator;
  private final StaffInvitationRepository staffInvitationRepository;
  private final AdminActionPolicy adminActionPolicy;
  private final StaffInvitationTokenHasher tokenHasher;
  private final Clock clock;
  private final StaffInvitationTokenGenerator tokenGenerator;
  private final StaffInvitationTimeToLivePolicy timeToLivePolicy;

  public CreateStaffInvitationUseCase(
      IdGenerator<StaffInvitationId> staffInvitationIdGenerator,
      StaffInvitationRepository staffInvitationRepository,
      AdminActionPolicy adminActionPolicy,
      StaffInvitationTokenHasher tokenHasher,
      Clock clock,
      StaffInvitationTokenGenerator tokenGenerator,
      StaffInvitationTimeToLivePolicy timeToLivePolicy) {
    this.staffInvitationIdGenerator = staffInvitationIdGenerator;
    this.staffInvitationRepository = staffInvitationRepository;
    this.adminActionPolicy = adminActionPolicy;
    this.tokenHasher = tokenHasher;
    this.clock = clock;
    this.tokenGenerator = tokenGenerator;
    this.timeToLivePolicy = timeToLivePolicy;
  }

  public CreateStaffInvitationResult execute(RegisterStaffInvitationCommand command, RequestContext requestContext) {
    var adminContext = adminActionPolicy.adminValidator(requestContext);

    Instant now = Instant.now(clock);

    Instant maxAllowedTime = now.plus(timeToLivePolicy.timeToLive());

    if (command.expiresAt().isAfter(maxAllowedTime)) {
      throw new IllegalStaffInvitationExpirationDateArgumentException(
          "Staff invitation expiration must not exceed "+ maxAllowedTime);
    }
    UserRole role;

    try {
      role = UserRole.valueOf(command.role());

    } catch (IllegalArgumentException e) {
      throw new IllegalUserRoleException("Role assigned not valid: " + command.role());
    }

    role.roleGuardIsStaff();

    UserEmail inviteeEmail = new UserEmail(command.inviteeEmail());
    StaffInvitationId invitationId = staffInvitationIdGenerator.generate();
    UserId invitedByUserId = adminContext.admin().id();
    PartnerId partnerId = adminContext.partner().id();
    PlainStaffInvitationToken rawToken = tokenGenerator.generate();
    StaffInvitationTokenHash hashedToken = tokenHasher.hash(rawToken);

    StaffInvitation invitation =
        StaffInvitation.create(
            invitationId,
            invitedByUserId,
            inviteeEmail,
            role,
            partnerId,
            hashedToken,
            now,
            command.expiresAt());

    staffInvitationRepository.save(invitation);

    return new CreateStaffInvitationResult(
        invitationId.invitationId(),
        inviteeEmail.email(),
        invitation.role().name(),
        invitation.invitationStatus().name(),
        invitation.invitationExpiresAt().time(),
        rawToken.plainToken());
  }
}
