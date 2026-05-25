package com.efpcode.infrastructure.adapters;

import com.efpcode.application.context.RequestContext;
import com.efpcode.application.port.in.ticket.TicketViews;
import com.efpcode.application.usecase.ticket.GetAssignedStaffTicketsUseCase;
import com.efpcode.application.usecase.ticket.GetReportedTicketUseCase;
import com.efpcode.application.usecase.ticket.GetReportedTicketsUseCase;
import com.efpcode.application.usecase.ticket.dto.*;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TicketViewTransactionalAdapter implements TicketViews {
  private final RequestContext requestContext;
  private final GetReportedTicketsUseCase getReportedTicketsUseCase;
  private final GetReportedTicketUseCase getReportedTicketUseCase;
  private final GetAssignedStaffTicketsUseCase getAssignedStaffTicketsUseCase;

  public TicketViewTransactionalAdapter(
      RequestContext requestContext,
      GetReportedTicketsUseCase getReportedTicketsUseCase,
      GetReportedTicketUseCase getReportedTicketUseCase,
      GetAssignedStaffTicketsUseCase getAssignedStaffTicketsUseCase) {
    this.requestContext = requestContext;
    this.getReportedTicketsUseCase = getReportedTicketsUseCase;
    this.getReportedTicketUseCase = getReportedTicketUseCase;
    this.getAssignedStaffTicketsUseCase = getAssignedStaffTicketsUseCase;
  }

  @Override
  @Transactional(readOnly = true)
  public List<TicketsResultsView> getReportedTickets() {
    return getReportedTicketsUseCase.execute(requestContext);
  }

  @Override
  @Transactional(readOnly = true)
  public TicketResult getReportedTicket(TicketViewer ticketViewer) {
    return getReportedTicketUseCase.execute(ticketViewer, requestContext);
  }

  @Override
  @Transactional(readOnly = true)
  public List<TicketStaffResult> getAssignedStaffTickets() {
    return getAssignedStaffTicketsUseCase.execute(requestContext);
  }

  @Override
  public List<TicketStaffResult> getPartnerTickets() {
    return List.of();
  }

  @Override
  public TicketStaffResult getStaffTicket(TicketViewer ticketViewer) {
    return null;
  }

  @Override
  public TicketStaffResult getStaffTicketBySlug(TicketSlugViewer ticketSlugViewer) {
    return null;
  }
}
