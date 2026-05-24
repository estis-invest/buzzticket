package com.efpcode.application.port.in.ticket;

import com.efpcode.application.usecase.ticket.dto.TicketResult;
import com.efpcode.application.usecase.ticket.dto.TicketSlugViewer;
import com.efpcode.application.usecase.ticket.dto.TicketViewer;
import com.efpcode.application.usecase.ticket.dto.TicketsResultsView;
import java.util.List;

public interface TicketViews {

  List<TicketsResultsView> getReportedTickets();

  TicketResult getReportedTicket(TicketViewer ticketViewer);

  List<TicketResult> getAssignedStaffTickets();

  List<TicketResult> getPartnerTickets();

  TicketResult getStaffTicket(TicketViewer ticketViewer);

  TicketResult getStaffTicketBySlug(TicketSlugViewer ticketSlugViewer);
}
