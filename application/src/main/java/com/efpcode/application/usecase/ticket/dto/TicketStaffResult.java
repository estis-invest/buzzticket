package com.efpcode.application.usecase.ticket.dto;

import com.efpcode.domain.ticket.model.Ticket;
import com.efpcode.domain.user.model.UserId;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record TicketStaffResult(
    UUID ticketId,
    String slug,
    String title,
    String description,
    String status,
    String priority,
    Instant createdAt,
    Instant updatedAt,
    Set<UUID> assignees,
    UUID reportedBy) {

  public static TicketStaffResult fromDomain(Ticket ticket) {
    Set<UUID> assignees =
        ticket.workers().workers().stream().map(UserId::id).collect(Collectors.toSet());

    return new TicketStaffResult(
        ticket.id().ticketId(),
        ticket.slug().slug(),
        ticket.title().title(),
        ticket.description().description(),
        ticket.status().name(),
        ticket.priority().name(),
        ticket.createdAt().time(),
        ticket.updatedAt().updatedAt(),
        assignees,
        ticket.reportedBy().id());
  }
}
