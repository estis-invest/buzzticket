package com.efpcode.infrastructure.web.dto.responses;

import com.efpcode.application.usecase.ticket.dto.TicketsResultsView;
import java.time.Instant;
import java.util.UUID;

public record TicketViewResponse(
    UUID ticketId,
    String slug,
    String title,
    String description,
    String status,
    String priority,
    UUID ownerPartner,
    Instant createdAt,
    Instant updatedAt) {

  public static TicketViewResponse fromResult(TicketsResultsView tickets) {
    return new TicketViewResponse(
        tickets.ticketId(),
        tickets.slug(),
        tickets.title(),
        tickets.description(),
        tickets.status(),
        tickets.priority(),
        tickets.ownerPartnerId(),
        tickets.createdAt(),
        tickets.updatedAt());
  }
}
