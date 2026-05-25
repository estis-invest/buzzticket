package com.efpcode.infrastructure.web.dto.responses;

import com.efpcode.application.usecase.ticket.dto.TicketStaffResult;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public record TicketStaffResponse(
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

  public static TicketStaffResponse fromResult(TicketStaffResult staffResult) {
    return new TicketStaffResponse(
        staffResult.ticketId(),
        staffResult.slug(),
        staffResult.title(),
        staffResult.title(),
        staffResult.status(),
        staffResult.priority(),
        staffResult.createdAt(),
        staffResult.updatedAt(),
        staffResult.assignees(),
        staffResult.reportedBy());
  }
}
