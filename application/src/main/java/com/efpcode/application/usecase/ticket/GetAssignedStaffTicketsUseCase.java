package com.efpcode.application.usecase.ticket;

import com.efpcode.application.context.RequestContext;
import com.efpcode.application.policy.staff.StaffActionPolicy;
import com.efpcode.application.policy.staff.dto.StaffContext;
import com.efpcode.application.usecase.ticket.dto.TicketStaffResult;
import com.efpcode.domain.ticket.model.Ticket;
import com.efpcode.domain.ticket.port.TicketRepository;
import java.util.List;

public class GetAssignedStaffTicketsUseCase {
  private final TicketRepository ticketRepository;
  private final StaffActionPolicy staffActionPolicy;

  public GetAssignedStaffTicketsUseCase(
      TicketRepository ticketRepository, StaffActionPolicy staffActionPolicy) {
    this.ticketRepository = ticketRepository;
    this.staffActionPolicy = staffActionPolicy;
  }

  public List<TicketStaffResult> execute(RequestContext requestContext) {
    StaffContext staffContext = staffActionPolicy.staffValidator(requestContext);
    List<Ticket> assignedTickets =
        ticketRepository.findByAssigneeAndPartner(
            staffContext.user().id(), staffContext.partner().id());

    return assignedTickets.stream().map(TicketStaffResult::fromDomain).toList();
  }
}
