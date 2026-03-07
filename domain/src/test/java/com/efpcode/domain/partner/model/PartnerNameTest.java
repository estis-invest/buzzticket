package com.efpcode.domain.partner.model;

import static org.assertj.core.api.Assertions.*;

import com.efpcode.domain.partner.exceptions.IllegalPartnerNameFormatException;
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
        Arguments.of((String) null), Arguments.of(""), Arguments.of("   "), Arguments.of("\n\t"));
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

  @Test
  @DisplayName("PartnerName update creates valid object if string is passed")
  void partnerNameUpdateCreatesValidObjectIfStringIsPassed() {

    var partnerName = new PartnerName("old");
    var result = partnerName.update("new");

    assertThat(result).isNotSameAs(partnerName);
    assertThat(result.partnerName()).doesNotContain(partnerName.partnerName());
  }

  private static Stream<Arguments> providePartnerNamesThatPasses() {
    return Stream.of(
        Arguments.of("P.I.X.A.R"),
        Arguments.of("L'Oréal"),
        Arguments.of("AT&T"),
        Arguments.of("K-märkt"),
        Arguments.of("Volkswagen AG (VOW)"),
        Arguments.of("Dev@Work"),
        Arguments.of("Apple, inc"));
  }

  @ParameterizedTest
  @MethodSource("providePartnerNamesThatPasses")
  @DisplayName("PartnerName that follows format creates valid objects")
  void partnerNameThatFollowsFormatCreatesValidObjects(String name) {

    assertThatCode(() -> new PartnerName(name)).doesNotThrowAnyException();
  }

  private static Stream<Arguments> providePartnerNamesThatFail() {
    return Stream.of(
        Arguments.of("Apple_Inc"), // Underscore is not allowed
        Arguments.of("Buda/Pest"), // Forward slash is not allowed
        Arguments.of("Partner#1"), // Hash/Pound is not allowed
        Arguments.of("Price < 100"), // Angle brackets / Math symbols
        Arguments.of("Profit+Growth"), // Plus (Unless you explicitly add it!)
        Arguments.of("Total = 50"), // Equals sign
        Arguments.of("Name; DROP TABLE"), // Semicolon
        Arguments.of("Name\""), // Double quotes (we only allowed single)
        Arguments.of("Name!"), // Exclamation mark
        Arguments.of("The $ Company"), // Dollar sign
        Arguments.of("Big [Store]"), // Square brackets (we only allowed parentheses)
        Arguments.of("Tokyo 🇯🇵") // Emojis
        );
  }

  @ParameterizedTest
  @MethodSource("providePartnerNamesThatFail")
  @DisplayName("PartnerNames that not follow correct format throws error")
  void partnerNamesThatNotFollowCorrectFormatThrowsError(String testInput) {

    assertThatThrownBy(() -> new PartnerName(testInput))
        .isInstanceOf(IllegalPartnerNameFormatException.class)
        .hasMessageContaining(
            String.format(
                "Partner name: %s is not following format standard allowed characters are letters, digits, ampersands, apostrophe, hyphen, dots, @, parenthesis and commas",
                testInput));
  }
}
