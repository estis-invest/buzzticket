package com.efpcode.application.usecase.user;

import com.efpcode.application.context.RequestContext;
import com.efpcode.application.policy.admin.AdminActionPolicy;
import com.efpcode.application.policy.admin.dto.AdminContext;
import com.efpcode.application.port.out.security.PasswordHasher;
import com.efpcode.application.usecase.user.dto.RegisterStaffCommand;
import com.efpcode.application.usecase.user.exceptions.IllegalUserEmailDuplicatedException;
import com.efpcode.application.usecase.user.exceptions.IllegalUserRoleException;
import com.efpcode.domain.common.model.PlainPassword;
import com.efpcode.domain.common.port.IdGenerator;
import com.efpcode.domain.partner.model.PartnerId;
import com.efpcode.domain.user.model.*;
import com.efpcode.domain.user.port.UserRepository;

public class RegisterStaffUseCase {
  private final IdGenerator<UserId> userIdGenerator;
  private final UserRepository userRepository;
  private final AdminActionPolicy adminActionPolicy;
  private final PasswordHasher passwordHasher;

  public RegisterStaffUseCase(
      IdGenerator<UserId> userIdGenerator,
      UserRepository userRepository,
      AdminActionPolicy adminActionPolicy,
      PasswordHasher passwordHasher) {
    this.userIdGenerator = userIdGenerator;
    this.userRepository = userRepository;
    this.adminActionPolicy = adminActionPolicy;
    this.passwordHasher = passwordHasher;
  }

  public User execute(RequestContext requestContext, RegisterStaffCommand command) {

    AdminContext adminContext = adminActionPolicy.adminValidator(requestContext);
    PartnerId partnerId = adminContext.partner().id();
    UserEmail userEmail = new UserEmail(command.email());

    if (userRepository.existsByEmail(userEmail)) {
      throw new IllegalUserEmailDuplicatedException("Duplicated email is not allowed");
    }

    PlainPassword password = new PlainPassword(command.password());
    UserPassword hashedPassword = passwordHasher.hash(password);
    UserId userId = userIdGenerator.generate();
    UserName userName = new UserName(command.name());
    UserRole role;

    try {
      role = UserRole.valueOf(command.role());

    } catch (IllegalArgumentException e) {
      throw new IllegalUserRoleException("Role assigned not valid: " + command.role());
    }

    User user =
        switch (role) {
          case UserRole.ADMIN ->
              UserFactory.createAdminUserWithPartner(
                  userId, userName, userEmail, hashedPassword, partnerId);
          case UserRole.SUPPORT ->
              UserFactory.createSupportUserWithPartner(
                  userId, userName, userEmail, hashedPassword, partnerId);
          default -> throw new IllegalUserRoleException("Role assigned not valid");
        };

    userRepository.save(user);

    return user;
  }
}
