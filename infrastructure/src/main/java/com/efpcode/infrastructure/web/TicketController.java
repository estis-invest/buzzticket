package com.efpcode.infrastructure.web;

import com.efpcode.application.port.in.ticket.TicketRegisterCommands;
import com.efpcode.application.usecase.ticket.dto.RegisterTicketCommand;
import com.efpcode.application.usecase.ticket.dto.TicketResult;
import com.efpcode.infrastructure.web.dto.requests.RegisterTicketRequest;
import com.efpcode.infrastructure.web.dto.responses.TicketResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/tickets")
class TicketController {

  private final TicketRegisterCommands ticketRegisterCommands;

  public TicketController(TicketRegisterCommands ticketRegisterCommands) {
    this.ticketRegisterCommands = ticketRegisterCommands;
  }

  @PostMapping
  public ResponseEntity<TicketResponse> registerTicket(
      @Valid @RequestBody RegisterTicketRequest request) {
    var command =
        new RegisterTicketCommand(
            request.title(), request.description(), request.priority(), request.partnerId());

    TicketResult ticketResult = ticketRegisterCommands.createTicket(command);

    return ResponseEntity.status(HttpStatus.CREATED).body(TicketResponse.fromResult(ticketResult));
  }
}
