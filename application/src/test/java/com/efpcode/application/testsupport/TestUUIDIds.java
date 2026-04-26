package com.efpcode.application.testsupport;

import com.efpcode.domain.partner.model.PartnerId;
import com.efpcode.domain.ticket.model.TicketId;
import java.util.UUID;

public class TestUUIDIds {
  private TestUUIDIds() {}

  public static TicketId ticketId() {
    return TicketId.of(UUID.randomUUID());
  }

  public static PartnerId partnerId() {
    return PartnerId.of(UUID.randomUUID());
  }

  /* -------- Fixed IDs (deterministic tests) -------- */

  public static TicketId ticketId(String uuid) {
    return TicketId.of(UUID.fromString(uuid));
  }

  public static PartnerId partnerId(String uuid) {
    return PartnerId.of(UUID.fromString(uuid));
  }
}
