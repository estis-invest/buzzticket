package com.efpcode.application.usecase.partner;

import com.efpcode.application.usecase.partner.dto.UpdatePartnerCommand;
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
    Partner updatedPartner =
        partner.updatePartner(
            new PartnerName(command.name()),
            new PartnerCity(command.city()),
            new PartnerCountry(command.country()),
            new PartnerIsoCode(command.isoCode()));
    partnerRepository.save(updatedPartner);
    return updatedPartner;
  }
}
