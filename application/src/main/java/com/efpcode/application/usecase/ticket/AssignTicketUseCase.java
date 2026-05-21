package com.efpcode.application.usecase.ticket;

import com.efpcode.application.context.RequestContext;
import com.efpcode.application.policy.staff.dto.StaffActionPolicy;
import com.efpcode.application.policy.staff.dto.StaffContext;
import com.efpcode.application.usecase.partner.exceptions.PartnerNotFoundException;
import com.efpcode.application.usecase.ticket.dto.AssignTicketCommand;
import com.efpcode.application.usecase.ticket.exceptions.InvalidTicketNotFoundException;
import com.efpcode.application.usecase.user.exceptions.IllegalUserNotFoundException;
import com.efpcode.domain.partner.model.PartnerId;
import com.efpcode.domain.ticket.model.Ticket;
import com.efpcode.domain.ticket.model.TicketId;
import com.efpcode.domain.ticket.model.TicketUpdateAt;
import com.efpcode.domain.ticket.port.TicketRepository;
import com.efpcode.domain.user.model.User;
import com.efpcode.domain.user.model.UserId;
import com.efpcode.domain.user.port.UserRepository;
import java.time.Clock;
import java.time.Instant;

public class AssignTicketUseCase {
  private final TicketRepository ticketRepository;
  private final UserRepository userRepository;
  private final StaffActionPolicy staffActionPolicy;
  private final Clock clock;

  public AssignTicketUseCase(
      TicketRepository ticketRepository,
      UserRepository userRepository,
      StaffActionPolicy staffActionPolicy,
      Clock clock) {
    this.ticketRepository = ticketRepository;
    this.userRepository = userRepository;
    this.staffActionPolicy = staffActionPolicy;
    this.clock = clock;
  }

  public void execute(AssignTicketCommand command, RequestContext requestContext) {
    StaffContext staffContext = staffActionPolicy.staffValidator(requestContext);

    UserId userId = new UserId(command.assigneeId());
    TicketId ticketId = new TicketId(command.ticketId());

    Ticket ticket =
        ticketRepository
            .findById(ticketId)
            .orElseThrow(() -> new InvalidTicketNotFoundException("Ticket not found"));

    staffActionPolicy.assertSamePartnerAsExpected(
        staffContext.partner().id(), ticket.ownerPartner());

    User assignee =
        userRepository
            .findUserById(userId)
            .orElseThrow(() -> new IllegalUserNotFoundException("User not found"));

    staffActionPolicy.assertAccountIsActive(assignee);

    PartnerId assigneePartnerId =
        assignee
            .partnerId()
            .orElseThrow(() -> new PartnerNotFoundException("Staff must have partner"));

    staffActionPolicy.assertSamePartnerAsExpected(assigneePartnerId, ticket.ownerPartner());

    Instant now = Instant.now(clock);

    TicketUpdateAt updateAt = TicketUpdateAt.of(now);

    var assignedTicket = ticket.assign(assignee.id(), assignee.role(), updateAt);

    ticketRepository.save(assignedTicket);
  }
}
