package com.efpcode.application.usecase.partner;

import com.efpcode.application.usecase.partner.exceptions.PartnerNotFoundException;
import com.efpcode.domain.partner.model.Partner;
import com.efpcode.domain.partner.port.PartnerRepository;
import java.util.List;

public class GetAllPartnersUseCase {
  private final PartnerRepository partnerRepository;

  public GetAllPartnersUseCase(PartnerRepository partnerRepository) {
    this.partnerRepository = partnerRepository;
  }

  public List<Partner> execute() {
    List<Partner> partners = partnerRepository.findAll();

    if (partners.isEmpty()) {
      throw new PartnerNotFoundException("No active partners found");
    }
    return partners;
  }
}
