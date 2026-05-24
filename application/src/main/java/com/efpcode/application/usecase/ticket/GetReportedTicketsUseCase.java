package com.efpcode.application.usecase.ticket;

import com.efpcode.application.context.RequestContext;
import com.efpcode.application.policy.user.UserAuthenticationPolicy;
import com.efpcode.application.policy.user.dto.UserContext;
import com.efpcode.application.usecase.ticket.dto.TicketsResultsView;
import com.efpcode.domain.ticket.exceptions.InvalidTicketException;
import com.efpcode.domain.ticket.model.Ticket;
import com.efpcode.domain.ticket.port.TicketRepository;
import java.util.List;

public class GetReportedTicketsUseCase {

  private final TicketRepository ticketRepository;
  private final UserAuthenticationPolicy userAuthenticationPolicy;

  public GetReportedTicketsUseCase(
      TicketRepository ticketRepository, UserAuthenticationPolicy userAuthenticationPolicy) {
    this.ticketRepository = ticketRepository;
    this.userAuthenticationPolicy = userAuthenticationPolicy;
  }

  public List<TicketsResultsView> execute(RequestContext requestContext) {
    UserContext userContext = userAuthenticationPolicy.userValidator(requestContext);

    List<Ticket> tickets = ticketRepository.findByReportedBy(userContext.user().id());

    if (tickets == null || tickets.isEmpty()) {
      throw new InvalidTicketException("No tickets are associated with user id");
    }

    return tickets.stream().map(TicketsResultsView::fromDomain).toList();
  }
}
