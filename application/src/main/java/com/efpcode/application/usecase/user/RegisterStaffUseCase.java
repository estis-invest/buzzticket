package com.efpcode.application.usecase.user;

import com.efpcode.application.port.context.RequestContext;
import com.efpcode.application.port.security.PasswordHasher;
import com.efpcode.application.usecase.partner.exceptions.IllegalPartnerStatusException;
import com.efpcode.application.usecase.partner.exceptions.PartnerNotFoundException;
import com.efpcode.application.usecase.user.dto.RegisterStaffCommand;
import com.efpcode.application.usecase.user.exceptions.IllegalUserEmailDuplicatedException;
import com.efpcode.application.usecase.user.exceptions.IllegalUserNotFoundException;
import com.efpcode.application.usecase.user.exceptions.IllegalUserRoleException;
import com.efpcode.application.usecase.user.exceptions.IllegalUserStatusException;
import com.efpcode.domain.common.model.PlainPassword;
import com.efpcode.domain.common.port.IdGenerator;
import com.efpcode.domain.partner.model.Partner;
import com.efpcode.domain.partner.model.PartnerId;
import com.efpcode.domain.partner.port.PartnerRepository;
import com.efpcode.domain.user.model.*;
import com.efpcode.domain.user.port.UserRepository;

public class RegisterStaffUseCase {
  private final IdGenerator<UserId> userIdGenerator;
  private final UserRepository userRepository;
  private final PartnerRepository partnerRepository;
  private final PasswordHasher passwordHasher;

  public RegisterStaffUseCase(
      IdGenerator<UserId> userIdGenerator,
      UserRepository userRepository,
      PartnerRepository partnerRepository,
      PasswordHasher passwordHasher) {
    this.userIdGenerator = userIdGenerator;
    this.userRepository = userRepository;
    this.partnerRepository = partnerRepository;
    this.passwordHasher = passwordHasher;
  }

  public User execute(RequestContext requestContext, RegisterStaffCommand command) {

    requestContext.role().roleGuardIsAdmin();

    User adminHandler =
        userRepository
            .findUserById(requestContext.userId())
            .orElseThrow(() -> new IllegalUserNotFoundException("Request handler is not found"));

    if (adminHandler.role() != requestContext.role()) {
      throw new SecurityException("Token role mismatch");
    }

    adminHandler.role().roleGuardIsAdmin();
    if (!adminHandler.status().isActive()) {
      throw new IllegalUserStatusException("Request handler status other then activated");
    }

    PartnerId partnerId =
        adminHandler
            .partnerId()
            .orElseThrow(() -> new PartnerNotFoundException("Request handler has no partner"));

    Partner partner =
        partnerRepository
            .findById(partnerId)
            .orElseThrow(
                () -> new PartnerNotFoundException("Partner for request handler was not found"));

    if (!partner.status().isActive()) {
      throw new IllegalPartnerStatusException("Partner status is not active");
    }

    UserEmail userEmail = new UserEmail(command.email());

    if (userRepository.existsByEmail(userEmail)) {
      throw new IllegalUserEmailDuplicatedException(
          "Duplicated email is not allowed: " + userEmail.email());
    }

    PlainPassword password = new PlainPassword(command.password());
    UserPassword hashedPassword = passwordHasher.hash(password);
    UserId userId = userIdGenerator.generate();
    UserName userName = new UserName(command.name());
    UserRole role = UserRole.valueOf(command.role());

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
