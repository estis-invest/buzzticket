package com.efpcode.application.usecase.partner;

import com.efpcode.application.usecase.partner.dto.UpdatePartnerCommand;
import com.efpcode.application.usecase.partner.exceptions.InvalidPartnerCommandArgumentException;
import com.efpcode.application.usecase.partner.exceptions.PartnerAlreadyExistsException;
import com.efpcode.domain.partner.model.*;
import com.efpcode.domain.partner.port.PartnerRepository;

public class EditPartnerUseCase {
  private final PartnerRepository partnerRepository;

  public EditPartnerUseCase(PartnerRepository partnerRepository) {
    this.partnerRepository = partnerRepository;
  }

  public Partner execute(PartnerId partnerId, UpdatePartnerCommand command) {

    Partner partner =
        partnerRepository
            .findById(partnerId)
            .orElseThrow(
                () ->
                    new InvalidPartnerCommandArgumentException(
                        "Partner not found with id:" + partnerId.partnerId()));

    UpdatePartnerCommand fullcommand = UpdatePartnerCommand.merge(command, partner);

    PartnerName newName = new PartnerName(fullcommand.name());
    PartnerCity newCity = new PartnerCity(fullcommand.city());
    PartnerCountry newCountry = new PartnerCountry(fullcommand.country());
    PartnerIsoCode newIsoCode = new PartnerIsoCode(fullcommand.isoCode());

    if (!partner.name().partnerName().equals(newName.partnerName())) {
      if (partnerRepository.existsByName(newName))
        throw new PartnerAlreadyExistsException(
            "Partner name already exists: " + newName.partnerName());
    }

    Partner updatedPartner = partner.updatePartner(newName, newCity, newCountry, newIsoCode);

    partnerRepository.save(updatedPartner);
    return updatedPartner;
  }
}
