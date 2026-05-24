package com.efpcode.application.usecase.ticket;

import com.efpcode.application.context.RequestContext;
import com.efpcode.application.policy.user.UserAuthenticationPolicy;
import com.efpcode.application.policy.user.dto.UserContext;
import com.efpcode.application.usecase.partner.exceptions.PartnerNotFoundException;
import com.efpcode.application.usecase.ticket.dto.TicketResult;
import com.efpcode.application.usecase.ticket.dto.TicketViewer;
import com.efpcode.application.usecase.ticket.exceptions.InvalidTicketAuthorizationException;
import com.efpcode.application.usecase.ticket.exceptions.InvalidTicketNotFoundException;
import com.efpcode.domain.partner.model.Partner;
import com.efpcode.domain.partner.port.PartnerRepository;
import com.efpcode.domain.ticket.model.Ticket;
import com.efpcode.domain.ticket.model.TicketId;
import com.efpcode.domain.ticket.port.TicketRepository;

public class GetReportedTicketUseCase {
  private final TicketRepository ticketRepository;
  private final PartnerRepository partnerRepository;
  private final UserAuthenticationPolicy userAuthenticationPolicy;

  public GetReportedTicketUseCase(
      TicketRepository ticketRepository,
      PartnerRepository partnerRepository,
      UserAuthenticationPolicy userAuthenticationPolicy) {
    this.ticketRepository = ticketRepository;
    this.partnerRepository = partnerRepository;

    this.userAuthenticationPolicy = userAuthenticationPolicy;
  }

  public TicketResult execute(TicketViewer viewer, RequestContext requestContext) {
    UserContext userContext = userAuthenticationPolicy.userValidator(requestContext);
    TicketId ticketId = TicketId.of(viewer.ticketId());

    Ticket ticket =
        ticketRepository
            .findById(ticketId)
            .orElseThrow(() -> new InvalidTicketNotFoundException("Ticket not found"));

    boolean isSelf = userContext.user().id().equals(ticket.reportedBy());

    if (!isSelf) {
      throw new InvalidTicketAuthorizationException("Ticket view forbidden");
    }

    Partner partner =
        partnerRepository
            .findById(ticket.ownerPartner())
            .orElseThrow(() -> new PartnerNotFoundException("Partner for ticket not found"));

    return TicketResult.fromDomain(ticket, partner.name(), partner.id());
  }
}
