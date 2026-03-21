package com.efpcode.application.usecase.partner;

import com.efpcode.application.usecase.partner.exceptions.PartnerNotFoundException;
import com.efpcode.domain.partner.model.Partner;
import com.efpcode.domain.partner.model.PartnerId;
import com.efpcode.domain.partner.port.PartnerRepository;

public class DeletePartnerUseCase {
  private final PartnerRepository partnerRepository;

  public DeletePartnerUseCase(PartnerRepository partnerRepository) {
    this.partnerRepository = partnerRepository;
  }

  public void execute(PartnerId partnerId) {
    Partner partner =
        partnerRepository
            .findById(partnerId)
            .orElseThrow(
                () ->
                    new PartnerNotFoundException(
                        "Partner not found with id: " + partnerId.partnerId()));

    Partner deletedPartner = partner.toDelete();

    partnerRepository.save(deletedPartner);
  }
}
