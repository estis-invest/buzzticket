package com.efpcode.application.usecase.ticket;

import com.efpcode.application.context.RequestContext;
import com.efpcode.application.policy.staff.StaffActionPolicy;
import com.efpcode.application.policy.staff.dto.StaffContext;
import com.efpcode.application.usecase.ticket.dto.GetStaffTicketQuery;
import com.efpcode.application.usecase.ticket.dto.TicketStaffResult;
import com.efpcode.application.usecase.ticket.exceptions.InvalidTicketNotFoundException;
import com.efpcode.domain.ticket.model.Ticket;
import com.efpcode.domain.ticket.model.TicketId;
import com.efpcode.domain.ticket.port.TicketRepository;

public class GetStaffTicketUseCase {
  private final TicketRepository ticketRepository;
  private final StaffActionPolicy staffActionPolicy;

  public GetStaffTicketUseCase(
      TicketRepository ticketRepository, StaffActionPolicy staffActionPolicy) {

    this.ticketRepository = ticketRepository;
    this.staffActionPolicy = staffActionPolicy;
  }

  public TicketStaffResult execute(
      GetStaffTicketQuery getStaffTicketQuery, RequestContext requestContext) {
    StaffContext staffContext = staffActionPolicy.staffValidator(requestContext);

    TicketId ticketId = TicketId.of(getStaffTicketQuery.ticketId());

    Ticket ticket =
        ticketRepository
            .findById(ticketId)
            .orElseThrow(
                () -> new InvalidTicketNotFoundException("Ticket with given id not found"));

    staffActionPolicy.assertSamePartnerAsExpected(
        staffContext.partner().id(), ticket.ownerPartner());

    return TicketStaffResult.fromDomain(ticket);
  }
}
