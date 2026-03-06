package com.efpcode.domain.partner.model;

import static org.assertj.core.api.Assertions.*;

import com.efpcode.domain.partner.exceptions.IllegalPartnerNameLengthException;
import com.efpcode.domain.partner.exceptions.InvalidPartnerNameException;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class PartnerNameTest {

  private static Stream<Arguments> provideBlankAndNull() {
    return Stream.of(
        Arguments.of(null, true),
        Arguments.of("", true),
        Arguments.of("   ", true),
        Arguments.of("\n\t", true));
  }

  @ParameterizedTest
  @MethodSource("provideBlankAndNull")
  @DisplayName("PartnerName throws error if blank or null is passed")
  void partnerNameThrowsErrorIfBlankOrNullIsPassed(String testInput) {
    assertThatThrownBy(() -> new PartnerName(testInput))
        .isInstanceOf(InvalidPartnerNameException.class)
        .hasMessageContaining("PartnerName cannot be instantiated with null or blank as argument.");
  }

  @Test
  @DisplayName("PartnerName cannot exceed upper limit of characters")
  void partnerNameCannotExceedUpperLimitOfCharacters() {
    var upperLimit = 255;

    var testInput = "a".repeat(256);
    assertThatThrownBy(() -> new PartnerName(testInput))
        .isInstanceOf(IllegalPartnerNameLengthException.class)
        .hasMessageContaining("PartnerName must not exceed character limit: " + upperLimit);
  }

  @Test
  @DisplayName("Partner name within character limit creates a valid object")
  void partnerNameWithinCharacterLimitCreatesAValidObject() {

    var testInput = "a".repeat(255);
    var partnerName = new PartnerName(testInput);

    assertThat(partnerName).isNotNull().isInstanceOf(PartnerName.class);
    assertThat(partnerName.partnerName()).hasSameSizeAs(testInput).hasToString(testInput);
  }

  @ParameterizedTest
  @MethodSource("provideBlankAndNull")
  @DisplayName("PartnerName update throws error if blank or null is passed")
  void partnerNameUpdateThrowsErrorIfBlankOrNullIsPassed(String testInput) {
    var partnerName = new PartnerName("a");
    assertThatThrownBy(() -> partnerName.update(testInput))
        .isInstanceOf(InvalidPartnerNameException.class)
        .hasMessageContaining("PartnerName cannot be instantiated with null or blank as argument.");
  }
}
