package com.efpcode.domain.ticket.model;

import com.efpcode.domain.ticket.exceptions.MissingUserAssignmentException;
import com.efpcode.domain.ticket.exceptions.TicketAssigmentLimitException;
import com.efpcode.domain.user.model.UserId;
import java.util.HashSet;
import java.util.Set;

public record TicketAssignees(Set<UserId> workers) {

  private static final int LIMIT = 3;

  public TicketAssignees {
    if (workers == null) {
      workers = Set.of();
    }

    workers = Set.copyOf(workers);

    if (workers.size() > LIMIT) {
      throw new TicketAssigmentLimitException(
          "A ticket cannot have more than " + LIMIT + " workers");
    }
  }

  public TicketAssignees add(UserId userId) {
    if (userId == null) throw new MissingUserAssignmentException("UserId cannot be null");
    if (workers.contains(userId)) return this; // Already assigned, no change needed

    var newWorkers = new HashSet<>(workers);
    newWorkers.add(userId);
    return new TicketAssignees(newWorkers);
  }

  public TicketAssignees remove(UserId userId) {
    if (userId == null) throw new MissingUserAssignmentException("UserId cannot be null");

    if (!workers.contains(userId)) return this; // Not assigned, no change needed

    var newWorkers = new HashSet<>(workers);
    newWorkers.remove(userId);
    return new TicketAssignees(newWorkers);
  }

  public static TicketAssignees empty() {
    return new TicketAssignees(Set.of());
  }
}
