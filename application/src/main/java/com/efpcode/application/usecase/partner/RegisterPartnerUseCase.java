package com.efpcode.application.usecase.partner;

import com.efpcode.application.usecase.partner.dto.RegisterPartnerCommand;
import com.efpcode.application.usecase.partner.exceptions.PartnerAlreadyExistsException;
import com.efpcode.domain.partner.model.Partner;
import com.efpcode.domain.partner.model.PartnerName;
import com.efpcode.domain.partner.port.PartnerRepository;

public class RegisterPartnerUseCase {
  private final PartnerRepository partnerRepository;

  public RegisterPartnerUseCase(PartnerRepository partnerRepository) {
    this.partnerRepository = partnerRepository;
  }

  public Partner execute(RegisterPartnerCommand command) {
    PartnerName name = new PartnerName(command.name());

    if (partnerRepository.existsByName(name)) {
      throw new PartnerAlreadyExistsException(
          String.format("Partner is already registered with that name: %s", name.partnerName()));
    }

    Partner newPartner =
        Partner.createDraftPartner(
            command.name(), command.city(), command.country(), command.isoCode());

    partnerRepository.save(newPartner);
    return newPartner;
  }
}
