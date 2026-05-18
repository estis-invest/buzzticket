package com.efpcode.domain.ticket.model;

import com.efpcode.domain.partner.model.PartnerId;
import com.efpcode.domain.ticket.exceptions.IllegalTicketAssignmentException;
import com.efpcode.domain.ticket.exceptions.IllegalTicketPriorityException;
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
    TicketCreatedAt createdAt,
    TicketUpdateAt updatedAt,
    TicketAssignees workers,
    UserId reportedBy,
    PartnerId ownerPartner) {

  public Ticket {
    Objects.requireNonNull(id, "TicketId cannot be null");
    Objects.requireNonNull(slug, "TicketSlug cannot be null");
    Objects.requireNonNull(title, "TicketTitle cannot be null");
    Objects.requireNonNull(description, "TicketDescription cannot be null");
    Objects.requireNonNull(status, "TicketStatus cannot be null");
    Objects.requireNonNull(priority, "TicketPriority cannot be null");
    Objects.requireNonNull(createdAt, "TicketCreatedAt cannot be null");
    Objects.requireNonNull(updatedAt, "TicketUpdateAt cannot be null");
    Objects.requireNonNull(workers, "TicketAssignees cannot be null");
    Objects.requireNonNull(reportedBy, "UserId cannot be null");
    Objects.requireNonNull(ownerPartner, "PartnerId cannot be null");
  }

  public Ticket open() {
    return withStatus(this.status.open(), updatedAt);
  }

  public Ticket close() {
    return withStatus(this.status().close(), updatedAt);
  }

  public Ticket archive() {
    return withStatus(this.status.archive(), updatedAt);
  }

  public static Ticket createPending(
      TicketId id,
      TicketSlug slug,
      TicketTitle title,
      TicketDescription description,
      TicketPriority priority,
      TicketCreatedAt time,
      UserId reportedBy,
      PartnerId ownerPartner) {

    return new Ticket(
        id,
        slug,
        title,
        description,
        TicketStatus.PENDING,
        priority,
        time,
        new TicketUpdateAt(time.time()),
        TicketAssignees.empty(),
        reportedBy,
        ownerPartner);
  }

  public Ticket assign(UserId staffId, UserRole actorRole, TicketUpdateAt updateAt) {
    if (staffId == null || actorRole == null || updateAt == null)
      throw new IllegalTicketAssignmentException("TicketAssign method cannot pass null!");

    actorRole.roleGuardAssignTickets();
    this.status.ticketStatusAssignGuard();

    return new Ticket(
        id,
        slug,
        title,
        description,
        status,
        priority,
        createdAt,
        updatedAt,
        workers.add(staffId),
        reportedBy,
        ownerPartner);
  }

  public Ticket withPriority(TicketPriority ticketPriority, TicketUpdateAt updateAt) {
    if (ticketPriority == null)
      throw new IllegalTicketPriorityException("Ticket priority passed cannot be null");
    this.status().ticketChangeStatusPriorityGuard();
    return new Ticket(
        id,
        slug,
        title,
        description,
        status,
        ticketPriority,
        createdAt,
        updatedAt,
        workers,
        reportedBy,
        ownerPartner);
  }

  private Ticket withStatus(TicketStatus newStatus, TicketUpdateAt updateAt) {
    return new Ticket(
        id,
        slug,
        title,
        description,
        newStatus,
        priority,
        createdAt,
        updatedAt,
        workers,
        reportedBy,
        ownerPartner);
  }
}
