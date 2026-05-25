package com.efpcode.application.usecase.ticket;

import com.efpcode.application.context.RequestContext;
import com.efpcode.application.policy.staff.StaffActionPolicy;
import com.efpcode.application.policy.staff.dto.StaffContext;
import com.efpcode.application.usecase.ticket.dto.TicketSlugViewer;
import com.efpcode.application.usecase.ticket.dto.TicketStaffResult;
import com.efpcode.application.usecase.ticket.exceptions.InvalidTicketSlugException;
import com.efpcode.domain.ticket.model.Ticket;
import com.efpcode.domain.ticket.model.TicketSlug;
import com.efpcode.domain.ticket.port.TicketRepository;

public class GetStaffTicketSlugUseCase {

  private final TicketRepository ticketRepository;
  private final StaffActionPolicy staffActionPolicy;

  public GetStaffTicketSlugUseCase(
      TicketRepository ticketRepository, StaffActionPolicy staffActionPolicy) {
    this.ticketRepository = ticketRepository;
    this.staffActionPolicy = staffActionPolicy;
  }

  public TicketStaffResult execute(TicketSlugViewer slugViewer, RequestContext requestContext) {
    StaffContext context = staffActionPolicy.staffValidator(requestContext);

    TicketSlug slug = new TicketSlug(slugViewer.slug());

    Ticket ticket =
        ticketRepository
            .findBySlug(slug)
            .orElseThrow(
                () -> new InvalidTicketSlugException("Slug provided does not match any tickets"));

    staffActionPolicy.assertSamePartnerAsExpected(context.partner().id(), ticket.ownerPartner());

    return TicketStaffResult.fromDomain(ticket);
  }
}
