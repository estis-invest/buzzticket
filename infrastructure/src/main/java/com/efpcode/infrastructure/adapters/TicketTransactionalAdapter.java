package com.efpcode.infrastructure.adapters;

import com.efpcode.application.context.RequestContext;
import com.efpcode.application.port.in.ticket.TicketRegisterCommands;
import com.efpcode.application.usecase.ticket.AssignTicketUseCase;
import com.efpcode.application.usecase.ticket.ChangeTicketPriorityUseCase;
import com.efpcode.application.usecase.ticket.ChangeTicketStatusUseCase;
import com.efpcode.application.usecase.ticket.TicketCreationUseCase;
import com.efpcode.application.usecase.ticket.dto.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
class TicketTransactionalAdapter implements TicketRegisterCommands {

  private final RequestContext requestContext;
  private final TicketCreationUseCase ticketCreationUseCase;
  private final AssignTicketUseCase assignTicketUseCase;
  private final ChangeTicketStatusUseCase changeTicketStatusUseCase;
  private final ChangeTicketPriorityUseCase changeTicketPriorityUseCase;

  public TicketTransactionalAdapter(
      RequestContext requestContext,
      TicketCreationUseCase ticketCreationUseCase,
      AssignTicketUseCase assignTicketUseCase,
      ChangeTicketStatusUseCase changeTicketStatusUseCase,
      ChangeTicketPriorityUseCase changeTicketPriorityUseCase) {
    this.requestContext = requestContext;
    this.ticketCreationUseCase = ticketCreationUseCase;
    this.assignTicketUseCase = assignTicketUseCase;
    this.changeTicketStatusUseCase = changeTicketStatusUseCase;
    this.changeTicketPriorityUseCase = changeTicketPriorityUseCase;
  }

  @Override
  @Transactional
  public TicketResult createTicket(RegisterTicketCommand command) {
    return ticketCreationUseCase.execute(command, requestContext);
  }

  @Override
  @Transactional
  public void assignTicket(AssignTicketCommand command) {
    assignTicketUseCase.execute(command, requestContext);
  }

  @Override
  @Transactional
  public void changeTicketStatus(ChangeTicketStatusCommand command) {
    changeTicketStatusUseCase.execute(command, requestContext);
  }

  @Override
  @Transactional
  public void changeTicketPriority(ChangeTicketPriorityCommand command) {
    changeTicketPriorityUseCase.execute(command, requestContext);
  }
}
