package com.efpcode.application.usecase.ticket;

import com.efpcode.application.context.RequestContext;
import com.efpcode.application.policy.staff.StaffActionPolicy;
import com.efpcode.application.policy.staff.dto.StaffContext;
import com.efpcode.application.usecase.ticket.dto.TicketStaffResult;
import com.efpcode.domain.ticket.model.Ticket;
import com.efpcode.domain.ticket.port.TicketRepository;
import java.util.List;

public class GetPartnerTicketsForStaffUseCase {

  private final TicketRepository ticketRepository;
  private final StaffActionPolicy staffActionPolicy;

  public GetPartnerTicketsForStaffUseCase(
      TicketRepository ticketRepository, StaffActionPolicy staffActionPolicy) {
    this.ticketRepository = ticketRepository;
    this.staffActionPolicy = staffActionPolicy;
  }

  public List<TicketStaffResult> execute(RequestContext requestContext) {
    StaffContext staffContext = staffActionPolicy.staffValidator(requestContext);
    List<Ticket> partnerTickets = ticketRepository.findByOwnerPartner(staffContext.partner().id());
    return partnerTickets.stream().map(TicketStaffResult::fromDomain).toList();
  }
}
