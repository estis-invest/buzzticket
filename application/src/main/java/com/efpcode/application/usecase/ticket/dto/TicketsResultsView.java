package com.efpcode.application.usecase.ticket.dto;

import com.efpcode.domain.ticket.model.Ticket;
import java.time.Instant;
import java.util.UUID;

public record TicketsResultsView(
    UUID ticketId,
    String slug,
    String title,
    String description,
    String status,
    String priority,
    UUID ownerPartnerId,
    Instant createdAt,
    Instant updatedAt) {

  public static TicketsResultsView fromDomain(Ticket ticket) {
    return new TicketsResultsView(
        ticket.id().ticketId(),
        ticket.slug().slug(),
        ticket.title().title(),
        ticket.description().description(),
        ticket.status().name(),
        ticket.priority().name(),
        ticket.ownerPartner().partnerId(),
        ticket.createdAt().time(),
        ticket.updatedAt().updatedAt());
  }
}
