package com.efpcode.domain.ticket.model;

import com.efpcode.domain.user.model.UserId;
import com.efpcode.domain.user.model.UserRole;
import java.util.Objects;

public record Ticket(
    TicketId id,
    TicketSlug slug,
    TicketTitle title,
    TicketDescription description,
    TicketStatus status,
    TicketPriority priority,
    TicketCreatedAt time,
    TicketAssignees workers,
    UserId reportedBy) {

  public Ticket {
    Objects.requireNonNull(id, "TicketId cannot be null");
    Objects.requireNonNull(slug, "TicketSlug cannot be null");
    Objects.requireNonNull(title, "TicketTitle cannot be null");
    Objects.requireNonNull(description, "TicketDescription cannot be null");
    Objects.requireNonNull(status, "TicketStatus cannot be null");
    Objects.requireNonNull(priority, "TicketPriority cannot be null");
    Objects.requireNonNull(time, "TicketCreatedAt cannot be null");
    Objects.requireNonNull(workers, "TicketAssignees cannot be null");
    Objects.requireNonNull(reportedBy, "UserId cannot be null");
  }

  public Ticket open() {
    return withStatus(this.status.open());
  }

  public Ticket close() {
    return withStatus(this.status().close());
  }

  public Ticket archive() {
    return withStatus(this.status.archive());
  }

  public static Ticket createPending(
      TicketId id,
      TicketSlug slug,
      TicketTitle title,
      TicketDescription description,
      TicketPriority priority,
      TicketCreatedAt time,
      UserId reportedBy) {

    return new Ticket(
        id,
        slug,
        title,
        description,
        TicketStatus.PENDING,
        priority,
        time,
        TicketAssignees.empty(),
        reportedBy);
  }

  public Ticket assign(UserId staffId, UserRole actorRole) {
    actorRole.roleGuardAssignTickets();
    this.status.ticketStatusAssignGuard();

    return new Ticket(
        id, slug, title, description, status, priority, time, workers.add(staffId), reportedBy);
  }

  public Ticket withPriority(TicketPriority ticketPriority) {
    return new Ticket(
        id, slug, title, description, status, ticketPriority, time, workers, reportedBy);
  }

  private Ticket withStatus(TicketStatus newStatus) {
    return new Ticket(id, slug, title, description, newStatus, priority, time, workers, reportedBy);
  }
}
