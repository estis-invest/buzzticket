package com.efpcode.domain.partner.model;

import static org.assertj.core.api.Assertions.*;

import com.efpcode.domain.partner.exceptions.IllegalPartnerIsoCodeFormatException;
import com.efpcode.domain.partner.exceptions.InvalidPartnerIsoCodeException;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class PartnerIsoCodeTest {

  private static Stream<Arguments> provideBlankAndNull() {
    return Stream.of(
        Arguments.of((String) null), Arguments.of(""), Arguments.of("   "), Arguments.of("\n\t"));
  }

  @ParameterizedTest
  @MethodSource("provideBlankAndNull")
  @DisplayName("Iso code cannot be null or blank throws error")
  void isoCodeCannotBeNullOrBlankThrowsError(String isoCode) {
    assertThatThrownBy(() -> new PartnerIsoCode(isoCode))
        .isInstanceOf(InvalidPartnerIsoCodeException.class)
        .hasMessageContaining("IsoCode cannot be null or blank");
  }

  @Test
  @DisplayName("Iso code can create a valid object")
  void isoCodeCanCreateAValidObject() {
    var testIsoCode = "SWE";
    var results = new PartnerIsoCode("SWE");
    assertThat(results.isoCode()).hasToString(testIsoCode);
    assertThat(results).isNotNull().isInstanceOf(PartnerIsoCode.class);
  }

  private static Stream<Arguments> provideIsoCodesThatPasses() {
    return Stream.of(Arguments.of("SWE"), Arguments.of("GBR"), Arguments.of("USA"));
  }

  @ParameterizedTest
  @MethodSource("provideIsoCodesThatPasses")
  @DisplayName("Iso code that follows format creates valid objects")
  void isoCodeThatFollowsFormatCreatesValidObjects(String isoCode) {
    assertThatCode(() -> new PartnerIsoCode(isoCode)).doesNotThrowAnyException();
  }

  private static Stream<Arguments> provideIsoCodesThatFail() {
    return Stream.of(
        Arguments.of("S"), Arguments.of("DE"), Arguments.of("SE"), Arguments.of("AZBJ"));
  }

  @ParameterizedTest
  @MethodSource("provideIsoCodesThatFail")
  @DisplayName("Iso code that does not follow format throws error")
  void isoCodeThatDoesNotFollowFormatThrowsError(String isoCode) {
    assertThatThrownBy(() -> new PartnerIsoCode(isoCode))
        .isInstanceOf(IllegalPartnerIsoCodeFormatException.class)
        .hasMessageContaining("IsoCode must be 3 characters long and letters need to be uppercase");
  }

  @ParameterizedTest
  @MethodSource("provideIsoCodesThatPasses")
  @DisplayName("IsoCode method update returns new valid object")
  void isoCodeMethodUpdateReturnsNewValidObject(String isoCode) {

    var oldIsoCode = new PartnerIsoCode("CHN");
    var result = oldIsoCode.update(isoCode);

    assertThat(result).isNotSameAs(oldIsoCode);
    assertThat(result.isoCode()).hasToString(isoCode);
  }

  private static Stream<Arguments> provideIsoCodeLowerCaseThatFails() {
    return Stream.of(Arguments.of("swe"), Arguments.of("gbr"), Arguments.of("usa"));
  }

  @ParameterizedTest
  @MethodSource("provideIsoCodeLowerCaseThatFails")
  @DisplayName("IsoCode method update throws error if isoCode is not uppercase")
  void isoCodeMethodUpdateThrowsErrorIfIsoCodeIsNotUpperCase(String isoCode) {
    assertThatThrownBy(() -> new PartnerIsoCode(isoCode))
        .isInstanceOf(IllegalPartnerIsoCodeFormatException.class)
        .hasMessageContaining("IsoCode must be uppercase");
  }
}
