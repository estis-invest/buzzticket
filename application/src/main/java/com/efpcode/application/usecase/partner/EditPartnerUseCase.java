package com.efpcode.application.usecase.partner;

import com.efpcode.application.usecase.partner.dto.UpdatePartnerCommand;
import com.efpcode.application.usecase.partner.exceptions.PartnerAlreadyExistsException;
import com.efpcode.application.usecase.partner.exceptions.PartnerNotFoundException;
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
                    new PartnerNotFoundException(
                        "Partner not found with id:" + partnerId.partnerId()));

    PartnerName newName = (command.name() == null) ? null : (new PartnerName(command.name()));
    PartnerCity newCity = (command.city() == null) ? null : (new PartnerCity(command.city()));
    PartnerCountry newCountry =
        (command.country() == null) ? null : (new PartnerCountry(command.country()));
    PartnerIsoCode newIsoCode =
        (command.isoCode() == null) ? null : (new PartnerIsoCode(command.isoCode()));

    if (newName != null && !partner.name().equals(newName)) {
      if (partnerRepository.existsByName(newName))
        throw new PartnerAlreadyExistsException(
            "Partner name already exists: " + newName.partnerName());
    }

    Partner updatedPartner = partner.updatePartner(newName, newCity, newCountry, newIsoCode);

    partnerRepository.save(updatedPartner);
    return updatedPartner;
  }
}
