package com.efpcode.application.usecase.ticket;

import com.efpcode.application.context.RequestContext;
import com.efpcode.application.policy.staff.StaffActionPolicy;
import com.efpcode.application.policy.staff.dto.StaffContext;
import com.efpcode.application.usecase.ticket.dto.ChangeTicketPriorityCommand;
import com.efpcode.application.usecase.ticket.exceptions.InvalidTicketAuthorizationException;
import com.efpcode.application.usecase.ticket.exceptions.InvalidTicketNotFoundException;
import com.efpcode.domain.ticket.model.Ticket;
import com.efpcode.domain.ticket.model.TicketId;
import com.efpcode.domain.ticket.model.TicketPriority;
import com.efpcode.domain.ticket.model.TicketUpdateAt;
import com.efpcode.domain.ticket.port.TicketRepository;
import java.time.Clock;
import java.time.Instant;

public class ChangeTicketPriorityUseCase {

  private final TicketRepository ticketRepository;
  private final StaffActionPolicy staffActionPolicy;
  private final Clock clock;

  public ChangeTicketPriorityUseCase(
      TicketRepository ticketRepository, StaffActionPolicy staffActionPolicy, Clock clock) {
    this.ticketRepository = ticketRepository;
    this.staffActionPolicy = staffActionPolicy;
    this.clock = clock;
  }

  public void execute(ChangeTicketPriorityCommand command, RequestContext requestContext) {

    StaffContext staffContext = staffActionPolicy.staffValidator(requestContext);

    TicketId ticketId = TicketId.of(command.id());

    Ticket ticket =
        ticketRepository
            .findById(ticketId)
            .orElseThrow(() -> new InvalidTicketNotFoundException("Ticket not found"));

    staffActionPolicy.assertSamePartnerAsExpected(
        staffContext.partner().id(), ticket.ownerPartner());

    boolean isAdmin = staffContext.user().role().isAdmin();
    boolean isAssigned = ticket.workers().contains(staffContext.user().id());

    if (!isAdmin && !isAssigned) {
      throw new InvalidTicketAuthorizationException(
          "Only assigned staff or admin can change ticket priority");
    }

    Instant now = Instant.now(clock);
    TicketUpdateAt updateAt = TicketUpdateAt.of(now);

    TicketPriority priority = TicketPriorityParser.parse(command.priority());

    Ticket updateTicketPriority = ticket.withPriority(priority, updateAt);

    ticketRepository.save(updateTicketPriority);
  }
}
