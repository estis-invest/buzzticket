package com.efpcode.domain.partner.model;

import static org.assertj.core.api.Assertions.*;

import com.efpcode.domain.partner.exceptions.IllegalPartnerStatusTransitionException;
import com.efpcode.domain.testsupport.TestUUIDIds;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.function.Function;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

class PartnerTest {

  private static final PartnerId ANY_ID = TestUUIDIds.partnerId();
  private static final PartnerName ANY_NAME = new PartnerName("Test Partner");
  private static final PartnerCity ANY_CITY = new PartnerCity("Test City");
  private static final PartnerCountry ANY_COUNTRY = new PartnerCountry("TEST COUNTRY");
  private static final PartnerIsoCode ANY_ISO_CODE = new PartnerIsoCode("SWE");
  private static final PartnerStatus ANY_STATUS = PartnerStatus.ACTIVE;
  private static final PartnerCreatedAt ANY_TIME = PartnerCreatedAt.createNow();
  public static final PartnerUpdateAt ANY_TIME_UPDATE = PartnerUpdateAt.createNow();

  private static Stream<Arguments> invalidArgumentsThatFail() {
    var testPartnerId = TestUUIDIds.partnerId();
    var testPartnerName = new PartnerName("Test Partner");
    var testPartnerCity = new PartnerCity("Test City");
    var testPartnerCountry = new PartnerCountry("TEST COUNTRY");
    var testPartnerIsoCode = new PartnerIsoCode("SWE");
    var testPartnerStatus = PartnerStatus.ACTIVE;
    var testPartnerCreatedAt = PartnerCreatedAt.createNow();
    var testPartnerUpdatedAt = ANY_TIME_UPDATE;
    return Stream.of(
        Arguments.of(
            null,
            testPartnerName,
            testPartnerCity,
            testPartnerCountry,
            testPartnerIsoCode,
            testPartnerStatus,
            testPartnerCreatedAt,
            testPartnerUpdatedAt,
            "Id cannot be null"),
        Arguments.of(
            testPartnerId,
            null,
            testPartnerCity,
            testPartnerCountry,
            testPartnerIsoCode,
            testPartnerStatus,
            testPartnerCreatedAt,
            testPartnerUpdatedAt,
            "Name cannot be null"),
        Arguments.of(
            testPartnerId,
            testPartnerName,
            null,
            testPartnerCountry,
            testPartnerIsoCode,
            testPartnerStatus,
            testPartnerCreatedAt,
            testPartnerUpdatedAt,
            "City cannot be null"),
        Arguments.of(
            testPartnerId,
            testPartnerName,
            testPartnerCity,
            null,
            testPartnerIsoCode,
            testPartnerStatus,
            testPartnerCreatedAt,
            testPartnerUpdatedAt,
            "Country cannot be null"),
        Arguments.of(
            testPartnerId,
            testPartnerName,
            testPartnerCity,
            testPartnerCountry,
            null,
            testPartnerStatus,
            testPartnerCreatedAt,
            testPartnerUpdatedAt,
            "IsoCode cannot be null"),
        Arguments.of(
            testPartnerId,
            testPartnerName,
            testPartnerCity,
            testPartnerCountry,
            testPartnerIsoCode,
            null,
            testPartnerCreatedAt,
            testPartnerUpdatedAt,
            "Status cannot be null"),
        Arguments.of(
            testPartnerId,
            testPartnerName,
            testPartnerCity,
            testPartnerCountry,
            testPartnerIsoCode,
            testPartnerStatus,
            null,
            testPartnerUpdatedAt,
            "createdAt cannot be null"),
        Arguments.of(
            testPartnerId,
            testPartnerName,
            testPartnerCity,
            testPartnerCountry,
            testPartnerIsoCode,
            testPartnerStatus,
            testPartnerCreatedAt,
            null,
            "updatedAt cannot be null"));
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
      PartnerUpdateAt partnerUpdatedAt,
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
                    partnerCreatedAt,
                    partnerUpdatedAt))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining(expectedMessage);
  }

  @Test
  @DisplayName("Partner creates a valid object if input is valid")
  void partnerCreatesAValidObjectIfInputIsValid() {

    var partner =
        new Partner(
            ANY_ID,
            ANY_NAME,
            ANY_CITY,
            ANY_COUNTRY,
            ANY_ISO_CODE,
            ANY_STATUS,
            ANY_TIME,
            ANY_TIME_UPDATE);

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
        new Partner(
            ANY_ID,
            ANY_NAME,
            ANY_CITY,
            ANY_COUNTRY,
            ANY_ISO_CODE,
            ANY_STATUS,
            ANY_TIME,
            ANY_TIME_UPDATE);
    assertThat(partner.isActive()).isTrue();
  }

  @ParameterizedTest
  @EnumSource(
      value = PartnerStatus.class,
      names = {"DEACTIVATED", "DELETED", "EDIT"})
  @DisplayName("Partner is not active if status is not active")
  void partnerIsNotActiveIfStatusIsNotActive(PartnerStatus status) {
    var partner =
        new Partner(
            ANY_ID,
            ANY_NAME,
            ANY_CITY,
            ANY_COUNTRY,
            ANY_ISO_CODE,
            status,
            ANY_TIME,
            ANY_TIME_UPDATE);
    assertThat(partner.isActive()).isFalse();
  }

  @Test
  @DisplayName("isDeleted is true if Partner is labelled as deleted")
  void isDeletedIsTrueIfPartnerIsLabelledAsDeleted() {
    var partner =
        new Partner(
            ANY_ID,
            ANY_NAME,
            ANY_CITY,
            ANY_COUNTRY,
            ANY_ISO_CODE,
            PartnerStatus.DELETED,
            ANY_TIME,
            ANY_TIME_UPDATE);
    assertThat(partner.isDeleted()).isTrue();
  }

  @ParameterizedTest
  @EnumSource(
      value = PartnerStatus.class,
      names = {"DELETED"},
      mode = EnumSource.Mode.EXCLUDE)
  @DisplayName("isDeleted is false if Partner is not labelled as deleted")
  void isDeletedIsFalseIfPartnerIsNotLabelledAsDeleted(PartnerStatus status) {
    var partner =
        new Partner(
            ANY_ID,
            ANY_NAME,
            ANY_CITY,
            ANY_COUNTRY,
            ANY_ISO_CODE,
            status,
            ANY_TIME,
            ANY_TIME_UPDATE);
    assertThat(partner.isDeleted()).isFalse();
  }

  @Test
  @DisplayName("isEdit true when Partner is in Edit mode")
  void isEditTrueWhenPartnerIsInEditMode() {
    var partner =
        new Partner(
            ANY_ID,
            ANY_NAME,
            ANY_CITY,
            ANY_COUNTRY,
            ANY_ISO_CODE,
            PartnerStatus.EDIT,
            ANY_TIME,
            ANY_TIME_UPDATE);
    assertThat(partner.isEdit()).isTrue();
  }

  @ParameterizedTest
  @EnumSource(
      value = PartnerStatus.class,
      names = {"ACTIVE", "DEACTIVATED", "DELETED"})
  @DisplayName("isEdit false when Partner is not in Edit mode")
  void isEditFalseWhenPartnerIsNotInEditMode(PartnerStatus status) {
    var partner =
        new Partner(
            ANY_ID,
            ANY_NAME,
            ANY_CITY,
            ANY_COUNTRY,
            ANY_ISO_CODE,
            status,
            ANY_TIME,
            ANY_TIME_UPDATE);
    assertThat(partner.isEdit()).isFalse();
  }

  @Test
  @DisplayName("isDeactivated true when Partner is in Deactivated mode")
  void isDeactivatedTrueWhenPartnerIsInDeactivatedMode() {
    var partner =
        new Partner(
            ANY_ID,
            ANY_NAME,
            ANY_CITY,
            ANY_COUNTRY,
            ANY_ISO_CODE,
            PartnerStatus.DEACTIVATED,
            ANY_TIME,
            ANY_TIME_UPDATE);
    assertThat(partner.isDeactivated()).isTrue();
  }

  @ParameterizedTest
  @EnumSource(
      value = PartnerStatus.class,
      names = {"ACTIVE", "EDIT", "DELETED"})
  @DisplayName("isDeactivated false when Partner is not in Deactivated mode")
  void isDeactivatedFalseWhenPartnerIsNotInDeactivatedMode(PartnerStatus status) {
    var partner =
        new Partner(
            ANY_ID,
            ANY_NAME,
            ANY_CITY,
            ANY_COUNTRY,
            ANY_ISO_CODE,
            status,
            ANY_TIME,
            ANY_TIME_UPDATE);
    assertThat(partner.isDeactivated()).isFalse();
  }

  @Test
  @DisplayName("canBeEdit is true for Partner with ACTIVE status")
  void canBeEditIsTrueForPartnerWithActiveStatus() {
    Partner partner =
        new Partner(
            ANY_ID,
            ANY_NAME,
            ANY_CITY,
            ANY_COUNTRY,
            ANY_ISO_CODE,
            PartnerStatus.ACTIVE,
            ANY_TIME,
            ANY_TIME_UPDATE);
    assertThat(partner.canBeEdit()).isTrue();
  }

  @ParameterizedTest
  @EnumSource(
      value = PartnerStatus.class,
      names = {"DELETED", "DEACTIVATED"})
  @DisplayName("canBeEdit is false for Partner with status other than ACTIVE or EDIT")
  void canBeEditIsFalseForPartnerWithStatusOtherThanActiveOrEdit(PartnerStatus status) {
    Partner partner =
        new Partner(
            ANY_ID,
            ANY_NAME,
            ANY_CITY,
            ANY_COUNTRY,
            ANY_ISO_CODE,
            status,
            ANY_TIME,
            ANY_TIME_UPDATE);
    assertThat(partner.canBeEdit()).isFalse();
  }

  @Nested
  class PartnerStateTest {

    private Partner draft;
    private Partner active;
    private Partner deactivated;
    private Partner deleted;

    @BeforeEach
    void setUp() {
      var id = TestUUIDIds.partnerId();
      var name = new PartnerName("EFP");
      var city = new PartnerCity("Berlin");
      var country = new PartnerCountry("DE");
      var iso = new PartnerIsoCode("DEU");
      var now = PartnerCreatedAt.createNow();
      var nowUpdate = new PartnerUpdateAt(Instant.now().minus(30, ChronoUnit.SECONDS));

      draft = new Partner(id, name, city, country, iso, PartnerStatus.EDIT, now, nowUpdate);
      active = new Partner(id, name, city, country, iso, PartnerStatus.ACTIVE, now, nowUpdate);
      deactivated =
          new Partner(id, name, city, country, iso, PartnerStatus.DEACTIVATED, now, nowUpdate);
      deleted = new Partner(id, name, city, country, iso, PartnerStatus.DELETED, now, nowUpdate);
    }

    private static Stream<Arguments> happyPathTransitions() {
      return Stream.of(
          Arguments.of(
              (Function<PartnerStateTest, Partner>) t -> t.draft.toActivate(),
              PartnerStatus.ACTIVE,
              (Function<PartnerStateTest, Instant>) t -> t.draft.updateAt().updatedAt()),
          Arguments.of(
              (Function<PartnerStateTest, Partner>) t -> t.active.toEdit(),
              PartnerStatus.EDIT,
              (Function<PartnerStateTest, Instant>) t -> t.active.updateAt().updatedAt()),
          Arguments.of(
              (Function<PartnerStateTest, Partner>) t -> t.active.toDeactivate(),
              PartnerStatus.DEACTIVATED,
              (Function<PartnerStateTest, Instant>) t -> t.active.updateAt().updatedAt()),
          Arguments.of(
              (Function<PartnerStateTest, Partner>) t -> t.deactivated.toActivate(),
              PartnerStatus.ACTIVE,
              (Function<PartnerStateTest, Instant>) t -> t.deactivated.updateAt().updatedAt()),
          Arguments.of(
              (Function<PartnerStateTest, Partner>) t -> t.deactivated.toDelete(),
              PartnerStatus.DELETED,
              (Function<PartnerStateTest, Instant>) t -> t.deactivated.updateAt().updatedAt()));
    }

    @ParameterizedTest
    @MethodSource("happyPathTransitions")
    @DisplayName("Partner state transition works")
    void partnerStateTransitionWorks(
        Function<PartnerStateTest, Partner> transition,
        PartnerStatus expectedStatus,
        Function<PartnerStateTest, Instant> expectedUpdatedAt) {
      Instant expectedUpdatedAtValue = expectedUpdatedAt.apply(this);
      Partner result = transition.apply(this);
      assertThat(result.status()).isEqualTo(expectedStatus);
      assertThat(result.updateAt().updatedAt()).isAfter(expectedUpdatedAtValue);
    }

    private static Stream<Arguments> deactivatedStatusTransitionThatFail() {
      return Stream.of(
          Arguments.of(
              (Function<Partner, Partner>) Partner::toEdit,
              "PartnerStatus: DEACTIVATED cannot be edited."),
          Arguments.of(
              (Function<Partner, Partner>) Partner::toDeactivate,
              "Only ACTIVE PartnerStatus can transition to DEACTIVATED"));
    }

    @ParameterizedTest
    @MethodSource("deactivatedStatusTransitionThatFail")
    @DisplayName("Deactivated partner throws errors for all transitions")
    void deactivatedPartnerThrowsErrorsForAllTransitions(
        Function<Partner, Partner> transition, String expectedMessage) {
      assertThatThrownBy(() -> transition.apply(deactivated))
          .isInstanceOf(IllegalPartnerStatusTransitionException.class)
          .hasMessageContaining(expectedMessage);
    }

    private static Stream<Arguments> activeStatusTransitionThatFail() {
      return Stream.of(
          Arguments.of(
              (Function<Partner, Partner>) Partner::toActivate,
              "Only DEACTIVATED and EDIT PartnerStatus can transition to ACTIVE"),
          Arguments.of(
              (Function<Partner, Partner>) Partner::toDelete,
              "Only DEACTIVATED PartnerStatus can transition to DELETED"));
    }

    @ParameterizedTest
    @MethodSource("activeStatusTransitionThatFail")
    @DisplayName("Active partner throws errors for all transitions")
    void activePartnerThrowsErrorsForAllTransitions(
        Function<Partner, Partner> transition, String expectedMessage) {
      assertThatThrownBy(() -> transition.apply(active))
          .isInstanceOf(IllegalPartnerStatusTransitionException.class)
          .hasMessageContaining(expectedMessage);
    }

    private static Stream<Arguments> editStatusTransitionThatFail() {
      return Stream.of(
          Arguments.of(
              (Function<Partner, Partner>) Partner::toDeactivate,
              "Only ACTIVE PartnerStatus can transition to DEACTIVATED"),
          Arguments.of(
              (Function<Partner, Partner>) Partner::toDelete,
              "Only DEACTIVATED PartnerStatus can transition to DELETED"));
    }

    @ParameterizedTest
    @MethodSource("editStatusTransitionThatFail")
    @DisplayName("Edit partner throws errors for all transitions")
    void editPartnerThrowsErrorsForAllTransitions(
        Function<Partner, Partner> transition, String expectedMessage) {
      assertThatThrownBy(() -> transition.apply(draft))
          .isInstanceOf(IllegalPartnerStatusTransitionException.class)
          .hasMessageContaining(expectedMessage);
    }

    private static Stream<Arguments> deletedStatusTransitionThatFail() {
      return Stream.of(
          Arguments.of(
              (Function<Partner, Partner>) Partner::toActivate,
              "Only DEACTIVATED and EDIT PartnerStatus can transition to ACTIVE"),
          Arguments.of(
              (Function<Partner, Partner>) Partner::toEdit,
              "PartnerStatus: DELETED cannot be edited."),
          Arguments.of(
              (Function<Partner, Partner>) Partner::toDeactivate,
              "Only ACTIVE PartnerStatus can transition to DEACTIVATED"),
          Arguments.of(
              (Function<Partner, Partner>) Partner::toDelete,
              "Only DEACTIVATED PartnerStatus can transition to DELETED"));
    }

    @ParameterizedTest
    @MethodSource("deletedStatusTransitionThatFail")
    @DisplayName("Deleted partner throws errors for all transitions")
    void deletedPartnerThrowsErrorsForAllTransitions(
        Function<Partner, Partner> transition, String expectedMessage) {
      assertThatThrownBy(() -> transition.apply(deleted))
          .isInstanceOf(IllegalPartnerStatusTransitionException.class)
          .hasMessageContaining(expectedMessage);
    }

    @Test
    @DisplayName("updatePartner should change data and return to ACTIVE status")
    void updatePartnerShouldChangeDataAndReturnToActiveStatus() {
      var newName = new PartnerName("Updated Name");
      var newCity = new PartnerCity("Munich");

      Partner updated = active.updatePartner(newName, newCity, null, null);

      assertThat(updated.name()).isEqualTo(newName);
      assertThat(updated.city()).isEqualTo(newCity);
      assertThat(updated.id()).isEqualTo(active.id());
      assertThat(updated.createdAt()).isEqualTo(active.createdAt());
      assertThat(updated.country()).isEqualTo(active.country());
      assertThat(updated.status()).isEqualTo(PartnerStatus.ACTIVE);
    }

    private static Stream<Arguments> updatePartnerCallsThatFail() {
      return Stream.of(
          Arguments.of(
              (Function<PartnerStateTest, Partner>)
                  t -> t.deleted.updatePartner(null, null, null, null),
              "PartnerStatus: DELETED cannot be edited."),
          Arguments.of(
              (Function<PartnerStateTest, Partner>)
                  t -> t.deactivated.updatePartner(null, null, null, null),
              "PartnerStatus: DEACTIVATED cannot be edited."));
    }

    @ParameterizedTest
    @MethodSource("updatePartnerCallsThatFail")
    @DisplayName("updatePartner throws errors for all transitions")
    void updatePartnerThrowsErrorsForAllTransitions(
        Function<PartnerStateTest, Partner> transition, String expectedMessage) {
      assertThatThrownBy(() -> transition.apply(this))
          .isInstanceOf(IllegalPartnerStatusTransitionException.class)
          .hasMessageContaining(expectedMessage);
    }

    @Test
    @DisplayName("updatePartner should work for DRAFT (EDIT) partners and move them to ACTIVE")
    void updatePartnerShouldWorkForDraftPartners() {
      var newName = new PartnerName("Draft Updated");

      Partner result = draft.updatePartner(newName, null, null, null);

      assertThat(result.status()).isEqualTo(PartnerStatus.ACTIVE);
      assertThat(result.name()).isEqualTo(newName);
    }
  }

  @Test
  @DisplayName("updatePartner returns the same object if all values are null")
  void updatePartnerReturnsTheSameObjectIfAllValuesAreNull() {
    var partner =
        new Partner(
            ANY_ID,
            ANY_NAME,
            ANY_CITY,
            ANY_COUNTRY,
            ANY_ISO_CODE,
            ANY_STATUS,
            ANY_TIME,
            ANY_TIME_UPDATE);

    var updatedPartner = partner.updatePartner(null, null, null, null);
    assertThat(updatedPartner).isNotSameAs(partner);
    assertThat(updatedPartner.id()).isEqualTo(partner.id());
    assertThat(updatedPartner.name()).isEqualTo(partner.name());
    assertThat(updatedPartner.city()).isEqualTo(partner.city());
    assertThat(updatedPartner.country()).isEqualTo(partner.country());
    assertThat(updatedPartner.isoCode()).isEqualTo(partner.isoCode());
    assertThat(updatedPartner.status()).isEqualTo(partner.status());
    assertThat(updatedPartner.createdAt()).isEqualTo(partner.createdAt());
  }

  @Test
  @DisplayName("updatePartner returns a valid new update object when args are passed")
  void updatePartnerReturnsAValidNewUpdateObjectWhenArgsArePassed() {
    var partner =
        new Partner(
            ANY_ID,
            ANY_NAME,
            ANY_CITY,
            ANY_COUNTRY,
            ANY_ISO_CODE,
            ANY_STATUS,
            ANY_TIME,
            ANY_TIME_UPDATE);

    var newName = new PartnerName("New Name");
    var newCity = new PartnerCity("New City");
    var newCountry = new PartnerCountry("NEW COUNTRY");
    var newIsoCode = new PartnerIsoCode("GBR");

    Partner updatedPartner = partner.updatePartner(newName, newCity, newCountry, newIsoCode);
    assertThat(updatedPartner).isNotSameAs(partner);
    assertThat(updatedPartner.id()).isEqualTo(partner.id());
    assertThat(updatedPartner.name()).isEqualTo(newName);
    assertThat(updatedPartner.city()).isEqualTo(newCity);
    assertThat(updatedPartner.country()).isEqualTo(newCountry);
    assertThat(updatedPartner.isoCode()).isEqualTo(newIsoCode);
    assertThat(updatedPartner.status()).isEqualTo(partner.status());
    assertThat(updatedPartner.createdAt()).isEqualTo(partner.createdAt());
  }

  @Test
  @DisplayName("createDraftPartner creates new Partner in status EDIT")
  void createDraftPartnerCreatesNewPartnerInStatusEdit() {

    var partner =
        Partner.createDraftPartner(
            ANY_ID,
            ANY_NAME.partnerName(),
            ANY_CITY.partnerCity(),
            ANY_COUNTRY.partnerCountry(),
            ANY_ISO_CODE.isoCode());

    assertThat(partner.status()).isEqualTo(PartnerStatus.EDIT);
    assertThat(partner.createdAt()).isNotNull();
    assertThat(partner.id()).isNotNull();
    assertThat(partner.name()).isEqualTo(ANY_NAME);
    assertThat(partner.city()).isEqualTo(ANY_CITY);
    assertThat(partner.country()).isEqualTo(ANY_COUNTRY);
    assertThat(partner.isoCode()).isEqualTo(ANY_ISO_CODE);
  }
}
