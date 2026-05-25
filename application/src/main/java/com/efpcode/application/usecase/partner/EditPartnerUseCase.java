package com.efpcode.application.usecase.partner;

import com.efpcode.application.context.RequestContext;
import com.efpcode.application.policy.admin.dto.AdminContext;
import com.efpcode.application.policy.partner.PartnerAdminActionPolicy;
import com.efpcode.application.usecase.partner.dto.UpdatePartnerCommand;
import com.efpcode.application.usecase.partner.exceptions.InvalidPartnerCommandArgumentException;
import com.efpcode.application.usecase.partner.exceptions.PartnerAlreadyExistsException;
import com.efpcode.domain.partner.model.*;
import com.efpcode.domain.partner.port.PartnerRepository;

public class EditPartnerUseCase {
  private final PartnerRepository partnerRepository;
  private final PartnerAdminActionPolicy partnerAdminActionPolicy;

  public EditPartnerUseCase(
      PartnerRepository partnerRepository, PartnerAdminActionPolicy partnerAdminActionPolicy) {
    this.partnerRepository = partnerRepository;
    this.partnerAdminActionPolicy = partnerAdminActionPolicy;
  }

  public Partner execute(
      PartnerId partnerId, UpdatePartnerCommand command, RequestContext requestContext) {
    AdminContext adminContext = partnerAdminActionPolicy.partnerAdminValidator(requestContext);

    partnerAdminActionPolicy.assertAdminHasSamePartnerAs(adminContext, partnerId);

    Partner partner =
        partnerRepository
            .findById(partnerId)
            .orElseThrow(
                () ->
                    new InvalidPartnerCommandArgumentException(
                        "Partner not found with id:" + partnerId.partnerId()));

    UpdatePartnerCommand fullCommand = UpdatePartnerCommand.merge(command, partner);

    PartnerName newName = new PartnerName(fullCommand.name());
    PartnerCity newCity = new PartnerCity(fullCommand.city());
    PartnerCountry newCountry = new PartnerCountry(fullCommand.country());
    PartnerIsoCode newIsoCode = new PartnerIsoCode(fullCommand.isoCode());

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
