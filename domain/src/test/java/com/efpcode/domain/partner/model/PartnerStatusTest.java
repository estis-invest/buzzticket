package com.efpcode.domain.partner.model;

import static org.assertj.core.api.Assertions.*;

import com.efpcode.domain.partner.exceptions.IllegalPartnerStatusTransitionException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class PartnerStatusTest {

  @ParameterizedTest
  @EnumSource(
      value = PartnerStatus.class,
      names = {"DEACTIVATED", "EDIT", "DELETED"})
  @DisplayName("isActive methods return false for all other partner status than ACTIVE")
  void isActiveMethodsReturnFalseForAllOtherPartnerStatusThanActive(PartnerStatus status) {

    assertThat(status.isActive()).isFalse();
  }

  @Test
  @DisplayName("isActive only true for ACTIVE PartnerStatus")
  void isActiveOnlyTrueForActivePartnerStatus() {
    assertThat(PartnerStatus.ACTIVE.isActive()).isTrue();
  }

  @ParameterizedTest
  @EnumSource(
      value = PartnerStatus.class,
      names = {"ACTIVE", "EDIT", "DEACTIVATED"})
  @DisplayName("isDeleted only true for DELETED PartnerStatus all other return false")
  void isDeletedOnlyTrueForDeletedPartnerStatusAllOtherReturnFalse(PartnerStatus status) {
    assertThat(status.isDeleted()).isFalse();
  }

  @Test
  @DisplayName("isDeleted true for PartnerStatus DELETED")
  void isDeletedTrueForPartnerStatusDeleted() {
    assertThat(PartnerStatus.DELETED.isDeleted()).isTrue();
  }

  @ParameterizedTest
  @EnumSource(
      value = PartnerStatus.class,
      names = {"ACTIVE", "DELETED", "DEACTIVATED"})
  @DisplayName("isEdit only true for EDIT PartnerStatus all other return false")
  void isEditOnlyTrueForEditPartnerStatusAllOtherReturnFalse(PartnerStatus status) {
    assertThat(status.isEdit()).isFalse();
  }

  @Test
  @DisplayName("isEdit true for EDIT PartnerStatus")
  void isEditTrueForEditPartnerStatus() {
    assertThat(PartnerStatus.EDIT.isEdit()).isTrue();
  }

  @ParameterizedTest
  @EnumSource(
      value = PartnerStatus.class,
      names = {"ACTIVE", "EDIT", "DELETED"})
  @DisplayName("isDeactivated only true for DEACTIVATED PartnerStatus all other return false")
  void isDeactivatedOnlyTrueForDeactivatedPartnerStatusAllOtherReturnFalse(PartnerStatus status) {
    assertThat(status.isDeactivated()).isFalse();
  }

  @Test
  @DisplayName("isDeactivated return true for DEACTIVATED PartnerStatus")
  void isDeactivatedReturnTrueForDeactivatedPartnerStatus() {
    assertThat(PartnerStatus.DEACTIVATED.isDeactivated()).isTrue();
  }

  @ParameterizedTest
  @EnumSource(
      value = PartnerStatus.class,
      names = {"DEACTIVATED", "EDIT", "DELETED"})
  @DisplayName("toDeactivate throws error if PartnerStatus is other then ACTIVE ")
  void toDeactivateThrowsErrorIfPartnerStatusIsOtherThenActive(PartnerStatus status) {

    assertThatThrownBy(status::toDeactivate)
        .isInstanceOf(IllegalPartnerStatusTransitionException.class)
        .hasMessageContaining(
            String.format(
                "PartnerStatus %s cannot be deactivated. Only ACTIVE PartnerStatus can transition to DEACTIVATED",
                status.name()));
  }

  @Test
  @DisplayName("toDeactivate return DEACTIVATED from ACTIVE PartnerStatus")
  void toDeactivateReturnDeactivatedFromActivePartnerStatus() {

    var partnerStatus = PartnerStatus.ACTIVE;
    var result = partnerStatus.toDeactivate();
    assertThat(result).isNotSameAs(partnerStatus);
    assertThat(result.isDeactivated()).isTrue();
    assertThat(result).isEqualTo(PartnerStatus.DEACTIVATED);
  }

  @ParameterizedTest
  @EnumSource(
      value = PartnerStatus.class,
      names = {"ACTIVE", "EDIT", "DELETED"})
  @DisplayName("toDelete throws error if PartnerStatus is other then DEACTIVATED")
  void toDeleteThrowsErrorIfPartnerStatusIsOtherThenDeactivated(PartnerStatus status) {
    assertThatThrownBy(status::toDelete)
        .isInstanceOf(IllegalPartnerStatusTransitionException.class)
        .hasMessageContaining(
            String.format(
                "PartnerStatus %s cannot be deleted. Only DEACTIVATED PartnerStatus can transition to DELETED",
                status.name()));
  }

  @Test
  @DisplayName("toDelete return DELETED from DEACTIVATED PartnerStatus")
  void toDeleteReturnDeletedFromDeactivatedPartnerStatus() {
    var partnerStatus = PartnerStatus.DEACTIVATED;
    var result = partnerStatus.toDelete();
    assertThat(result).isNotSameAs(partnerStatus);
    assertThat(result.isDeleted()).isTrue();
    assertThat(result).isEqualTo(PartnerStatus.DELETED);
  }

  @ParameterizedTest
  @EnumSource(
      value = PartnerStatus.class,
      names = {"ACTIVE", "DELETED"})
  @DisplayName("toActivate throws error for all PartnerStatus except DEACTIVATED and EDIT")
  void toActivateThrowsErrorForAllPartnerStatusExceptDeactivatedAndEdit(PartnerStatus status) {
    assertThatThrownBy(status::toActivate)
        .isInstanceOf(IllegalPartnerStatusTransitionException.class)
        .hasMessageContaining(
            String.format(
                "PartnerStatus %s cannot be activated. Only DEACTIVATED and EDIT PartnerStatus can transition to ACTIVE",
                status.name()));
  }

  @ParameterizedTest
  @EnumSource(
      value = PartnerStatus.class,
      names = {"DEACTIVATED", "EDIT"})
  @DisplayName("toActive return ACTIVE from PartnerStatus DEACTIVATED and EDIT")
  void toActiveReturnActiveFromPartnerStatusDeactivatedAndEdit(PartnerStatus status) {
    var result = status.toActivate();
    assertThat(result).isNotSameAs(status);
    assertThat(result.isActive()).isTrue();
    assertThat(result).isEqualTo(PartnerStatus.ACTIVE);
  }

  @ParameterizedTest
  @EnumSource(
      value = PartnerStatus.class,
      names = {"DELETED", "EDIT", "DEACTIVATED"})
  @DisplayName("toEdit return EDIT PartnerStatus only for ACTIVE status else throws error")
  void toEditReturnEditPartnerStatusOnlyForActiveStatusElseThrowsError(PartnerStatus status) {

    assertThatThrownBy(status::toEdit)
        .isInstanceOf(IllegalPartnerStatusTransitionException.class)
        .hasMessageContaining(
            String.format(
                "PartnerStatus %s cannot be edited. Only ACTIVE PartnerStatus can transition to EDIT",
                status.name()));
  }

  @Test
  @DisplayName("toEdit return EDIT PartnerStatus only for ACTIVE status")
  void toEditReturnEditPartnerStatusOnlyForActiveStatus() {
    var result = PartnerStatus.ACTIVE.toEdit();
    assertThat(result).isNotSameAs(PartnerStatus.ACTIVE);
    assertThat(result.isEdit()).isTrue();
    assertThat(result).isEqualTo(PartnerStatus.EDIT);
  }

  @ParameterizedTest
  @EnumSource(
      value = PartnerStatus.class,
      names = {"DELETED", "EDIT", "DEACTIVATED"})
  @DisplayName("canBeEdit is true only for ACTIVE status")
  void canBeEditIsTrueOnlyForActiveStatus(PartnerStatus status) {
    assertThat(status.canBeEdit()).isFalse();
  }

  @Test
  @DisplayName("canBeEdit true for ACTIVE PartnerStatus")
  void canBeEditTrueForActivePartnerStatus() {
    assertThat(PartnerStatus.ACTIVE.canBeEdit()).isTrue();
  }

  @ParameterizedTest
  @EnumSource(
      value = PartnerStatus.class,
      names = {"DELETED", "EDIT", "DEACTIVATED"})
  @DisplayName("partnerStatusEditGuard throws error if status is not ACTIVE")
  void partnerStatusEditGuardThrowsErrorIfStatusIsNotActive(PartnerStatus status) {
    assertThatThrownBy(status::partnerStatusEditGuard)
        .isInstanceOf(IllegalPartnerStatusTransitionException.class)
        .hasMessageContaining(String.format("PartnerStatus: %s cannot be edited.", status.name()));
  }

  @Test
  @DisplayName("partnerStatusEditGuard does not throw error if status is ACTIVE")
  void partnerStatusEditGuardDoesNotThrowErrorIfStatusIsActive() {
    assertThatCode(PartnerStatus.ACTIVE::partnerStatusEditGuard).doesNotThrowAnyException();
  }
}
