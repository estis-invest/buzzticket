package com.efpcode.domain.partner.model;

import static org.assertj.core.api.Assertions.*;

import com.efpcode.domain.partner.exceptions.IllegalPartnerIdArgumentException;
import com.efpcode.domain.partner.exceptions.InvalidPartnerIdException;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class PartnerIdTest {
  @Test
  @DisplayName("PartnerId cannot pass null throws exception")
  void partnerIdCannotPassNullThrowsException() {

    assertThatThrownBy(() -> new PartnerId(null))
        .isInstanceOf(InvalidPartnerIdException.class)
        .hasMessageContaining("Partner cannot be null");
  }

  @Test
  @DisplayName("PartnerId method generate returns a valid object")
  void partnerIdMethodGenerateReturnsAValidObject() {

    var result = PartnerId.generate();
    assertThat(result).isNotNull().isInstanceOf(PartnerId.class);
  }

  @Test
  @DisplayName("PartnerId method fromString returns a valid object")
  void partnerIdMethodFromStringReturnsAValidObject() {
    var uuidString = UUID.randomUUID().toString();
    PartnerId result = PartnerId.fromString(uuidString);
    assertThat(result).isNotNull().isInstanceOf(PartnerId.class);
    assertThat(result.partnerId().toString()).hasToString(uuidString);
  }

  private static Stream<Arguments> provideBlankAndNull() {
    return Stream.of(Arguments.of(null, true), Arguments.of("", true), Arguments.of("   ", true));
  }

  @ParameterizedTest
  @MethodSource("provideBlankAndNull")
  @DisplayName("PartnerId method fromString throws error if null or blank is passed")
  void partnerIdMethodFromStringThrowsErrorIfNullOrBlankIsPassed(String uuidTest) {
    assertThatThrownBy(() -> PartnerId.fromString(uuidTest))
        .isInstanceOf(IllegalPartnerIdArgumentException.class)
        .hasMessageContaining("fromString method cannot pass null or blank");
  }
}
