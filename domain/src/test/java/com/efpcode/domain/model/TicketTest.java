package com.efpcode.domain.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TicketTest {
  @Test
  @DisplayName("Ticket created has default status Pending")
  void ticketCreatedHasDefaultStatusPending() {
    var ticket = new Ticket("Fix the broken build");
    assertThat(ticket.status()).isEqualTo(TicketStatus.PENDING);
  }
}
