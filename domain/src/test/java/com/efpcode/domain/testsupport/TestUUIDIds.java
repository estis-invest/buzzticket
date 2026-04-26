package com.efpcode.domain.testsupport;

import com.efpcode.domain.ticket.model.TicketId;
import java.util.UUID;

public class TestUUIDIds {
  private TestUUIDIds() {}

  public static TicketId ticketId() {
    return TicketId.of(UUID.randomUUID());
  }

  /* -------- Fixed IDs (deterministic tests) -------- */

  public static TicketId ticketId(String uuid) {
    return TicketId.of(UUID.fromString(uuid));
  }
}
