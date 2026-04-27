package com.efpcode.domain.ticket.model;

import static org.assertj.core.api.Assertions.*;

import com.efpcode.domain.ticket.exceptions.InvalidTicketUpdateAtException;
import com.efpcode.domain.ticket.exceptions.TicketUpdateAtDateException;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TicketUpdateAtTest {

  @Test
  @DisplayName("TicketUpdateAt cannot be null")
  void ticketUpdateAtCannotBeNull() {
    assertThatThrownBy(() -> new TicketUpdateAt(null))
        .isInstanceOf(InvalidTicketUpdateAtException.class)
        .hasMessageContaining("Update timestamp is required");
  }

  @Test
  @DisplayName("TicketUpdateAt cannot pass zero updatedAt as argument")
  void ticketUpdateAtCannotPassZeroUpdatedAtAsArgument() {
    var timeZero = Instant.ofEpochMilli(0);
    assertThatThrownBy(() -> new TicketUpdateAt(timeZero))
        .isInstanceOf(InvalidTicketUpdateAtException.class)
        .hasMessageContaining("Update timestamp is required");
  }

  @Test
  @DisplayName("TicketUpdateAt cannot be in the future beyond margin of error")
  void ticketUpdateAtCannotBeInTheFutureBeyondMarginOfError() {
    var futureTime = Instant.now().plusSeconds(90);
    assertThatThrownBy(() -> new TicketUpdateAt(futureTime))
        .isInstanceOf(TicketUpdateAtDateException.class)
        .hasMessageContaining("Update timestamp cannot be in the future");
  }

  @Test
  @DisplayName("TicketUpdateAt within grace period returns a valid object")
  void ticketUpdateAtWithinGracePeriodReturnsAValidObject() {
    var slightFutureInTime = Instant.now().plusSeconds(30);
    var result = new TicketUpdateAt(slightFutureInTime);
    assertThat(result).isNotNull().isInstanceOf(TicketUpdateAt.class);
    assertThat(result.updatedAt()).isEqualTo(slightFutureInTime);
  }

  @Test
  @DisplayName("TicketUpdateAt created with current updatedAt returns a valid object")
  void ticketUpdateAtWithInstantNowReturnsAValidObject() {
    var now = Instant.now();
    var result = new TicketUpdateAt(now);
    assertThat(result).isNotNull().isInstanceOf(TicketUpdateAt.class);
    assertThat(result.updatedAt()).isEqualTo(now);
    assertThat(result.updatedAt()).isBeforeOrEqualTo(now.plusSeconds(5));
  }

  @Test
  @DisplayName("TicketUpdateAt createNow returns a valid object")
  void ticketUpdateAtCreateNowReturnAValidObject() {
    var before = Instant.now();
    var result = TicketUpdateAt.createNow();
    var after = Instant.now();
    assertThat(result).isNotNull().isInstanceOf(TicketUpdateAt.class);
    assertThat(result.updatedAt()).isAfterOrEqualTo(before);
    assertThat(result.updatedAt()).isBeforeOrEqualTo(after);
  }

  @Test
  @DisplayName("TicketUpdateAt objects created from createNow are new instances each time")
  void ticketUpdateAtObjectCreatedFromCreateNowAreNewInstanceEachTime() {
    var time1 = TicketUpdateAt.createNow();
    var time2 = TicketUpdateAt.createNow();
    assertThat(time1).isNotSameAs(time2);
  }
}
