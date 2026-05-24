package com.efpcode.infrastructure.adapters;

import com.efpcode.application.context.RequestContext;
import com.efpcode.application.port.in.ticket.TicketViews;
import com.efpcode.application.usecase.ticket.GetReportedTicketsUseCase;
import com.efpcode.application.usecase.ticket.dto.TicketResult;
import com.efpcode.application.usecase.ticket.dto.TicketSlugViewer;
import com.efpcode.application.usecase.ticket.dto.TicketViewer;
import com.efpcode.application.usecase.ticket.dto.TicketsResultsView;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TicketViewTransactionalAdapter implements TicketViews {
  private final RequestContext requestContext;
  private final GetReportedTicketsUseCase getReportedTicketsUseCase;

  public TicketViewTransactionalAdapter(
      RequestContext requestContext, GetReportedTicketsUseCase getReportedTicketsUseCase) {
    this.requestContext = requestContext;
    this.getReportedTicketsUseCase = getReportedTicketsUseCase;
  }

  @Override
  @Transactional(readOnly = true)
  public List<TicketsResultsView> getReportedTickets() {
    return getReportedTicketsUseCase.execute(requestContext);
  }

  @Override
  public TicketResult getReportedTicket(TicketViewer ticketViewer) {
    return null;
  }

  @Override
  public List<TicketResult> getAssignedStaffTickets() {
    return List.of();
  }

  @Override
  public List<TicketResult> getPartnerTickets() {
    return List.of();
  }

  @Override
  public TicketResult getStaffTicket(TicketViewer ticketViewer) {
    return null;
  }

  @Override
  public TicketResult getStaffTicketBySlug(TicketSlugViewer ticketSlugViewer) {
    return null;
  }
}
