package com.efpcode.domain.ticket.model;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.efpcode.domain.ticket.exceptions.InvalidTicketTitleException;
import com.efpcode.domain.ticket.exceptions.TicketTitleLengthException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TicketTitleTest {
  @Test
  @DisplayName("TicketTitle cannot pass null or blank")
  void ticketTitleCannotPassNullOrBlank() {
    assertThatThrownBy(() -> new TicketTitle("  "))
        .isInstanceOf(InvalidTicketTitleException.class)
        .hasMessageContaining("be blank");

    assertThatThrownBy(() -> new TicketTitle(null))
        .isInstanceOf(InvalidTicketTitleException.class)
        .hasMessageContaining("or null");
  }

  @Test
  @DisplayName("TicketTitle has max length if surpass throws error")
  void ticketTitleHasMaxLengthIfSurpassThrowsError() {

    var longTitle = "a".repeat(51);

    assertThatThrownBy(() -> new TicketTitle(longTitle))
        .isInstanceOf(TicketTitleLengthException.class)
        .hasMessageContaining("Max length");
  }
}
