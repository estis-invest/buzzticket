package com.efpcode.infrastructure.web;

import com.efpcode.application.port.in.ticket.TicketRegisterCommands;
import com.efpcode.application.usecase.ticket.dto.*;
import com.efpcode.infrastructure.web.dto.requests.*;
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

  @PreAuthorize("hasAnyRole('ADMIN', 'SUPPORT')")
  @PatchMapping("/{id}/status")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void updateTicketStatus(
      @Valid @RequestBody UpdateTicketStatusRequest request, @PathVariable UUID id) {
    ChangeTicketStatusCommand command = new ChangeTicketStatusCommand(request.status(), id);
    ticketRegisterCommands.changeTicketStatus(command);
  }

  @PreAuthorize("hasAnyRole('ADMIN', 'SUPPORT')")
  @PatchMapping("/{id}/priority")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void updateTicketStatus(
      @Valid @RequestBody UpdateTicketPriorityRequest request, @PathVariable UUID id) {
    ChangeTicketPriorityCommand command = new ChangeTicketPriorityCommand(request.priority(), id);
    ticketRegisterCommands.changeTicketPriority(command);
  }

  @PreAuthorize("hasAnyRole('ADMIN', 'SUPPORT')")
  @PatchMapping("/{id}/description")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void updateTicketDescription(
      @Valid @RequestBody UpdateTicketDescriptionRequest request, @PathVariable UUID id) {
    ChangeTicketDescriptionCommand command =
        new ChangeTicketDescriptionCommand(request.description(), id);
    ticketRegisterCommands.changeTicketDescription(command);
  }

  // TODO: Ticket API Endpoints

  // --- Commands ---
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
