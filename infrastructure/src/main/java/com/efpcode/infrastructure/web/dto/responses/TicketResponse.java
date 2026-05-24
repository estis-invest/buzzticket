package com.efpcode.infrastructure.web.dto.responses;

import com.efpcode.application.usecase.ticket.dto.TicketResult;
import java.time.Instant;
import java.util.UUID;

public record TicketResponse(
    UUID ticketId,
    String slug,
    String title,
    String description,
    String status,
    String priority,
    String partnerName,
    Instant createdAt,
    Instant updatedAt) {

  public static TicketResponse fromResult(TicketResult ticketResult) {
    return new TicketResponse(
        ticketResult.ticketId(),
        ticketResult.slug(),
        ticketResult.title(),
        ticketResult.description(),
        ticketResult.status(),
        ticketResult.priority(),
        ticketResult.partnerName(),
        ticketResult.createdAt(),
        ticketResult.updatedAt());
  }
}
