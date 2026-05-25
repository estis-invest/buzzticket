package com.efpcode.infrastructure.web.dto.responses;

import java.util.List;

public record TicketsViewListResponse(List<TicketViewResponse> tickets, int total) {}
