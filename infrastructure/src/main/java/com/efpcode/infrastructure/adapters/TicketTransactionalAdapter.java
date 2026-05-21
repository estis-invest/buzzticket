package com.efpcode.infrastructure.adapters;

import com.efpcode.application.context.RequestContext;
import com.efpcode.application.port.in.ticket.TicketRegisterCommands;
import com.efpcode.application.usecase.ticket.TicketCreationUseCase;
import com.efpcode.application.usecase.ticket.dto.RegisterTicketCommand;
import com.efpcode.application.usecase.ticket.dto.TicketResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
class TicketTransactionalAdapter implements TicketRegisterCommands {

  private final RequestContext requestContext;
  private final TicketCreationUseCase ticketCreationUseCase;

  public TicketTransactionalAdapter(
      RequestContext requestContext, TicketCreationUseCase ticketCreationUseCase) {
    this.requestContext = requestContext;
    this.ticketCreationUseCase = ticketCreationUseCase;
  }

  @Override
  @Transactional
  public TicketResult createTicket(RegisterTicketCommand command) {
    return ticketCreationUseCase.execute(command, requestContext);
  }
}
