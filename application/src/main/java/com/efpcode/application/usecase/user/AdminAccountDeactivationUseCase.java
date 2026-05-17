package com.efpcode.application.usecase.user;

import com.efpcode.application.context.RequestContext;
import com.efpcode.application.policy.admin.AdminActionPolicy;
import com.efpcode.application.policy.admin.dto.AdminContext;
import com.efpcode.application.policy.partner.PartnerAdminInvariantPolicy;
import com.efpcode.application.usecase.user.dto.DeactivateCommand;
import com.efpcode.application.usecase.user.exceptions.IllegalUserNotFoundException;
import com.efpcode.domain.user.model.User;
import com.efpcode.domain.user.model.UserId;
import com.efpcode.domain.user.model.UserRole;
import com.efpcode.domain.user.port.UserRepository;

public class AdminAccountDeactivationUseCase {
  private final UserRepository userRepository;
  private final AdminActionPolicy adminActionPolicy;
  private final PartnerAdminInvariantPolicy partnerAdminInvariantPolicy;

  public AdminAccountDeactivationUseCase(
      UserRepository userRepository,
      AdminActionPolicy adminActionPolicy,
      PartnerAdminInvariantPolicy partnerAdminInvariantPolicy) {
    this.userRepository = userRepository;
    this.adminActionPolicy = adminActionPolicy;
    this.partnerAdminInvariantPolicy = partnerAdminInvariantPolicy;
  }

  public void execute(DeactivateCommand command, RequestContext requestContext) {
    AdminContext adminContext = adminActionPolicy.adminValidator(requestContext);

    if (command.uuid() == null) {
      throw new IllegalUserNotFoundException("Required value is missing");
    }

    UserId targetUserId = UserId.of(command.uuid());

    User targetUser =
        userRepository
            .findUserById(targetUserId)
            .orElseThrow(() -> new IllegalUserNotFoundException("User not found"));

    adminActionPolicy.assertThatTargetIsActive(targetUser);
    adminActionPolicy.assertSamePartnerIfStaff(adminContext, targetUser);

    if (targetUser.role() == UserRole.ADMIN) {
      partnerAdminInvariantPolicy.ensureCanRemoveAdmin(adminContext.partner().id());
    }

    User deactivatedUser = targetUser.deactivate();

    userRepository.save(deactivatedUser);
  }
}
