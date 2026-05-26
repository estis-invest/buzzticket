package com.efpcode.application.usecase.partner;

import com.efpcode.application.context.RequestContext;
import com.efpcode.application.policy.partner.PartnerAdminActionPolicy;
import com.efpcode.application.usecase.partner.exceptions.PartnerNotFoundException;
import com.efpcode.domain.partner.model.Partner;
import com.efpcode.domain.partner.model.PartnerId;
import com.efpcode.domain.partner.port.PartnerRepository;

public class ActivatePartnerUseCase {
  private final PartnerRepository partnerRepository;
  private final PartnerAdminActionPolicy partnerAdminActionPolicy;

  public ActivatePartnerUseCase(
      PartnerRepository partnerRepository, PartnerAdminActionPolicy partnerAdminActionPolicy) {
    this.partnerRepository = partnerRepository;
    this.partnerAdminActionPolicy = partnerAdminActionPolicy;
  }

  public Partner execute(PartnerId partnerId, RequestContext requestContext) {
    partnerAdminActionPolicy.partnerAdminValidator(requestContext);

    Partner partner =
        partnerRepository
            .findById(partnerId)
            .orElseThrow(
                () ->
                    new PartnerNotFoundException(
                        "Partner not found with id:" + partnerId.partnerId()));
    Partner activatedPartner = partner.toActivate();
    partnerRepository.save(activatedPartner);
    return activatedPartner;
  }
}
