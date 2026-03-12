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
}
