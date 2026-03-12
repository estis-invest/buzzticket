package com.efpcode.domain.partner.model;

import static org.assertj.core.api.Assertions.*;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

class PartnerTest {

  private static final PartnerId ANY_ID = PartnerId.generate();
  private static final PartnerName ANY_NAME = new PartnerName("Test Partner");
  private static final PartnerCity ANY_CITY = new PartnerCity("Test City");
  private static final PartnerCountry ANY_COUNTRY = new PartnerCountry("TEST COUNTRY");
  private static final PartnerIsoCode ANY_ISO_CODE = new PartnerIsoCode("SWE");
  private static final PartnerStatus ANY_STATUS = PartnerStatus.ACTIVE;
  private static final PartnerCreatedAt ANY_TIME = PartnerCreatedAt.createNow();

  private static Stream<Arguments> invalidArgumentsThatFail() {
    var testPartnerId = PartnerId.generate();
    var testPartnerName = new PartnerName("Test Partner");
    var testPartnerCity = new PartnerCity("Test City");
    var testPartnerCountry = new PartnerCountry("TEST COUNTRY");
    var testPartnerIsoCode = new PartnerIsoCode("SWE");
    var testPartnerStatus = PartnerStatus.ACTIVE;
    var testPartnerCreatedAt = PartnerCreatedAt.createNow();
    return Stream.of(
        Arguments.of(
            null,
            testPartnerName,
            testPartnerCity,
            testPartnerCountry,
            testPartnerIsoCode,
            testPartnerStatus,
            testPartnerCreatedAt,
            "Id cannot be null"),
        Arguments.of(
            testPartnerId,
            null,
            testPartnerCity,
            testPartnerCountry,
            testPartnerIsoCode,
            testPartnerStatus,
            testPartnerCreatedAt,
            "Name cannot be null"),
        Arguments.of(
            testPartnerId,
            testPartnerName,
            null,
            testPartnerCountry,
            testPartnerIsoCode,
            testPartnerStatus,
            testPartnerCreatedAt,
            "City cannot be null"),
        Arguments.of(
            testPartnerId,
            testPartnerName,
            testPartnerCity,
            null,
            testPartnerIsoCode,
            testPartnerStatus,
            testPartnerCreatedAt,
            "Country cannot be null"),
        Arguments.of(
            testPartnerId,
            testPartnerName,
            testPartnerCity,
            testPartnerCountry,
            null,
            testPartnerStatus,
            testPartnerCreatedAt,
            "IsoCode cannot be null"),
        Arguments.of(
            testPartnerId,
            testPartnerName,
            testPartnerCity,
            testPartnerCountry,
            testPartnerIsoCode,
            null,
            testPartnerCreatedAt,
            "Status cannot be null"),
        Arguments.of(
            testPartnerId,
            testPartnerName,
            testPartnerCity,
            testPartnerCountry,
            testPartnerIsoCode,
            testPartnerStatus,
            null,
            "createdAt cannot be null"));
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
      PartnerCreatedAt partnerCreatedAt,
      String expectedMessage) {

    assertThatThrownBy(
            () ->
                new Partner(
                    partnerId,
                    partnerName,
                    partnerCity,
                    partnerCountry,
                    partnerIsoCode,
                    partnerStatus,
                    partnerCreatedAt))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining(expectedMessage);
  }

  @Test
  @DisplayName("Partner creates a valid object if input is valid")
  void partnerCreatesAValidObjectIfInputIsValid() {

    var partner =
        new Partner(ANY_ID, ANY_NAME, ANY_CITY, ANY_COUNTRY, ANY_ISO_CODE, ANY_STATUS, ANY_TIME);

    assertThat(partner).isNotNull().isInstanceOf(Partner.class);
    assertThat(partner.id()).isEqualTo(ANY_ID);
    assertThat(partner.name()).isEqualTo(ANY_NAME);
    assertThat(partner.city()).isEqualTo(ANY_CITY);
    assertThat(partner.country()).isEqualTo(ANY_COUNTRY);
    assertThat(partner.isoCode()).isEqualTo(ANY_ISO_CODE);
    assertThat(partner.status()).isEqualTo(ANY_STATUS);
    assertThat(partner.createdAt()).isEqualTo(ANY_TIME);
  }

  @Test
  @DisplayName("Partner is active if status is active")
  void partnerIsActiveIfStatusIsActive() {
    var partner =
        new Partner(ANY_ID, ANY_NAME, ANY_CITY, ANY_COUNTRY, ANY_ISO_CODE, ANY_STATUS, ANY_TIME);
    assertThat(partner.isActive()).isTrue();
  }

  @ParameterizedTest
  @EnumSource(
      value = PartnerStatus.class,
      names = {"DEACTIVATED", "DELETED", "EDIT"})
  @DisplayName("Partner is not active if status is not active")
  void partnerIsNotActiveIfStatusIsNotActive(PartnerStatus status) {
    var partner =
        new Partner(ANY_ID, ANY_NAME, ANY_CITY, ANY_COUNTRY, ANY_ISO_CODE, status, ANY_TIME);
    assertThat(partner.isActive()).isFalse();
  }

  @Test
  @DisplayName("isDeleted is true if Partner is labelled as deleted")
  void isDeletedIsTrueIfPartnerIsLabelledAsDeleted() {
    assertThat(PartnerStatus.DELETED.isDeleted()).isTrue();
  }

  @ParameterizedTest
  @EnumSource(
      value = PartnerStatus.class,
      names = {"DELETED"},
      mode = EnumSource.Mode.EXCLUDE)
  @DisplayName("isDeleted is false if Partner is not labelled as deleted")
  void isDeletedIsFalseIfPartnerIsNotLabelledAsDeleted(PartnerStatus status) {
    assertThat(status.isDeleted()).isFalse();
  }

  @Test
  @DisplayName("isEdit true when Partner is in Edit mode")
  void isEditTrueWhenPartnerIsInEditMode() {
    assertThat(PartnerStatus.EDIT.isEdit()).isTrue();
  }

  @ParameterizedTest
  @EnumSource(
      value = PartnerStatus.class,
      names = {"ACTIVE", "DEACTIVATED", "DELETED"})
  @DisplayName("isEdit false when Partner is not in Edit mode")
  void isEditFalseWhenPartnerIsNotInEditMode(PartnerStatus status) {
    assertThat(status.isEdit()).isFalse();
  }

  @Test
  @DisplayName("isDeactivated true when Partner is in Deactivated mode")
  void isDeactivatedTrueWhenPartnerIsInDeactivatedMode() {
    assertThat(PartnerStatus.DEACTIVATED.isDeactivated()).isTrue();
  }

  @ParameterizedTest
  @EnumSource(
      value = PartnerStatus.class,
      names = {"ACTIVE", "EDIT", "DELETED"})
  @DisplayName("isDeactivated false when Partner is not in Deactivated mode")
  void isDeactivatedFalseWhenPartnerIsNotInDeactivatedMode(PartnerStatus status) {
    assertThat(status.isDeactivated()).isFalse();
  }
}
