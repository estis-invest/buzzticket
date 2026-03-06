package com.efpcode.domain.ticket.model;

import com.efpcode.domain.ticket.exceptions.IllegalTicketStatusAssignmentException;
import com.efpcode.domain.ticket.exceptions.IllegalTicketStatusTransitionException;

public enum TicketStatus {
  PENDING,
  OPEN,
  CLOSED,
  ARCHIVED;

  public TicketStatus open() {

    if (!isTransitionAllowed(OPEN)) invalidTransition(this, OPEN);
    return OPEN;
  }

  public TicketStatus close() {
    if (!isTransitionAllowed(CLOSED)) invalidTransition(this, CLOSED);
    return CLOSED;
  }

  public TicketStatus archive() {
    if (!isTransitionAllowed(ARCHIVED)) invalidTransition(this, ARCHIVED);
    return ARCHIVED;
  }

  public boolean isTransitionAllowed(TicketStatus targetStatus) {
    if (targetStatus == null) return false;
    return switch (targetStatus) {
      case OPEN -> this == PENDING;
      case CLOSED -> this == OPEN;
      case ARCHIVED -> this == CLOSED;
      default -> false;
    };
  }

  public boolean isTicketAssignable() {
    return switch (this) {
      case OPEN, PENDING -> true;
      default -> false;
    };
  }

  public boolean canChangeTicketPriority() {
    return switch (this) {
      case OPEN, PENDING -> true;
      default -> false;
    };
  }

  public void ticketStatusAssignGuard() {
    if (!isTicketAssignable()) {
      throw new IllegalTicketStatusAssignmentException(
          String.format("TicketStatus: %s cannot assign users", this));
    }
  }

  public void ticketChangeStatusPriorityGuard() {
    if (!canChangeTicketPriority())
      throw new IllegalTicketStatusAssignmentException(
          String.format("TicketPriority cannot be altered in status: %s", this));
  }

  private void invalidTransition(TicketStatus fromStatus, TicketStatus toStatus) {
    throw new IllegalTicketStatusTransitionException(
        String.format("TicketStatus cannot be transferred from %s to %s", fromStatus, toStatus));
  }
}
