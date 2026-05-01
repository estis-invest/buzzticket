package com.efpcode.application.usecase.company;

import com.efpcode.application.port.security.PasswordHasher;
import com.efpcode.application.usecase.company.dto.CompanyCommand;
import com.efpcode.application.usecase.company.dto.CompanyResult;
import com.efpcode.application.usecase.partner.exceptions.PartnerAlreadyExistsException;
import com.efpcode.domain.common.model.PlainPassword;
import com.efpcode.domain.common.port.IdGenerator;
import com.efpcode.domain.partner.model.Partner;
import com.efpcode.domain.partner.model.PartnerId;
import com.efpcode.domain.partner.model.PartnerName;
import com.efpcode.domain.partner.port.PartnerRepository;
import com.efpcode.domain.user.exceptions.UserEmailArgumentDuplicationException;
import com.efpcode.domain.user.model.*;
import com.efpcode.domain.user.port.UserRepository;

public class RegisterCompanyUseCase {

  private final IdGenerator<PartnerId> idPartnerGenerator;
  private final IdGenerator<UserId> idUserGenerator;
  private final PartnerRepository partnerRepository;
  private final UserRepository userRepository;
  private final PasswordHasher passwordHasher;

  public RegisterCompanyUseCase(
      IdGenerator<PartnerId> idPartnerGenerator,
      IdGenerator<UserId> idUserGenerator,
      PartnerRepository partnerRepository,
      UserRepository userRepository,
      PasswordHasher passwordHasher) {
    this.idPartnerGenerator = idPartnerGenerator;
    this.idUserGenerator = idUserGenerator;
    this.partnerRepository = partnerRepository;
    this.userRepository = userRepository;
    this.passwordHasher = passwordHasher;
  }

  public CompanyResult execute(CompanyCommand command) {

    PartnerName partnerName = new PartnerName(command.name());
    UserEmail adminEmail = new UserEmail(command.adminEmail());

    if (partnerRepository.existsByName(partnerName)) {
      throw new PartnerAlreadyExistsException(
          String.format(
              "Partner is already registered with that name: %s", partnerName.partnerName()));
    }

    if (userRepository.existsByEmail(adminEmail)) {
      throw new UserEmailArgumentDuplicationException("Email is not unique");
    }

    PartnerId partnerId = idPartnerGenerator.generate();
    UserId userId = idUserGenerator.generate();

    Partner draftPartner =
        Partner.createDraftPartner(
            partnerId,
            partnerName.partnerName(),
            command.city(),
            command.country(),
            command.isoCode());

    Partner newPartner = draftPartner.toActivate();

    PlainPassword plainPassword = new PlainPassword(command.adminPassword());
    UserPassword hashedPassword = passwordHasher.hash(plainPassword);
    UserName userName = new UserName(command.adminName());
    UserEmail userEmail = new UserEmail(command.adminEmail());

    User adminUser =
        UserFactory.createAdminUserWithPartner(
            userId, userName, userEmail, hashedPassword, partnerId);

    partnerRepository.save(newPartner);
    userRepository.save(adminUser);
    return new CompanyResult(newPartner, adminUser);
  }
}
