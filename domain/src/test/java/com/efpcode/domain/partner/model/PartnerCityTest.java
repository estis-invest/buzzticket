package com.efpcode.domain.partner.model;

import static org.assertj.core.api.Assertions.*;

import com.efpcode.domain.partner.exceptions.IllegalPartnerCityMaxLengthException;
import com.efpcode.domain.partner.exceptions.IllegalPartnerCityNameFormatException;
import com.efpcode.domain.partner.exceptions.InvalidPartnerCityException;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class PartnerCityTest {

  private static Stream<Arguments> provideBlankAndNull() {
    return Stream.of(
        Arguments.of((String) null), Arguments.of(""), Arguments.of("   "), Arguments.of("\n\t"));
  }

  @ParameterizedTest
  @MethodSource("provideBlankAndNull")
  @DisplayName("PartnerCity does not except null or blank as input")
  void partnerCityDoesNotExceptNullOrBlankAsInput(String testInput) {

    assertThatThrownBy(() -> new PartnerCity(testInput))
        .isInstanceOf(InvalidPartnerCityException.class)
        .hasMessageContaining("cannot pass null or blank as partner city");
  }

  @Test
  @DisplayName("PartnerCity has a max length of a 100 characters throws error if exceeded")
  void partnerCityHasAMaxLengthOfA100CharactersThrowsErrorIfExceeded() {

    var city = "a".repeat(101);

    assertThatThrownBy(() -> new PartnerCity(city))
        .isInstanceOf(IllegalPartnerCityMaxLengthException.class)
        .hasMessageContaining(
            "City name cannot exceed 100 characters, input length was: " + city.trim().length());
  }

  private static Stream<Arguments> provideCityNamesThatPasses() {
    return Stream.of(
        Arguments.of("Winston-Salem"),
        Arguments.of("St. Petersburg"),
        Arguments.of("São Paulo"),
        Arguments.of("Vrångö"),
        Arguments.of("Bishop's Castle"),
        Arguments.of("São Paulo"),
        Arguments.of("München"));
  }

  @ParameterizedTest
  @MethodSource("provideCityNamesThatPasses")
  @DisplayName("City names format accepts only letters, dots, hyphen and space")
  void cityNamesFormatAcceptsOnlyLettersDotsHyphenAndSpace(String cityName) {

    assertThatCode(() -> new PartnerCity(cityName)).doesNotThrowAnyException();
  }

  private static Stream<Arguments> provideCityNamesThatFail() {
    return Stream.of(
        Arguments.of("London, UK"), // Comma
        Arguments.of("Berlin/West"), // Slash
        Arguments.of("Paris (Central)"), // Parentheses
        Arguments.of("City<script>"), // Angle brackets
        Arguments.of("New York #1"), // Hash/Pound
        Arguments.of("Stockholm!"), // Exclamation
        Arguments.of("Prague_OldTown"), // Underscore
        Arguments.of("Tokyo 🇯🇵") // Emoji
        );
  }

  @ParameterizedTest
  @MethodSource("provideCityNamesThatFail")
  @DisplayName("City names that do not follow format should throw error")
  void cityNamesThatDoNotFollowFormatShouldThrowError(String city) {

    assertThatThrownBy(() -> new PartnerCity(city))
        .isInstanceOf(IllegalPartnerCityNameFormatException.class)
        .hasMessageContaining(
            "City name should only contain letters, digits, dots, hyphen, apostrophe and space");
  }

  @Test
  @DisplayName("PartnerCity constructor can return a valid object")
  void partnerCityConstructorCanReturnAValidObject() {
    var testCity = new PartnerCity("  New York  ");
    var expected = "New York";

    assertThat(testCity.partnerCity())
        .isNotNull()
        .isInstanceOf(String.class)
        .hasSameSizeAs(expected)
        .hasToString(expected);
  }

  @Test
  @DisplayName("PartnerCity method update returns a new object")
  void partnerCityMethodUpdateReturnsANewObject() {

    var oldCity = new PartnerCity("Stockholm");
    var newCity = oldCity.update("Göteborg");

    assertThat(newCity).isNotSameAs(oldCity);
    assertThat(newCity.partnerCity()).hasToString("Göteborg");
  }
}
