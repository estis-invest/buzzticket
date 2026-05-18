package com.efpcode.application.usecase.user;

import com.efpcode.application.context.RequestContext;
import com.efpcode.application.policy.admin.AdminActionPolicy;
import com.efpcode.application.policy.admin.dto.AdminContext;
import com.efpcode.application.usecase.shared.UserResolver;
import com.efpcode.application.usecase.user.dto.ActivateUserCommand;
import com.efpcode.application.usecase.user.exceptions.IllegalUserStatusException;
import com.efpcode.domain.user.model.User;
import com.efpcode.domain.user.port.UserRepository;

public class AdminAccountActivateUseCase {
  private final UserRepository userRepository;
  private final AdminActionPolicy adminActionPolicy;

  public AdminAccountActivateUseCase(
      UserRepository userRepository, AdminActionPolicy adminActionPolicy) {
    this.userRepository = userRepository;
    this.adminActionPolicy = adminActionPolicy;
  }

  public void execute(ActivateUserCommand command, RequestContext requestContext) {

    AdminContext adminContext = adminActionPolicy.adminValidator(requestContext);

    User targetUser = UserResolver.resolveRequired(userRepository, command.uuid());

    if (targetUser.isActive()) {
      throw new IllegalUserStatusException("User is already activated");
    }

    adminActionPolicy.assertSamePartnerIfStaff(adminContext, targetUser);

    User activatedUser = targetUser.activate();

    userRepository.save(activatedUser);
  }
}
