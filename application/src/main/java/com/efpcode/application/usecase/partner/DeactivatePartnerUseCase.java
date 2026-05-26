package com.efpcode.application.usecase.partner;

import com.efpcode.application.context.RequestContext;
import com.efpcode.application.policy.admin.dto.AdminContext;
import com.efpcode.application.policy.partner.PartnerAdminActionPolicy;
import com.efpcode.application.usecase.partner.exceptions.PartnerNotFoundException;
import com.efpcode.domain.partner.model.Partner;
import com.efpcode.domain.partner.model.PartnerId;
import com.efpcode.domain.partner.port.PartnerRepository;

public class DeactivatePartnerUseCase {
  private final PartnerRepository partnerRepository;
  private final PartnerAdminActionPolicy partnerAdminActionPolicy;

  public DeactivatePartnerUseCase(
      PartnerRepository partnerRepository, PartnerAdminActionPolicy partnerAdminActionPolicy) {
    this.partnerRepository = partnerRepository;
    this.partnerAdminActionPolicy = partnerAdminActionPolicy;
  }

  public Partner execute(PartnerId partnerId, RequestContext requestContext) {
    AdminContext adminContext = partnerAdminActionPolicy.partnerAdminValidator(requestContext);
    partnerAdminActionPolicy.assertAdminHasSamePartnerAs(adminContext, partnerId);
    Partner partner =
        partnerRepository
            .findById(partnerId)
            .orElseThrow(
                () ->
                    new PartnerNotFoundException(
                        "Partner not found with id:" + partnerId.partnerId()));

    Partner deactivatedPartner = partner.toDeactivate();
    partnerRepository.save(deactivatedPartner);
    return deactivatedPartner;
  }
}
