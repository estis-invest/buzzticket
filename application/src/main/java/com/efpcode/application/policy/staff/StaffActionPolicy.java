package com.efpcode.application.policy.staff;

import com.efpcode.application.context.RequestContext;
import com.efpcode.application.policy.staff.dto.StaffContext;
import com.efpcode.application.usecase.partner.exceptions.IllegalPartnerStatusException;
import com.efpcode.application.usecase.partner.exceptions.PartnerNotFoundException;
import com.efpcode.application.usecase.user.exceptions.IllegalUserNotFoundException;
import com.efpcode.application.usecase.user.exceptions.IllegalUserStatusException;
import com.efpcode.domain.partner.model.Partner;
import com.efpcode.domain.partner.model.PartnerId;
import com.efpcode.domain.partner.port.PartnerRepository;
import com.efpcode.domain.user.model.User;
import com.efpcode.domain.user.port.UserRepository;

public class StaffActionPolicy {

  private final UserRepository userRepository;
  private final PartnerRepository partnerRepository;

  public StaffActionPolicy(UserRepository userRepository, PartnerRepository partnerRepository) {
    this.userRepository = userRepository;
    this.partnerRepository = partnerRepository;
  }

  public StaffContext staffValidator(RequestContext requestContext) {

    requestContext.role().roleGuardIsStaff();

    User staffHandler =
        userRepository
            .findUserById(requestContext.userId())
            .orElseThrow(() -> new IllegalUserNotFoundException("Staff handler not found"));

    if (staffHandler.role() != requestContext.role()) {
      throw new SecurityException("Token role mismatch");
    }

    if (!staffHandler.status().isActive()) {
      throw new IllegalUserStatusException("Request handler status other than activated");
    }

    PartnerId partnerId =
        staffHandler
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

    return new StaffContext(staffHandler, partner);
  }

  public void assertSamePartnerAsExpected(PartnerId sourcePartnerId, PartnerId expectedPartnerId) {
    if (!expectedPartnerId.equals(sourcePartnerId)) {
      throw new IllegalPartnerStatusException("Partner mismatch against expected");
    }
  }

  public void assertAccountIsActive(User source) {
    if (!source.isActive()) {
      throw new IllegalUserStatusException("Account must be active");
    }
  }
}
