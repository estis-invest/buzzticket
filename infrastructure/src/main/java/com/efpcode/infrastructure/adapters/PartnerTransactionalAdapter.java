package com.efpcode.infrastructure.adapters;

import com.efpcode.application.context.RequestContext;
import com.efpcode.application.port.in.partner.PartnerLifeCycleCommands;
import com.efpcode.application.usecase.partner.*;
import com.efpcode.application.usecase.partner.dto.RegisterPartnerCommand;
import com.efpcode.application.usecase.partner.dto.UpdatePartnerCommand;
import com.efpcode.domain.partner.model.Partner;
import com.efpcode.domain.partner.model.PartnerId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PartnerTransactionalAdapter implements PartnerLifeCycleCommands {

  private final RegisterPartnerUseCase registerPartnerUseCase;
  private final DeletePartnerUseCase deletePartnerUseCase;
  private final DeactivatePartnerUseCase deactivatePartnerUseCase;
  private final ActivatePartnerUseCase activatePartnerUseCase;
  private final EditPartnerUseCase editPartnerUseCase;
  private final RequestContext requestContext;

  public PartnerTransactionalAdapter(
      RegisterPartnerUseCase registerPartnerUseCase,
      DeletePartnerUseCase deletePartnerUseCase,
      DeactivatePartnerUseCase deactivatePartnerUseCase,
      ActivatePartnerUseCase activatePartnerUseCase,
      EditPartnerUseCase editPartnerUseCase,
      RequestContext requestContext) {

    this.registerPartnerUseCase = registerPartnerUseCase;
    this.deletePartnerUseCase = deletePartnerUseCase;
    this.deactivatePartnerUseCase = deactivatePartnerUseCase;
    this.activatePartnerUseCase = activatePartnerUseCase;
    this.editPartnerUseCase = editPartnerUseCase;
    this.requestContext = requestContext;
  }

  @Transactional
  @Override
  public Partner register(RegisterPartnerCommand command) {
    return registerPartnerUseCase.execute(command, requestContext);
  }

  @Override
  @Transactional
  public void delete(PartnerId id) {
    deletePartnerUseCase.execute(id, requestContext);
  }

  @Override
  @Transactional
  public Partner deactivate(PartnerId id) {
    return deactivatePartnerUseCase.execute(id, requestContext);
  }

  @Override
  @Transactional
  public Partner activate(PartnerId id) {
    return activatePartnerUseCase.execute(id, requestContext);
  }

  @Override
  @Transactional
  public Partner edit(PartnerId id, UpdatePartnerCommand command) {
    return editPartnerUseCase.execute(id, command, requestContext);
  }
}
