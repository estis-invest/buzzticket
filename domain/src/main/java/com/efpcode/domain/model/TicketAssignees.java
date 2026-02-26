package com.efpcode.domain.model;

import java.util.Set;

public record TicketAssignees(Set<UserId> workers) {

  private static final int LIMIT = 3;

  public TicketAssignees {
    if (workers == null) {
      workers = Set.of();
    }

    workers = Set.copyOf(workers);

    if (workers.size() > LIMIT) {
      throw new IllegalArgumentException("A ticket cannot have more then " + LIMIT + " workers");
    }
  }

  public static TicketAssignees empty() {
    return new TicketAssignees(Set.of());
  }
}
