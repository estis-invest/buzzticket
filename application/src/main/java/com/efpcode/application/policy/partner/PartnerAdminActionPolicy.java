package com.efpcode.application.policy.partner;

import com.efpcode.application.context.RequestContext;
import com.efpcode.application.policy.admin.dto.AdminContext;
import com.efpcode.application.usecase.auth.exceptions.TokenValidationException;
import com.efpcode.application.usecase.partner.exceptions.IllegalPartnerStatusException;
import com.efpcode.application.usecase.partner.exceptions.PartnerContextMismatchException;
import com.efpcode.application.usecase.partner.exceptions.PartnerNotFoundException;
import com.efpcode.application.usecase.user.exceptions.IllegalUserNotFoundException;
import com.efpcode.application.usecase.user.exceptions.IllegalUserStatusException;
import com.efpcode.domain.partner.model.Partner;
import com.efpcode.domain.partner.model.PartnerId;
import com.efpcode.domain.partner.port.PartnerRepository;
import com.efpcode.domain.user.model.User;
import com.efpcode.domain.user.port.UserRepository;

public class PartnerAdminActionPolicy {
  private final UserRepository userRepository;
  private final PartnerRepository partnerRepository;

  public PartnerAdminActionPolicy(
      UserRepository userRepository, PartnerRepository partnerRepository) {
    this.userRepository = userRepository;
    this.partnerRepository = partnerRepository;
  }

  public AdminContext partnerAdminValidator(RequestContext requestContext) {
    requestContext.role().roleGuardIsAdmin();

    User adminHandler =
        userRepository
            .findUserById(requestContext.userId())
            .orElseThrow(() -> new IllegalUserNotFoundException("Request handler is not found"));

    if (adminHandler.role() != requestContext.role()) {
      throw new TokenValidationException("Token role mismatch");
    }

    if (!adminHandler.status().isActive()) {
      throw new IllegalUserStatusException("Request handler status other than activated");
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

    if (partner.status().isDeleted()) {
      throw new IllegalPartnerStatusException("Partner status is DELETED");
    }

    return new AdminContext(adminHandler, partner);
  }

  public void assertAdminHasSamePartnerAs(AdminContext adminContext, PartnerId partnerId) {
    if (!adminContext.partner().id().equals(partnerId)) {
      throw new PartnerContextMismatchException("Admin partner and partnerId are different");
    }
  }
}
