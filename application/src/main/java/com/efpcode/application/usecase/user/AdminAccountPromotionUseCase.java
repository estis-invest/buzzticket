package com.efpcode.application.usecase.user;

import com.efpcode.application.context.RequestContext;
import com.efpcode.application.policy.admin.AdminActionPolicy;
import com.efpcode.application.policy.admin.dto.AdminContext;
import com.efpcode.application.usecase.shared.UserResolver;
import com.efpcode.application.usecase.user.dto.PromoteCommand;
import com.efpcode.application.usecase.user.exceptions.IllegalUserRoleException;
import com.efpcode.domain.user.model.User;
import com.efpcode.domain.user.model.UserRole;
import com.efpcode.domain.user.port.UserRepository;

public class AdminAccountPromotionUseCase {
  private final UserRepository userRepository;
  private final AdminActionPolicy adminActionPolicy;

  public AdminAccountPromotionUseCase(
      UserRepository userRepository, AdminActionPolicy adminActionPolicy) {
    this.userRepository = userRepository;
    this.adminActionPolicy = adminActionPolicy;
  }

  public void execute(PromoteCommand command, RequestContext requestContext) {

    AdminContext adminContext = adminActionPolicy.adminValidator(requestContext);
    User targetUser = UserResolver.resolveRequired(userRepository, command.uuid());

    adminActionPolicy.assertThatTargetIsActive(targetUser);

    if (targetUser.role() != UserRole.SUPPORT) {
      throw new IllegalUserRoleException("Cannot promote user, wrong role");
    }

    adminActionPolicy.assertSamePartnerIfStaff(adminContext, targetUser);

    User adminUser = targetUser.promoteToAdmin();

    userRepository.save(adminUser);
  }
}
