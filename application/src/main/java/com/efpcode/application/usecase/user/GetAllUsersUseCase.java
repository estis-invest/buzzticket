package com.efpcode.application.usecase.user;

import com.efpcode.application.context.RequestContext;
import com.efpcode.application.policy.admin.AdminActionPolicy;
import com.efpcode.application.policy.admin.dto.AdminContext;
import com.efpcode.application.usecase.user.dto.UserResult;
import com.efpcode.domain.user.model.User;
import com.efpcode.domain.user.port.UserRepository;
import java.util.List;

public class GetAllUsersUseCase {
  private final UserRepository userRepository;
  private final AdminActionPolicy adminActionPolicy;

  public GetAllUsersUseCase(UserRepository userRepository, AdminActionPolicy adminActionPolicy) {
    this.userRepository = userRepository;
    this.adminActionPolicy = adminActionPolicy;
  }

  public List<UserResult> execute(RequestContext requestContext) {

    AdminContext adminContext = adminActionPolicy.adminValidator(requestContext);

    List<User> users =
        userRepository.findAllCustomersAndStaffByPartnerId(adminContext.partner().id());

    return users.stream().map(UserResult::fromDomain).toList();
  }
}
