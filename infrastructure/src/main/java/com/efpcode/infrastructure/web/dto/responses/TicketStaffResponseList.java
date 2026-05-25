package com.efpcode.infrastructure.web.dto.responses;

import java.util.List;

public record TicketStaffResponseList(List<TicketStaffResponse> ticketStaffResponses, int total) {}
