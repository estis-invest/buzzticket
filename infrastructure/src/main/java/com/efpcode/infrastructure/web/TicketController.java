package com.efpcode.infrastructure.web;

import com.efpcode.application.port.in.ticket.TicketRegisterCommands;
import com.efpcode.application.usecase.ticket.dto.AssignTicketCommand;
import com.efpcode.application.usecase.ticket.dto.RegisterTicketCommand;
import com.efpcode.application.usecase.ticket.dto.TicketResult;
import com.efpcode.infrastructure.web.dto.requests.AssignTicketRequest;
import com.efpcode.infrastructure.web.dto.requests.RegisterTicketRequest;
import com.efpcode.infrastructure.web.dto.responses.TicketResponse;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

  @PreAuthorize("hasAnyRole('ADMIN', 'SUPPORT')")
  @PostMapping("/{id}/assignments")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void assignTicketToUser(
      @Valid @RequestBody AssignTicketRequest request, @PathVariable UUID id) {

    AssignTicketCommand assignTicketCommand = new AssignTicketCommand(id, request.assigneeId());

    ticketRegisterCommands.assignTicket(assignTicketCommand);
  }

  // TODO: Ticket API Endpoints

  // --- Commands ---
  //  TODO [Ticket] Implement POST    /api/v1/tickets                         (create ticket)
  //  TODO [Ticket] Implement POST    /api/v1/tickets/{id}/assignments        (assign user)
  //  TODO [Ticket] Implement PATCH   /api/v1/tickets/{id}/status             (change status)
  //  TODO [Ticket] Implement PATCH   /api/v1/tickets/{id}/description        (update description)
  //
  // --- Customer (Reporter) ---
  //  TODO [Ticket] Implement GET     /api/v1/tickets                         (list user tickets)
  //  TODO [Ticket] Implement GET     /api/v1/tickets/{id}                    (get ticket by id,
  // ownership check)
  //
  // --- Staff limited to partnerId ---
  //  TODO [Ticket] Implement GET     /api/v1/staff/tickets/assigned          (tickets assigned to
  // user)
  //  TODO [Ticket] Implement GET     /api/v1/staff/tickets/partner           (tickets for staff
  // partner)
  //  TODO [Ticket] Implement GET     /api/v1/staff/tickets/{id}              (get ticket by id,
  // staff view)
  //  TODO [Ticket] Implement GET     /api/v1/staff/tickets/slug/{slug}       (get ticket by slug)
  //
}
