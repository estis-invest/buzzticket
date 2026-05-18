package com.efpcode.domain.ticket.model;

import static org.assertj.core.api.Assertions.*;

import com.efpcode.domain.ticket.exceptions.InvalidTicketUpdateAtException;
import java.time.Instant;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

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
    var timeZero = Instant.EPOCH;
    assertThatThrownBy(() -> new TicketUpdateAt(timeZero))
        .isInstanceOf(InvalidTicketUpdateAtException.class)
        .hasMessageContaining("Update timestamp is required");
  }

  @Test
  @DisplayName("TicketUpdateAt created with current updatedAt returns a valid object")
  void ticketUpdateAtWithInstantNowReturnsAValidObject() {
    var now = Instant.now();
    var result = new TicketUpdateAt(now);
    assertThat(result).isNotNull().isInstanceOf(TicketUpdateAt.class);
    assertThat(result.updatedAt()).isEqualTo(now);
  }

  @Test
  @DisplayName("TicketUpdate of method creates valid object when timestamp is passed")
  void ticketUpdateOfMethodCreatesValidObjectWhenTimestampIsPassed() {

    var now = Instant.now();
    var result = TicketUpdateAt.of(now);
    assertThat(result).isNotNull().isInstanceOf(TicketUpdateAt.class);
    assertThat(result.updatedAt()).isEqualTo(now);
  }

  private static Stream<Arguments> providesInvalidTime() {
    return Stream.of(Arguments.of((Instant) null), Arguments.of(Instant.EPOCH));
  }

  @ParameterizedTest
  @MethodSource("providesInvalidTime")
  @DisplayName("Invalid time passing of method throws error")
  void invalidTimePassingOfMethodThrowsError(Instant badTime) {
    assertThatThrownBy(() -> TicketUpdateAt.of(badTime))
        .isInstanceOf(InvalidTicketUpdateAtException.class)
        .hasMessageContaining("Update timestamp is required");
  }
}
