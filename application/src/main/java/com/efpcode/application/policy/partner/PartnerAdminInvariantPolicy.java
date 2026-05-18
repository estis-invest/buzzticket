package com.efpcode.application.policy.partner;

import com.efpcode.application.usecase.user.exceptions.PartnerRequiresAdminException;
import com.efpcode.domain.partner.model.PartnerId;
import com.efpcode.domain.user.model.User;
import com.efpcode.domain.user.model.UserRole;
import com.efpcode.domain.user.port.UserRepository;

public class PartnerAdminInvariantPolicy {

  private final UserRepository userRepository;

  public PartnerAdminInvariantPolicy(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public void ensureCanRemoveAdmin(PartnerId partnerId) {
    long numberOfAdmins =
        userRepository.findAdminsForUpdate(partnerId).stream()
            .filter(User::isActive)
            .filter(user -> user.role() == UserRole.ADMIN)
            .count();

    if (numberOfAdmins <= 1) {
      throw new PartnerRequiresAdminException("Partner must keep at least one active admin");
    }
  }
}
