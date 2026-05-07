package com.efpcode.application.port.in.partner;

import com.efpcode.application.usecase.partner.dto.RegisterPartnerCommand;
import com.efpcode.application.usecase.partner.dto.UpdatePartnerCommand;
import com.efpcode.domain.partner.model.Partner;
import com.efpcode.domain.partner.model.PartnerId;

public interface PartnerLifeCycleCommands {

  Partner register(RegisterPartnerCommand command);

  void delete(PartnerId partnerId);

  Partner deactivate(PartnerId partnerId);

  Partner activate(PartnerId partnerId);

  Partner edit(PartnerId partnerId, UpdatePartnerCommand command);
}
