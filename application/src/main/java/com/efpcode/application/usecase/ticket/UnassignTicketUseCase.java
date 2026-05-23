package com.efpcode.application.usecase.ticket;

import com.efpcode.application.context.RequestContext;
import com.efpcode.application.policy.staff.StaffActionPolicy;
import com.efpcode.application.policy.staff.dto.StaffContext;
import com.efpcode.application.usecase.ticket.dto.UnassignTicketCommand;
import com.efpcode.application.usecase.ticket.exceptions.InvalidTicketAssignmentException;
import com.efpcode.application.usecase.ticket.exceptions.InvalidTicketAuthorizationException;
import com.efpcode.application.usecase.ticket.exceptions.InvalidTicketNotFoundException;
import com.efpcode.domain.ticket.model.Ticket;
import com.efpcode.domain.ticket.model.TicketId;
import com.efpcode.domain.ticket.model.TicketUpdateAt;
import com.efpcode.domain.ticket.port.TicketRepository;
import com.efpcode.domain.user.model.UserId;
import java.time.Clock;
import java.time.Instant;

public class UnassignTicketUseCase {
  private final TicketRepository ticketRepository;
  private final StaffActionPolicy staffActionPolicy;
  private final Clock clock;

  public UnassignTicketUseCase(
      TicketRepository ticketRepository, StaffActionPolicy staffActionPolicy, Clock clock) {
    this.ticketRepository = ticketRepository;
    this.staffActionPolicy = staffActionPolicy;
    this.clock = clock;
  }

  public void execute(UnassignTicketCommand command, RequestContext requestContext) {
    StaffContext staffContext = staffActionPolicy.staffValidator(requestContext);

    TicketId ticketId = new TicketId(command.ticketId());
    UserId targetAssigneeId = UserId.of(command.assigneeId());

    Ticket ticket =
        ticketRepository
            .findById(ticketId)
            .orElseThrow(() -> new InvalidTicketNotFoundException("Ticket not found"));

    staffActionPolicy.assertSamePartnerAsExpected(
        staffContext.partner().id(), ticket.ownerPartner());

    if (!ticket.workers().contains(targetAssigneeId)) {
      throw new InvalidTicketAssignmentException("Assignee Id not present in ticket");
    }

    boolean isSelfRemoval = targetAssigneeId.equals(staffContext.user().id());
    boolean isAdmin = staffContext.user().role().isAdmin();

    if (!isAdmin && !isSelfRemoval) {
      throw new InvalidTicketAuthorizationException(
          "Only admin or the assignee themselves can remove assignment");
    }

    Instant now = Instant.now(clock);
    TicketUpdateAt updateAt = TicketUpdateAt.of(now);

    Ticket removedAssigneeTicket =
        ticket.unassign(targetAssigneeId, staffContext.user().role(), updateAt);

    ticketRepository.save(removedAssigneeTicket);
  }
}
