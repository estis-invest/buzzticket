package com.efpcode.domain.ticket.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.efpcode.domain.ticket.exceptions.InvalidTicketIdException;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TicketIdTest {
  @Test
  @DisplayName("TicketId cannot pass null value")
  void ticketIdCannotPassNullValue() {

    assertThatThrownBy(() -> new TicketId(null))
        .isInstanceOf(InvalidTicketIdException.class)
        .hasMessageContaining("UUID is required");
  }

  @Test
  @DisplayName("TicketId generate method returns valid UUID object")
  void ticketIdGenerateMethodReturnsValidUuidObject() {
    var result = TicketId.generate();
    assertThat(result).isNotNull().isInstanceOf(TicketId.class);
  }

  @Test
  @DisplayName("TicketId fromString method returns a valid TicketID object")
  void ticketIdFromStringMethodReturnsAValidTicketIdObject() {

    var stringUUID = UUID.randomUUID().toString();

    TicketId result = TicketId.fromString(stringUUID);

    assertThat(result).isNotNull().isInstanceOf(TicketId.class);
    assertThat(result.value().toString()).hasToString(stringUUID);
  }
}
