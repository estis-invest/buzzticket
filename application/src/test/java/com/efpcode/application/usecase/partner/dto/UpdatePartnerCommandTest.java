package com.efpcode.application.usecase.partner.dto;

import static org.assertj.core.api.Assertions.*;

import com.efpcode.application.testsupport.TestUUIDIds;
import com.efpcode.domain.partner.model.Partner;
import com.efpcode.domain.partner.model.PartnerId;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class UpdatePartnerCommandTest {

  private Partner testPartner;
  private PartnerId id;

  @BeforeEach
  void setUp() {
    id = TestUUIDIds.partnerId();
    testPartner = Partner.createDraftPartner(id, "TEST", "TEST", "TEST", "TST");
  }

  private static Stream<Arguments> nonUpdateForNullOrEmptyArguments() {
    return Stream.of(
        Arguments.of("", "", "", ""),
        Arguments.of(null, null, null, null),
        Arguments.of(" ", "  ", "   ", "   "),
        Arguments.of(null, "", "  ", "    "));
  }

  @ParameterizedTest(name = "[{index}] Testing: name={0}, city={1}, country={2}, isoCode={3}")
  @MethodSource("nonUpdateForNullOrEmptyArguments")
  @DisplayName("No updates for null or blank data")
  void noUpdatesForNullOrBlankData(String name, String city, String country, String isoCode) {
    var command = new UpdatePartnerCommand(name, city, country, isoCode);
    var result = UpdatePartnerCommand.merge(command, testPartner);

    assertThat(result.name()).isEqualTo(testPartner.name().partnerName());
    assertThat(result.city()).isEqualTo(testPartner.city().partnerCity());
    assertThat(result.country()).isEqualTo(testPartner.country().partnerCountry());
    assertThat(result.isoCode()).isEqualTo(testPartner.isoCode().isoCode());
  }

  private static Stream<Arguments> partialUpdateForArguments() {
    return Stream.of(
        Arguments.of("BEST", "", "", ""),
        Arguments.of("", "BEST", "", ""),
        Arguments.of("", "", "BEST", ""),
        Arguments.of("", "", "", "BST"),
        Arguments.of("BEST", "BEST", "", ""),
        Arguments.of("BEST", "BEST", "BEST", ""),
        Arguments.of("BEST", "BEST", "BEST", "BST"));
  }

  @ParameterizedTest(name = "[{index}] Testing: name={0}, city={1}, country={2}, isoCode={3}")
  @MethodSource("partialUpdateForArguments")
  @DisplayName("Partial Update returns a valid object")
  void partialUpdateReturnsAValidObject(String name, String city, String country, String isoCode) {
    var original =
        new UpdatePartnerCommand(
            testPartner.name().partnerName(),
            testPartner.city().partnerCity(),
            testPartner.country().partnerCountry(),
            testPartner.isoCode().isoCode());
    var command = new UpdatePartnerCommand(name, city, country, isoCode);
    var result = UpdatePartnerCommand.merge(command, testPartner);

    assertThat(result).isNotEqualTo(original);
  }
}
