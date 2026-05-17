package com.efpcode.application.usecase.user;

import com.efpcode.application.context.RequestContext;
import com.efpcode.application.policy.admin.AdminActionPolicy;
import com.efpcode.application.policy.admin.dto.AdminContext;
import com.efpcode.application.usecase.user.dto.UserResult;
import com.efpcode.application.usecase.user.exceptions.IllegalUserNotFoundException;
import com.efpcode.domain.user.model.User;
import com.efpcode.domain.user.model.UserId;
import com.efpcode.domain.user.port.UserRepository;

public class GetUserUseCase {
  private final UserRepository userRepository;
  private final AdminActionPolicy adminActionPolicy;

  public GetUserUseCase(UserRepository userRepository, AdminActionPolicy adminActionPolicy) {
    this.userRepository = userRepository;
    this.adminActionPolicy = adminActionPolicy;
  }

  public UserResult execute(RequestContext requestContext, UserId id) {
    AdminContext adminContext = adminActionPolicy.adminValidator(requestContext);

    User user =
        userRepository
            .findUserById(id)
            .orElseThrow(() -> new IllegalUserNotFoundException("User not found"));
    adminActionPolicy.assertSamePartnerIfStaff(adminContext, user);

    return UserResult.fromDomain(user);
  }
}
