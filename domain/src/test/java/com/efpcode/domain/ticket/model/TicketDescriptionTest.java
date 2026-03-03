package com.efpcode.domain.ticket.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.efpcode.domain.ticket.exceptions.InvalidTicketDescriptionException;
import com.efpcode.domain.ticket.exceptions.TicketDescriptionLengthException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TicketDescriptionTest {
  @Test
  @DisplayName("TicketDescription cannot pass null or blank")
  void ticketDescriptionCannotPassNullOrBlank() {
    assertThatThrownBy(() -> new TicketDescription("    "))
        .isInstanceOf(InvalidTicketDescriptionException.class)
        .hasMessageContaining("be empty");

    assertThatThrownBy(() -> new TicketDescription(null))
        .isInstanceOf(InvalidTicketDescriptionException.class)
        .hasMessageContaining("be empty");
  }

  @Test
  @DisplayName("TicketDescription has max length and surpassed throws error")
  void ticketDescriptionHasMaxLengthAndSurpassedThrowsError() {

    var longText = "a".repeat(1801);

    assertThatThrownBy(() -> new TicketDescription(longText))
        .isInstanceOf(TicketDescriptionLengthException.class)
        .hasMessageContaining("exceeds max length");
  }

  @Test
  @DisplayName("TicketDescription can be created if length is under max length")
  void ticketDescriptionCanBeCreatedIfLengthIsUnderMaxLength() {
    var lengthOfText = 1800;
    var longText = "a".repeat(lengthOfText);
    var ticketDescription = new TicketDescription(longText);

    assertThat(ticketDescription.description()).isEqualTo(longText);
    assertThat(ticketDescription.description()).isNotBlank().isNotNull();
    assertThat(ticketDescription.description()).hasSameSizeAs(longText);
  }
}
