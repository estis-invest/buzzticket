package com.efpcode.application.port.policy;

import com.efpcode.domain.partner.model.PartnerId;

public interface PartnerAdminPolicy {

  void ensurePartnerHasAdmin(PartnerId partnerId);
}
