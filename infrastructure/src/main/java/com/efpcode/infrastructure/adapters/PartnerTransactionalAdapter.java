package com.efpcode.infrastructure.adapters;

import com.efpcode.application.usecase.partner.*;
import com.efpcode.application.usecase.partner.dto.RegisterPartnerCommand;
import com.efpcode.application.usecase.partner.dto.UpdatePartnerCommand;
import com.efpcode.domain.partner.model.Partner;
import com.efpcode.domain.partner.model.PartnerId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PartnerTransactionalAdapter {

  private final RegisterPartnerUseCase registerPartnerUseCase;
  private final DeletePartnerUseCase deletePartnerUseCase;
  private final DeactivatePartnerUseCase deactivatePartnerUseCase;
  private final ActivatePartnerUseCase activatePartnerUseCase;
  private final EditPartnerUseCase editPartnerUseCase;

  public PartnerTransactionalAdapter(
      RegisterPartnerUseCase registerPartnerUseCase,
      DeletePartnerUseCase deletePartnerUseCase,
      DeactivatePartnerUseCase deactivatePartnerUseCase,
      ActivatePartnerUseCase activatePartnerUseCase,
      EditPartnerUseCase editPartnerUseCase) {

    this.registerPartnerUseCase = registerPartnerUseCase;
    this.deletePartnerUseCase = deletePartnerUseCase;
    this.deactivatePartnerUseCase = deactivatePartnerUseCase;
    this.activatePartnerUseCase = activatePartnerUseCase;
    this.editPartnerUseCase = editPartnerUseCase;
  }

  @Transactional
  public Partner registerPartner(RegisterPartnerCommand command) {
    return registerPartnerUseCase.execute(command);
  }

  @Transactional
  public void deletePartner(PartnerId id) {
    deletePartnerUseCase.execute(id);
  }

  @Transactional
  public Partner deactivatePartner(PartnerId id) {
    return deactivatePartnerUseCase.execute(id);
  }

  @Transactional
  public Partner activatePartner(PartnerId id) {
    return activatePartnerUseCase.execute(id);
  }

  @Transactional
  public Partner editPartner(PartnerId id, UpdatePartnerCommand command) {
    return editPartnerUseCase.execute(id, command);
  }
}
