package com.efpcode.application.usecase.partner;

import com.efpcode.application.usecase.partner.exceptions.PartnerNotFoundByIdException;
import com.efpcode.domain.partner.model.Partner;
import com.efpcode.domain.partner.model.PartnerId;
import com.efpcode.domain.partner.port.PartnerRepository;

public class GetPartnerUseCase {
  private final PartnerRepository partnerRepository;

  public GetPartnerUseCase(PartnerRepository partnerRepository) {
    this.partnerRepository = partnerRepository;
  }

  public Partner execute(PartnerId partnerId) {
    return partnerRepository
        .findById(partnerId)
        .orElseThrow(
            () ->
                new PartnerNotFoundByIdException(
                    "Partner was not found: " + partnerId.partnerId().toString()));
  }
}
