package com.efpcode.domain.ticket.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.efpcode.domain.ticket.exceptions.InvalidCreatedAtException;
import java.time.Instant;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class TicketCreatedAtTest {

  private static Stream<Arguments> providesInvalidTime() {

    return Stream.of(Arguments.of((Instant) null), Arguments.of(Instant.EPOCH));
  }

  @ParameterizedTest
  @MethodSource("providesInvalidTime")
  @DisplayName("TicketCreateAt cannot pass null value or zero createdAt")
  void ticketCreateAtCannotPassNullValueOrZeroCreatedAt(Instant badTime) {

    assertThatThrownBy(() -> new TicketCreatedAt(badTime))
        .isInstanceOf(InvalidCreatedAtException.class)
        .hasMessageContaining("Time is required");
  }

  @ParameterizedTest
  @MethodSource("providesInvalidTime")
  @DisplayName("Static method of cannot pass null or zero to create valid createdAt object")
  void staticMethodOfCannotPassNullOrZeroToCreateValidCreatedAtObject(Instant badTime) {

    assertThatThrownBy(() -> TicketCreatedAt.of(badTime))
        .isInstanceOf(InvalidCreatedAtException.class)
        .hasMessageContaining("Time is required");
  }

  @Test
  @DisplayName("Passing valid instant timestamp creates valid object")
  void passingValidInstantTimestampCreatesValidObject() {

    Instant now = Instant.now();

    TicketCreatedAt createdAt = new TicketCreatedAt(now);

    assertThat(createdAt.time()).isEqualTo(now);
  }

  @Test
  @DisplayName("Passing valid instant timestamp to of method creates valid object")
  void passingValidInstantTimestampToOfMethodCreatesValidObject() {

    Instant now = Instant.now();

    TicketCreatedAt createdAt = TicketCreatedAt.of(now);

    assertThat(createdAt.time()).isEqualTo(now);
  }
}
