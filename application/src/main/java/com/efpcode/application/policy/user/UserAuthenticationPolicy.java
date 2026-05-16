package com.efpcode.application.policy.user;

import com.efpcode.application.context.RequestContext;
import com.efpcode.application.policy.user.dto.UserContext;
import com.efpcode.application.usecase.auth.exceptions.AuthenticatedUserNotFoundException;
import com.efpcode.application.usecase.auth.exceptions.InvalidAuthenticationException;
import com.efpcode.application.usecase.auth.exceptions.TokenValidationException;
import com.efpcode.application.usecase.partner.exceptions.IllegalPartnerStatusException;
import com.efpcode.application.usecase.partner.exceptions.PartnerNotFoundException;
import com.efpcode.application.usecase.user.exceptions.IllegalUserStatusException;
import com.efpcode.domain.partner.model.Partner;
import com.efpcode.domain.partner.model.PartnerId;
import com.efpcode.domain.partner.port.PartnerRepository;
import com.efpcode.domain.user.model.User;
import com.efpcode.domain.user.port.UserRepository;
import java.util.Optional;

public class UserAuthenticationPolicy {
  private final UserRepository userRepository;
  private final PartnerRepository partnerRepository;

  public UserAuthenticationPolicy(
      UserRepository userRepository, PartnerRepository partnerRepository) {
    this.userRepository = userRepository;
    this.partnerRepository = partnerRepository;
  }

  public UserContext userValidator(RequestContext requestContext) {

    if (requestContext == null) {
      throw new InvalidAuthenticationException("Authentication required");
    }

    User user =
        userRepository
            .findUserById(requestContext.userId())
            .orElseThrow(() -> new AuthenticatedUserNotFoundException("User no longer exists"));

    if (requestContext.role() != user.role()) {
      throw new TokenValidationException("Token role mismatch");
    }

    if (!user.isActive()) {
      throw new InvalidAuthenticationException("User is not active");
    }

    Optional<Partner> partner = Optional.empty();

    if (user.role().isStaff()) {

      PartnerId partnerId =
          user.partnerId()
              .orElseThrow(
                  () -> new IllegalUserStatusException("Staff user must have a partner id"));

      Partner partnerFound =
          partnerRepository
              .findById(partnerId)
              .orElseThrow(() -> new PartnerNotFoundException("Partner not found"));

      if (!partnerFound.isActive()) {
        throw new IllegalPartnerStatusException("Partner is not active");
      }

      partner = Optional.of(partnerFound);
    }

    return new UserContext(user, partner);
  }
}
