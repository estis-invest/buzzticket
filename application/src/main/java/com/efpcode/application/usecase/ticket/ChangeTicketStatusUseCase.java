package com.efpcode.application.usecase.ticket;

import com.efpcode.application.context.RequestContext;
import com.efpcode.application.policy.staff.dto.StaffActionPolicy;
import com.efpcode.application.policy.staff.dto.StaffContext;
import com.efpcode.application.usecase.ticket.dto.ChangeTicketStatusCommand;
import com.efpcode.application.usecase.ticket.exceptions.InvalidTicketNotFoundException;
import com.efpcode.application.usecase.ticket.exceptions.InvalidTicketStatusTransitionException;
import com.efpcode.domain.ticket.model.Ticket;
import com.efpcode.domain.ticket.model.TicketId;
import com.efpcode.domain.ticket.model.TicketStatus;
import com.efpcode.domain.ticket.model.TicketUpdateAt;
import com.efpcode.domain.ticket.port.TicketRepository;
import java.time.Clock;
import java.time.Instant;

public class ChangeTicketStatusUseCase {
  private final TicketRepository ticketRepository;
  private final StaffActionPolicy staffActionPolicy;
  private final Clock clock;

  public ChangeTicketStatusUseCase(
      TicketRepository ticketRepository, StaffActionPolicy staffActionPolicy, Clock clock) {
    this.ticketRepository = ticketRepository;
    this.staffActionPolicy = staffActionPolicy;
    this.clock = clock;
  }

  public void execute(ChangeTicketStatusCommand command, RequestContext requestContext) {

    StaffContext staffContext = staffActionPolicy.staffValidator(requestContext);

    TicketId ticketId = TicketId.of(command.ticketId());

    Ticket ticket =
        ticketRepository
            .findById(ticketId)
            .orElseThrow(() -> new InvalidTicketNotFoundException("Ticket not found"));

    staffActionPolicy.assertSamePartnerAsExpected(
        staffContext.partner().id(), ticket.ownerPartner());

    boolean isAdmin = staffContext.user().role().isAdmin();
    boolean isAssigned = ticket.workers().workers().contains(staffContext.user().id());

    if (!isAdmin && !isAssigned) {
      throw new InvalidTicketStatusTransitionException(
          "Only assigned staff or admin can change ticket status");
    }

    Instant now = Instant.now(clock);
    TicketUpdateAt updateAt = TicketUpdateAt.of(now);

    TicketStatus status = TicketStatusParser.parse(command.status());

    Ticket tickStatusUpdated;

    switch (status) {
      case OPEN -> tickStatusUpdated = ticket.open(updateAt);
      case CLOSED -> tickStatusUpdated = ticket.close(updateAt);
      case ARCHIVED -> tickStatusUpdated = ticket.archive(updateAt);
      default -> throw new InvalidTicketStatusTransitionException("Invalid ticket status");
    }

    ticketRepository.save(tickStatusUpdated);
  }
}
