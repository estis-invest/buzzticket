package com.efpcode.application.usecase.user;

import com.efpcode.application.port.security.PasswordHasher;
import com.efpcode.application.usecase.partner.exceptions.PartnerNotFoundException;
import com.efpcode.application.usecase.user.dto.RegisterUserWithPartnerCommand;
import com.efpcode.application.usecase.user.exceptions.IllegalUserEmailDuplicatedException;
import com.efpcode.domain.common.model.PlainPassword;
import com.efpcode.domain.common.port.IdGenerator;
import com.efpcode.domain.partner.model.PartnerId;
import com.efpcode.domain.partner.port.PartnerRepository;
import com.efpcode.domain.user.model.*;
import com.efpcode.domain.user.port.UserRepository;

public class RegisterAdminUserUseCase {
  private final UserRepository userRepository;
  private final PartnerRepository partnerRepository;
  private final IdGenerator<UserId> userIdIdGenerator;
  private final PasswordHasher passwordHasher;

  public RegisterAdminUserUseCase(
      UserRepository userRepository,
      PartnerRepository partnerRepository,
      IdGenerator<UserId> userIdIdGenerator,
      PasswordHasher passwordHasher) {
    this.userRepository = userRepository;
    this.partnerRepository = partnerRepository;
    this.userIdIdGenerator = userIdIdGenerator;
    this.passwordHasher = passwordHasher;
  }

  public User execute(RegisterUserWithPartnerCommand command) {

    PartnerId partnerId = PartnerId.fromString(command.userPartner());
    UserEmail email = new UserEmail(command.userEmail());

    boolean isFoundPartner = partnerRepository.existsById(partnerId);
    boolean emailExists = userRepository.existsByEmail(email);

    if (!isFoundPartner) {
      throw new PartnerNotFoundException("Partner is not found");
    }

    if (emailExists) {
      throw new IllegalUserEmailDuplicatedException("Email is not unique");
    }

    PlainPassword plainPassword = new PlainPassword(command.userPassword());
    UserPassword hashedPassword = passwordHasher.hash(plainPassword);
    UserId id = userIdIdGenerator.generate();
    UserName name = new UserName(command.userName());

    User newUser =
        UserFactory.createAdminUserWithPartner(id, name, email, hashedPassword, partnerId);

    userRepository.save(newUser);

    return newUser;
  }
}
