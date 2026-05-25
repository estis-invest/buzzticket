package com.efpcode.application.usecase.partner;

import com.efpcode.application.context.RequestContext;
import com.efpcode.application.policy.admin.dto.AdminContext;
import com.efpcode.application.policy.partner.PartnerAdminActionPolicy;
import com.efpcode.application.usecase.partner.exceptions.PartnerNotFoundException;
import com.efpcode.domain.partner.model.Partner;
import com.efpcode.domain.partner.model.PartnerId;
import com.efpcode.domain.partner.port.PartnerRepository;

public class DeletePartnerUseCase {
  private final PartnerRepository partnerRepository;
  private final PartnerAdminActionPolicy partnerAdminActionPolicy;

  public DeletePartnerUseCase(
      PartnerRepository partnerRepository, PartnerAdminActionPolicy partnerAdminActionPolicy) {
    this.partnerRepository = partnerRepository;
    this.partnerAdminActionPolicy = partnerAdminActionPolicy;
  }

  public void execute(PartnerId partnerId, RequestContext requestContext) {

    AdminContext adminContext = partnerAdminActionPolicy.partnerAdminValidator(requestContext);
    partnerAdminActionPolicy.assertAdminHasSamePartnerAs(adminContext, partnerId);

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
