package com.efpcode.application.policy.user;

import com.efpcode.application.context.RequestContext;
import com.efpcode.application.policy.user.dto.UserContext;
import com.efpcode.application.usecase.auth.exceptions.AuthenticatedUserNotFoundException;
import com.efpcode.application.usecase.auth.exceptions.InvalidAuthenticationException;
import com.efpcode.application.usecase.auth.exceptions.TokenValidationException;
import com.efpcode.domain.partner.model.Partner;
import com.efpcode.domain.partner.model.PartnerId;
import com.efpcode.domain.partner.port.PartnerRepository;
import com.efpcode.domain.user.model.User;
import com.efpcode.domain.user.port.UserRepository;
import java.util.Optional;

public class UserAuthenticationPolicy {
  private final UserRepository userRepository;
  private final PartnerRepository partnerRepository;
  private static final String ERROR_MESSAGE = "Authentication failed";

  public UserAuthenticationPolicy(
      UserRepository userRepository, PartnerRepository partnerRepository) {
    this.userRepository = userRepository;
    this.partnerRepository = partnerRepository;
  }

  public UserContext userValidator(RequestContext requestContext) {
    User user = userAccountValidator(requestContext);

    Optional<Partner> partner = Optional.empty();

    if (user.role().isStaff()) {

      PartnerId partnerId =
          user.partnerId().orElseThrow(() -> new InvalidAuthenticationException(ERROR_MESSAGE));

      Partner partnerFound =
          partnerRepository
              .findById(partnerId)
              .orElseThrow(() -> new InvalidAuthenticationException(ERROR_MESSAGE));

      if (!partnerFound.isActive()) {
        throw new InvalidAuthenticationException(ERROR_MESSAGE);
      }

      partner = Optional.of(partnerFound);
    }

    return new UserContext(user, partner);
  }

  public User userAccountValidator(RequestContext requestContext) {
    if (requestContext == null) {
      throw new InvalidAuthenticationException(ERROR_MESSAGE);
    }

    User user =
        userRepository
            .findUserById(requestContext.userId())
            .orElseThrow(() -> new AuthenticatedUserNotFoundException(ERROR_MESSAGE));

    if (requestContext.role() != user.role()) {
      throw new TokenValidationException(ERROR_MESSAGE);
    }

    if (!user.isActive()) {
      throw new InvalidAuthenticationException(ERROR_MESSAGE);
    }

    return user;
  }
}
