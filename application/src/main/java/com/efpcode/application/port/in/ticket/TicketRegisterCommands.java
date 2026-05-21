package com.efpcode.application.port.in.ticket;

import com.efpcode.application.usecase.ticket.dto.AssignTicketCommand;
import com.efpcode.application.usecase.ticket.dto.RegisterTicketCommand;
import com.efpcode.application.usecase.ticket.dto.TicketResult;

public interface TicketRegisterCommands {
  TicketResult createTicket(RegisterTicketCommand command);

  void assignTicket(AssignTicketCommand command);
}
