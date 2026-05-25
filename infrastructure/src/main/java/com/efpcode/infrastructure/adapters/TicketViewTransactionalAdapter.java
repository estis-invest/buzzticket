package com.efpcode.infrastructure.adapters;

import com.efpcode.application.context.RequestContext;
import com.efpcode.application.port.in.ticket.TicketViews;
import com.efpcode.application.usecase.ticket.*;
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
  private final GetPartnerTicketsForStaffUseCase getPartnerTicketsForStaffUseCase;
  private final GetStaffTicketUseCase getStaffTicketUseCase;
  private final GetStaffTicketSlugUseCase getStaffTicketSlugUseCase;

  public TicketViewTransactionalAdapter(
      RequestContext requestContext,
      GetReportedTicketsUseCase getReportedTicketsUseCase,
      GetReportedTicketUseCase getReportedTicketUseCase,
      GetAssignedStaffTicketsUseCase getAssignedStaffTicketsUseCase,
      GetPartnerTicketsForStaffUseCase getPartnerTicketsForStaffUseCase,
      GetStaffTicketUseCase getStaffTicketUseCase,
      GetStaffTicketSlugUseCase getStaffTicketSlugUseCase) {
    this.requestContext = requestContext;
    this.getReportedTicketsUseCase = getReportedTicketsUseCase;
    this.getReportedTicketUseCase = getReportedTicketUseCase;
    this.getAssignedStaffTicketsUseCase = getAssignedStaffTicketsUseCase;
    this.getPartnerTicketsForStaffUseCase = getPartnerTicketsForStaffUseCase;
    this.getStaffTicketUseCase = getStaffTicketUseCase;
    this.getStaffTicketSlugUseCase = getStaffTicketSlugUseCase;
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
  @Transactional(readOnly = true)
  public List<TicketStaffResult> getPartnerTickets() {
    return getPartnerTicketsForStaffUseCase.execute(requestContext);
  }

  @Override
  @Transactional(readOnly = true)
  public TicketStaffResult getStaffTicket(TicketViewer ticketViewer) {
    return getStaffTicketUseCase.execute(ticketViewer, requestContext);
  }

  @Override
  @Transactional(readOnly = true)
  public TicketStaffResult getStaffTicketBySlug(TicketSlugViewer ticketSlugViewer) {
    return getStaffTicketSlugUseCase.execute(ticketSlugViewer, requestContext);
  }
}
