package com.efpcode.infrastructure.policy;

import com.efpcode.application.port.policy.BootstrapPolicy;
import com.efpcode.domain.partner.port.PartnerRepository;
import com.efpcode.infrastructure.security.exceptions.BootstrapNotAllowedException;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("!test")
@Component
public class BootstrapPolicyAdapter implements BootstrapPolicy {
  private final PartnerRepository partnerRepository;

  public BootstrapPolicyAdapter(PartnerRepository partnerRepository) {
    this.partnerRepository = partnerRepository;
  }

  @Override
  public void ensureBootstrapAllowed() {

    if (partnerRepository.existsAny()) {
      throw new BootstrapNotAllowedException("Bootstrap already completed");
    }
  }
}
