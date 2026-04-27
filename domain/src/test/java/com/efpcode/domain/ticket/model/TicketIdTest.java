package com.efpcode.domain.ticket.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.efpcode.domain.testsupport.TestUUIDIds;
import com.efpcode.domain.ticket.exceptions.IllegalTicketIdArgumentException;
import com.efpcode.domain.ticket.exceptions.InvalidTicketIdException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class TicketIdTest {
  @Test
  @DisplayName("TicketId cannot pass null value")
  void ticketIdCannotPassNullValue() {

    assertThatThrownBy(() -> new TicketId(null))
        .isInstanceOf(InvalidTicketIdException.class)
        .hasMessageContaining("UUID is required");
  }

  @Test
  @DisplayName("TicketId of method returns valid UUID object")
  void ticketIdOfMethodReturnsValidUuidObject() {
    var expected = TestUUIDIds.ticketId();
    var result = TicketId.of(expected.value());

    assertThat(result).isNotNull().isInstanceOf(TicketId.class);
    assertThat(result.value()).isEqualTo(expected.value());
  }

  @Test
  @DisplayName("TicketId fromString method returns a valid TicketID object")
  void ticketIdFromStringMethodReturnsAValidTicketIdObject() {

    var stringUUID = "00000000-0000-0000-0000-000000000001";

    var expected = TestUUIDIds.ticketId(stringUUID);

    TicketId result = TicketId.fromString(stringUUID);

    assertThat(result).isNotNull().isInstanceOf(TicketId.class);
    assertThat(result.value()).hasToString(stringUUID);
    assertThat(result).isEqualTo(expected);
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {" ", "   ", "\t", "\n"})
  @DisplayName("fromString method throws error if null or blank is passed")
  void fromStringMethodThrowsErrorIfNullOrBlankIsPassed(String invalidArgs) {

    assertThatThrownBy(() -> TicketId.fromString(invalidArgs))
        .isInstanceOf(IllegalTicketIdArgumentException.class)
        .hasMessageContaining("fromString method cannot pass null or blank");
  }

  @Test
  @DisplayName("Malformatted UUID will throws error")
  void malformattedUuidWillThrowsError() {
    var malFormattedUUID = "100-not-valid";

    assertThatThrownBy(() -> TicketId.fromString(malFormattedUUID))
        .isInstanceOf(InvalidTicketIdException.class)
        .hasMessageContaining("Invalid or malformatted uuid");
  }
}
