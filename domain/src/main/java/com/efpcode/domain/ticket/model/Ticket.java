package com.efpcode.domain.ticket.model;

import com.efpcode.domain.partner.model.PartnerId;
import com.efpcode.domain.ticket.exceptions.*;
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

    if (createdAt.time().isAfter(updatedAt.updatedAt())) {
      throw new InvalidTicketException("Ticket updatedAt cannot be before the createdAt time");
    }

    if (status != TicketStatus.PENDING && workers.isEmpty()) {
      throw new IllegalTicketStatusTransitionException(
          "Ticket must have at least one worker when status is: " + status.name());
    }
  }

  public Ticket open(TicketUpdateAt updateAt) {
    return withStatus(this.status.open(), updateAt);
  }

  public Ticket close(TicketUpdateAt updateAt) {
    return withStatus(this.status().close(), updateAt);
  }

  public Ticket archive(TicketUpdateAt updateAt) {
    return withStatus(this.status.archive(), updateAt);
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
    if (time == null) {
      throw new InvalidTicketException("CreatedAt time cannot be null");
    }

    return new Ticket(
        id,
        slug,
        title,
        description,
        TicketStatus.PENDING,
        priority,
        time,
        TicketUpdateAt.of(time.time()),
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
        updateAt,
        workers.add(staffId),
        reportedBy,
        ownerPartner);
  }

  public Ticket unassign(UserId staffId, UserRole actorRole, TicketUpdateAt updateAt) {
    if (staffId == null || actorRole == null || updateAt == null)
      throw new IllegalTicketAssignmentException("TicketUnassign method cannot pass null!");

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
        updateAt,
        workers.remove(staffId),
        reportedBy,
        ownerPartner);
  }

  public Ticket withPriority(TicketPriority ticketPriority, TicketUpdateAt updateAt) {
    if (ticketPriority == null || updateAt == null)
      throw new IllegalTicketPriorityException(
          "Ticket priority or update time passed cannot be null");
    this.status().ticketChangeStatusPriorityGuard();
    return new Ticket(
        id,
        slug,
        title,
        description,
        status,
        ticketPriority,
        createdAt,
        updateAt,
        workers,
        reportedBy,
        ownerPartner);
  }

  public Ticket updateTicketDescription(TicketDescription newDescription, TicketUpdateAt updateAt) {

    if (newDescription == null || updateAt == null) {
      throw new IllegalTicketDescriptionUpdateException(
          "Ticket description or update time passed cannot be null");
    }
    this.status.ticketUpdateDescriptionGuard();

    return new Ticket(
        id,
        slug,
        title,
        newDescription,
        status,
        priority,
        createdAt,
        updateAt,
        workers,
        reportedBy,
        ownerPartner);
  }

  private Ticket withStatus(TicketStatus newStatus, TicketUpdateAt updateAt) {
    if (updateAt == null) {
      throw new IllegalTicketStatusTransitionException("Update time is required");
    }
    return new Ticket(
        id,
        slug,
        title,
        description,
        newStatus,
        priority,
        createdAt,
        updateAt,
        workers,
        reportedBy,
        ownerPartner);
  }
}
