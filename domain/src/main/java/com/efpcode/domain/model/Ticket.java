package com.efpcode.domain.model;

import java.util.Objects;

public record Ticket(
    TicketId id,
    TicketSlug slug,
    TicketTitle title,
    TicketDescription description,
    TicketStatus status,
    TicketCreatAt time,
    TicketAssignees workers,
    UserId reportedBy) {

  public Ticket {
    Objects.requireNonNull(id, "TicketId cannot be null");
    Objects.requireNonNull(slug, "TicketSlug cannot be null");
    Objects.requireNonNull(title, "TicketTitle cannot be null");
    Objects.requireNonNull(description, "TicketDescription cannot be null");
    Objects.requireNonNull(status, "TicketStatus cannot be null");
    Objects.requireNonNull(time, "TicketCreatAt cannot be null");
    Objects.requireNonNull(workers, "TicketAssignees cannot be null");
    Objects.requireNonNull(reportedBy, "UserId cannot be null");
  }

  public Ticket open() {
    if (this.status != TicketStatus.PENDING) {
      return this;
    }

    return new Ticket(
        this.id,
        this.slug,
        this.title,
        this.description,
        TicketStatus.OPEN,
        this.time,
        this.workers,
        this.reportedBy);
  }

  public Ticket close() {

    if (this.status != TicketStatus.OPEN) {
      return this;
    }
    return new Ticket(
        this.id,
        this.slug,
        this.title,
        this.description,
        TicketStatus.CLOSED,
        this.time,
        this.workers,
        this.reportedBy);
  }

  public Ticket archive() {

    if (this.status != TicketStatus.CLOSED) {
      return this;
    }
    return new Ticket(
        this.id,
        this.slug,
        this.title,
        this.description,
        TicketStatus.ARCHIVED,
        this.time,
        this.workers,
        this.reportedBy);
  }

  public static Ticket createPending(
      TicketId id,
      TicketSlug slug,
      TicketTitle title,
      TicketDescription description,
      TicketCreatAt time,
      UserId reportedBy) {

    return new Ticket(
        id,
        slug,
        title,
        description,
        TicketStatus.PENDING,
        time,
        TicketAssignees.empty(),
        reportedBy);
  }
}
