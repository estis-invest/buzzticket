package com.efpcode.application.port.policy;

import com.efpcode.application.port.context.RequestContext;
import com.efpcode.application.port.policy.dto.AdminContext;
import com.efpcode.application.usecase.partner.exceptions.IllegalPartnerStatusException;
import com.efpcode.application.usecase.partner.exceptions.PartnerNotFoundException;
import com.efpcode.application.usecase.user.exceptions.IllegalUserNotFoundException;
import com.efpcode.application.usecase.user.exceptions.IllegalUserStatusException;
import com.efpcode.domain.partner.model.Partner;
import com.efpcode.domain.partner.model.PartnerId;
import com.efpcode.domain.partner.port.PartnerRepository;
import com.efpcode.domain.user.model.User;
import com.efpcode.domain.user.port.UserRepository;

public class AdminActionPolicy {
  private final UserRepository userRepository;
  private final PartnerRepository partnerRepository;

  public AdminActionPolicy(UserRepository userRepository, PartnerRepository partnerRepository) {
    this.userRepository = userRepository;
    this.partnerRepository = partnerRepository;
  }

  public AdminContext adminValidator(RequestContext requestContext) {

    requestContext.role().roleGuardIsAdmin();

    User adminHandler =
        userRepository
            .findUserById(requestContext.userId())
            .orElseThrow(() -> new IllegalUserNotFoundException("Request handler is not found"));

    if (adminHandler.role() != requestContext.role()) {
      throw new SecurityException("Token role mismatch");
    }

    adminHandler.role().roleGuardIsAdmin();
    if (!adminHandler.status().isActive()) {
      throw new IllegalUserStatusException("Request handler status other then activated");
    }

    PartnerId partnerId =
        adminHandler
            .partnerId()
            .orElseThrow(() -> new PartnerNotFoundException("Request handler has no partner"));

    Partner partner =
        partnerRepository
            .findById(partnerId)
            .orElseThrow(
                () -> new PartnerNotFoundException("Partner for request handler was not found"));

    if (!partner.status().isActive()) {
      throw new IllegalPartnerStatusException("Partner status is not active");
    }

    return new AdminContext(adminHandler, partner);
  }
}
