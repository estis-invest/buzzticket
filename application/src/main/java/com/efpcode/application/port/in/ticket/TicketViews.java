package com.efpcode.application.port.in.ticket;

import com.efpcode.application.usecase.ticket.dto.*;
import java.util.List;

public interface TicketViews {

  List<TicketsResultsView> getReportedTickets();

  TicketResult getReportedTicket(TicketViewer ticketViewer);

  List<TicketStaffResult> getAssignedStaffTickets();

  List<TicketStaffResult> getPartnerTickets();

  TicketStaffResult getStaffTicket(TicketViewer ticketViewer);

  TicketStaffResult getStaffTicketBySlug(TicketSlugViewer ticketSlugViewer);
}
