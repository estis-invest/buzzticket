package com.efpcode.domain.testsupport;

import com.efpcode.domain.partner.model.PartnerId;
import com.efpcode.domain.ticket.model.TicketId;
import com.efpcode.domain.user.model.UserId;
import java.util.UUID;

public class TestUUIDIds {
  private TestUUIDIds() {}

  public static TicketId ticketId() {
    return TicketId.of(UUID.randomUUID());
  }

  public static PartnerId partnerId() {
    return PartnerId.of(UUID.randomUUID());
  }

  public static UserId userId() {
    return UserId.of(UUID.randomUUID());
  }

  /* -------- Fixed IDs (deterministic tests) -------- */

  public static TicketId ticketId(String uuid) {
    return TicketId.of(UUID.fromString(uuid));
  }

  public static PartnerId partnerId(String uuid) {
    return PartnerId.of(UUID.fromString(uuid));
  }

  public static UserId userId(String uuid) {
    return UserId.of(UUID.fromString(uuid));
  }
}
