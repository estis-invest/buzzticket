package com.efpcode.application.port.in.ticket;

import com.efpcode.application.usecase.ticket.dto.*;
import java.util.List;

public interface TicketViews {

  List<TicketsResultsView> getReportedTickets();

  TicketResult getReportedTicket(GetStaffTicketQuery getStaffTicketQuery);

  List<TicketStaffResult> getAssignedStaffTickets();

  List<TicketStaffResult> getPartnerTickets();

  TicketStaffResult getStaffTicket(GetStaffTicketQuery getStaffTicketQuery);

  TicketStaffResult getStaffTicketBySlug(GetStaffTicketsBySlugQuery getStaffTicketsBySlugQuery);
}
