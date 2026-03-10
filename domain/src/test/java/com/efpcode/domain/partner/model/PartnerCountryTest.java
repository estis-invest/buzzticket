package com.efpcode.domain.partner.model;

import static org.assertj.core.api.Assertions.*;

import com.efpcode.domain.partner.exceptions.IllegalPartnerCountryFormatException;
import com.efpcode.domain.partner.exceptions.IllegalPartnerCountryMaxLengthException;
import com.efpcode.domain.partner.exceptions.InvalidPartnerCountryException;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class PartnerCountryTest {

  private static Stream<Arguments> provideBlankAndNull() {
    return Stream.of(
        Arguments.of((String) null), Arguments.of(""), Arguments.of("   "), Arguments.of("\n\t"));
  }

  @ParameterizedTest
  @MethodSource("provideBlankAndNull")
  @DisplayName("PartnerCountry throws error if null or blank is passed")
  void partnerCountryThrowsErrorIfNullOrBlankIsPassed(String testInput) {

    assertThatThrownBy(() -> new PartnerCountry(testInput))
        .isInstanceOf(InvalidPartnerCountryException.class)
        .hasMessageContaining("Country cannot be created with null or blank");
  }

  private static Stream<Arguments> provideValidCountries() {
    return Stream.of(
        Arguments.of("ARGENTINA"),
        Arguments.of("ANTIGUA-ET-BARBUDA"),
        Arguments.of("CONGO (THE DEMOCRATIC REPUBLIC OF THE)"),
        Arguments.of("ROYAUME-UNI DE GRANDE-BRETAGNE ET D'IRLANDE DU NORD (LE)"),
        Arguments.of("USA"),
        Arguments.of("U.S.A."),
        Arguments.of("SWEDEN"),
        Arguments.of(" SWEDEN "),
        Arguments.of("VIRGIN ISLANDS (U.S.)"),
        Arguments.of("SOUTH AFRICA"));
  }

  @ParameterizedTest
  @MethodSource("provideValidCountries")
  @DisplayName("Country that follows format returns a valid object")
  void countryThatFollowsFormatReturnsAValidObject(String country) {

    assertThatCode(() -> new PartnerCountry(country)).doesNotThrowAnyException();
  }

  private static Stream<Arguments> provideInvalidCountries() {
    return Stream.of(
        Arguments.of("Argentina"),
        Arguments.of("Vierges des États-Unis (les Îles)"),
        Arguments.of("'Vierges des États-Unis (les Îles)'"),
        Arguments.of("SWEDen"),
        Arguments.of("sweden"),
        Arguments.of("sweDEN"),
        Arguments.of("Tokyo 🇯🇵") // Emojis
        );
  }

  @ParameterizedTest
  @MethodSource("provideInvalidCountries")
  @DisplayName("Country that does not follow format throws error")
  void countryThatDoesNotFollowFormatThrowsError(String country) {

    assertThatThrownBy(() -> new PartnerCountry(country))
        .isInstanceOf(IllegalPartnerCountryFormatException.class)
        .hasMessageContaining(
            String.format(
                "Country name: %s does not follow format: only upper case letters, commas, parenthesis, hyphens and dots are allowed",
                country));
  }

  @Test
  @DisplayName("PartnerCountry returns a valid object if format is followed")
  void partnerCountryReturnsAValidObjectIfFormatIsFollowed() {

    var country = new PartnerCountry("SWEDEN");

    assertThat(country.partnerCountry()).hasToString("SWEDEN");
  }

  @Test
  @DisplayName("Country name cannot exceed more then 100 characters")
  void countryNameCannotExceedMoreThen100Characters() {

    var country = "A".repeat(101);
    var upperLimit = 100;

    assertThatThrownBy(() -> new PartnerCountry(country))
        .isInstanceOf(IllegalPartnerCountryMaxLengthException.class)
        .hasMessageContaining(
            String.format(
                "Country name passed exceeds max length: %d characters. Passed name had a length of %d characters",
                upperLimit, country.trim().length()));
  }

  @ParameterizedTest
  @MethodSource("provideValidCountries")
  @DisplayName("PartnerCountry method update returns a new valid object")
  void partnerCountryMethodUpdateReturnsANewValidObject(String country) {

    var oldCountry = new PartnerCountry("England".toUpperCase());
    PartnerCountry newCountry = oldCountry.update(country);

    assertThat(newCountry).isNotSameAs(oldCountry);
    assertThat(newCountry.partnerCountry()).hasToString(country.trim());
  }

  private static Stream<Arguments> provideInvalidCountryWordsThatFails() {
    return Stream.of(
        Arguments.of("S"),
        Arguments.of("S "),
        Arguments.of("S W E D E N"),
        Arguments.of("SW E D E N"),
        Arguments.of("SWE D E N"),
        Arguments.of("SWED E N"),
        Arguments.of("SWEDE N"),
        Arguments.of("A (B) C"),
        Arguments.of("S    W     E    D    E     N"));
  }

  @ParameterizedTest
  @MethodSource("provideInvalidCountryWordsThatFails")
  @DisplayName("PartnerCountry throws error if country name contains invalid words")
  void partnerCountryThrowsErrorIfCountryNameContainsInvalidWords(String country) {
    var testCountry = country.trim();

    assertThatThrownBy(() -> new PartnerCountry(testCountry))
        .isInstanceOf(IllegalPartnerCountryFormatException.class)
        .hasMessageContaining(
            String.format("Country cannot be created with invalid words: %s", testCountry));
  }
}
