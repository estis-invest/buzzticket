package com.efpcode.infrastructure.adapters;

import com.efpcode.application.context.RequestContext;
import com.efpcode.application.port.in.ticket.TicketRegisterCommands;
import com.efpcode.application.usecase.ticket.*;
import com.efpcode.application.usecase.ticket.dto.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
class TicketTransactionalAdapter implements TicketRegisterCommands {

  private final RequestContext requestContext;
  private final TicketCreationUseCase ticketCreationUseCase;
  private final AssignTicketUseCase assignTicketUseCase;
  private final UnassignTicketUseCase unassignTicketUseCase;
  private final ChangeTicketStatusUseCase changeTicketStatusUseCase;
  private final ChangeTicketPriorityUseCase changeTicketPriorityUseCase;
  private final ChangeTicketDescriptionUseCase changeTicketDescriptionUseCase;

  public TicketTransactionalAdapter(
      RequestContext requestContext,
      TicketCreationUseCase ticketCreationUseCase,
      AssignTicketUseCase assignTicketUseCase,
      UnassignTicketUseCase unassignTicketUseCase,
      ChangeTicketStatusUseCase changeTicketStatusUseCase,
      ChangeTicketPriorityUseCase changeTicketPriorityUseCase,
      ChangeTicketDescriptionUseCase changeTicketDescriptionUseCase) {
    this.requestContext = requestContext;
    this.ticketCreationUseCase = ticketCreationUseCase;
    this.assignTicketUseCase = assignTicketUseCase;
    this.unassignTicketUseCase = unassignTicketUseCase;
    this.changeTicketStatusUseCase = changeTicketStatusUseCase;
    this.changeTicketPriorityUseCase = changeTicketPriorityUseCase;
    this.changeTicketDescriptionUseCase = changeTicketDescriptionUseCase;
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
  public void unassignTicket(UnassignTicketCommand command) {
    unassignTicketUseCase.execute(command, requestContext);
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

  @Override
  @Transactional
  public void changeTicketDescription(ChangeTicketDescriptionCommand command) {
    changeTicketDescriptionUseCase.execute(command, requestContext);
  }
}
