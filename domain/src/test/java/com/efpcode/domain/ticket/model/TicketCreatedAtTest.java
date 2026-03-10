package com.efpcode.domain.ticket.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.efpcode.domain.ticket.exceptions.InvalidCreatedAtException;
import com.efpcode.domain.ticket.exceptions.InvalidTicketDateException;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TicketCreatedAtTest {
  @Test
  @DisplayName("TicketCreateAt cannot pass null value or zero userCreatedAt")
  void ticketCreateAtCannotPassNullValueOrZeroTime() {

    assertThatThrownBy(() -> new TicketCreatedAt(null))
        .isInstanceOf(InvalidCreatedAtException.class)
        .hasMessageContaining("Time is required");

    var emptyTime = Instant.ofEpochMilli(0);

    assertThatThrownBy(() -> new TicketCreatedAt(emptyTime))
        .isInstanceOf(InvalidCreatedAtException.class)
        .hasMessageContaining("Time is required");
  }

  @Test
  @DisplayName("Time cannot be created in the future")
  void timeCannotBeCreatedInTheFuture() {

    var futureTime = Instant.now().plusSeconds(90);

    assertThatThrownBy(() -> new TicketCreatedAt(futureTime))
        .isInstanceOf(InvalidTicketDateException.class)
        .hasMessageContaining("the future");
  }

  @Test
  @DisplayName("Time has createNow method that returns new objects")
  void timeHasCreateNowMethodThatReturnsNewObjects() {

    var pastTime = TicketCreatedAt.createNow();
    var currentTime = TicketCreatedAt.createNow();

    assertThat(pastTime).isNotSameAs(currentTime);
  }

  @Test
  @DisplayName("Time has createNow method and returns valid objects")
  void timeHasCreateNowMethodAndReturnsValidObjects() {

    var now = TicketCreatedAt.createNow();
    assertThat(now.time()).isNotNull();
    assertThat(now.time()).isBeforeOrEqualTo(Instant.now());
  }
}
