package com.efpcode.infrastructure.policy;

import com.efpcode.application.port.policy.PartnerAdminPolicy;
import com.efpcode.domain.partner.model.PartnerId;
import com.efpcode.domain.user.port.UserRepository;
import com.efpcode.infrastructure.persistence.exceptions.PartnerRequiresAdminException;
import org.springframework.stereotype.Component;

@Component
public class PartnerAdminPolicyJpaAdapter implements PartnerAdminPolicy {
  private final UserRepository userRepository;

  public PartnerAdminPolicyJpaAdapter(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public void ensurePartnerHasAdmin(PartnerId partnerId) {
    if (!userRepository.existsAdminForPartner(partnerId)) {
      throw new PartnerRequiresAdminException("Partner must have at least one admin", null);
    }
  }
}
