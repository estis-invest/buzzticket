package com.efpcode.application.port.in.ticket;

import com.efpcode.application.usecase.ticket.dto.*;

public interface TicketRegisterCommands {
  TicketResult createTicket(RegisterTicketCommand command);

  void assignTicket(AssignTicketCommand command);

  void unassignTicket(UnassignTicketCommand command);

  void changeTicketStatus(ChangeTicketStatusCommand command);

  void changeTicketPriority(ChangeTicketPriorityCommand command);

  void changeTicketDescription(ChangeTicketDescriptionCommand command);
}
