package com.efpcode.application.usecase.user;

import com.efpcode.application.port.out.security.PasswordHasher;
import com.efpcode.application.port.out.security.StaffInvitationTokenHasher;
import com.efpcode.application.usecase.user.dto.RegisterStaffInvitationAccountCommand;
import com.efpcode.application.usecase.user.dto.StaffInvitationAcceptanceResult;
import com.efpcode.application.usecase.user.exceptions.*;
import com.efpcode.domain.common.model.PlainPassword;
import com.efpcode.domain.common.model.PlainStaffInvitationToken;
import com.efpcode.domain.common.port.IdGenerator;
import com.efpcode.domain.staffinvitation.StaffInvitation;
import com.efpcode.domain.staffinvitation.StaffInvitationTokenHash;
import com.efpcode.domain.staffinvitation.port.StaffInvitationRepository;
import com.efpcode.domain.user.model.*;
import com.efpcode.domain.user.port.UserRepository;
import java.time.Clock;
import java.time.Instant;

public class AcceptStaffInvitationUseCase {
  private final IdGenerator<UserId> userIdGenerator;
  private final UserRepository userRepository;
  private final StaffInvitationRepository staffInvitationRepository;
  private final StaffInvitationTokenHasher tokenHasher;
  private final Clock clock;
  private final PasswordHasher passwordHasher;

  public AcceptStaffInvitationUseCase(
      IdGenerator<UserId> userIdGenerator,
      UserRepository userRepository,
      StaffInvitationRepository staffInvitationRepository,
      StaffInvitationTokenHasher tokenHasher,
      Clock clock,
      PasswordHasher passwordHasher) {
    this.userIdGenerator = userIdGenerator;
    this.userRepository = userRepository;
    this.staffInvitationRepository = staffInvitationRepository;
    this.tokenHasher = tokenHasher;
    this.clock = clock;
    this.passwordHasher = passwordHasher;
  }

  public StaffInvitationAcceptanceResult execute(RegisterStaffInvitationAccountCommand command) {

    PlainStaffInvitationToken rawToken = new PlainStaffInvitationToken(command.rawToken());
    PlainPassword rawPassword = new PlainPassword(command.password());
    UserName userName = new UserName(command.name());
    StaffInvitationTokenHash hashedToken = tokenHasher.hash(rawToken);
    Instant now = Instant.now(clock);
    UserId userId = userIdGenerator.generate();
    UserPassword hashedPassword = passwordHasher.hash(rawPassword);

    StaffInvitation invitation =
        staffInvitationRepository
            .findPendingByTokenHash(hashedToken)
            .orElseThrow(() -> new InvitationNotFoundException("User has no invitation"));

    if (!tokenHasher.matches(rawToken, invitation.invitationTokenHash())) {
      throw new InvalidStaffInvitationTokenException("User has invalid token");
    }

    if (invitation.invitationExpiresAt().time().isBefore(now)) {
      var invitationUpdated = invitation.expire(now);
      staffInvitationRepository.save(invitationUpdated);
      throw new StaffInvitationDateException("Invitation is expired");
    }

    if (userRepository.existsByEmail(invitation.inviteeEmail())) {
      throw new IllegalUserEmailDuplicatedException("Duplicated email is not allowed");
    }

    User user =
        switch (invitation.role()) {
          case ADMIN ->
              UserFactory.createAdminUserWithPartner(
                  userId,
                  userName,
                  invitation.inviteeEmail(),
                  hashedPassword,
                  invitation.partnerId());
          case SUPPORT ->
              UserFactory.createSupportUserWithPartner(
                  userId,
                  userName,
                  invitation.inviteeEmail(),
                  hashedPassword,
                  invitation.partnerId());

          default -> throw new IllegalUserRoleException("Role assigned not valid");
        };

    userRepository.save(user);
    var acceptedInvitation = invitation.accept(now, userId);
    staffInvitationRepository.save(acceptedInvitation);

    return new StaffInvitationAcceptanceResult(
        userId.id(), userName.name(), invitation.role().name());
  }
}
