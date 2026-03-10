package com.efpcode.domain.partner.model;

import static org.assertj.core.api.Assertions.*;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class PartnerTest {

  private static PartnerId ANY_ID = PartnerId.generate();
  private static PartnerName ANY_NAME = new PartnerName("Test Partner");
  private static PartnerCity ANY_CITY = new PartnerCity("Test City");
  private static PartnerCountry ANY_COUNTRY = new PartnerCountry("TEST COUNTRY");
  private static PartnerIsoCode ANY_ISOCODE = new PartnerIsoCode("SWE");
  private static PartnerStatus ANY_STATUS = PartnerStatus.ACTIVE;

  private static Stream<Arguments> invalidArgumentsThatFail() {
    var testPartnerId = PartnerId.generate();
    var testPartnerName = new PartnerName("Test Partner");
    var testPartnerCity = new PartnerCity("Test City");
    var testPartnerCountry = new PartnerCountry("TEST COUNTRY");
    var testPartnerIsoCode = new PartnerIsoCode("SWE");
    var testPartnerStatus = PartnerStatus.ACTIVE;
    return Stream.of(
        Arguments.of(
            null,
            testPartnerName,
            testPartnerCity,
            testPartnerCountry,
            testPartnerIsoCode,
            testPartnerStatus,
            "Id cannot be null"),
        Arguments.of(
            testPartnerId,
            null,
            testPartnerCity,
            testPartnerCountry,
            testPartnerIsoCode,
            testPartnerStatus,
            "Name cannot be null"),
        Arguments.of(
            testPartnerId,
            testPartnerName,
            null,
            testPartnerCountry,
            testPartnerIsoCode,
            testPartnerStatus,
            "City cannot be null"),
        Arguments.of(
            testPartnerId,
            testPartnerName,
            testPartnerCity,
            null,
            testPartnerIsoCode,
            testPartnerStatus,
            "Country cannot be null"),
        Arguments.of(
            testPartnerId,
            testPartnerName,
            testPartnerCity,
            testPartnerCountry,
            null,
            testPartnerStatus,
            "IsoCode cannot be null"),
        Arguments.of(
            testPartnerId,
            testPartnerName,
            testPartnerCity,
            testPartnerCountry,
            testPartnerIsoCode,
            null,
            "Status cannot be null"));
  }

  @ParameterizedTest
  @MethodSource("invalidArgumentsThatFail")
  @DisplayName("Partner throws NULL Pointer Exception if any fields passed is null")
  void partnerThrowsNullPointerExceptionIfAnyFieldsPassedIsNull(
      PartnerId partnerId,
      PartnerName partnerName,
      PartnerCity partnerCity,
      PartnerCountry partnerCountry,
      PartnerIsoCode partnerIsoCode,
      PartnerStatus partnerStatus,
      String expectedMessage) {

    assertThatThrownBy(
            () ->
                new Partner(
                    partnerId,
                    partnerName,
                    partnerCity,
                    partnerCountry,
                    partnerIsoCode,
                    partnerStatus))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining(expectedMessage);
  }

  @Test
  @DisplayName("Partner creates a valid object if input is valid")
  void partnerCreatesAValidObjectIfInputIsValid() {

    var partner = new Partner(ANY_ID, ANY_NAME, ANY_CITY, ANY_COUNTRY, ANY_ISOCODE, ANY_STATUS);

    assertThat(partner).isNotNull().isInstanceOf(Partner.class);
    assertThat(partner.id()).isEqualTo(ANY_ID);
    assertThat(partner.name()).isEqualTo(ANY_NAME);
    assertThat(partner.city()).isEqualTo(ANY_CITY);
    assertThat(partner.country()).isEqualTo(ANY_COUNTRY);
    assertThat(partner.isoCode()).isEqualTo(ANY_ISOCODE);
    assertThat(partner.status()).isEqualTo(ANY_STATUS);
  }
}
