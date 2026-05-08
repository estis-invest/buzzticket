package com.efpcode.domain.staffinvitation;

import static org.assertj.core.api.Assertions.*;

import com.efpcode.domain.staffinvitation.exceptions.InvalidStaffInvitationStatusException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class StaffInvitationStatusTest {

  @Test
  @DisplayName("Staff invitation status can be accepted if status is in PENDING")
  void staffInvitationStatusCanBeAcceptedIfStatusIsInPending() {
    var inviteStatus = StaffInvitationStatus.PENDING;

    var result = inviteStatus.accept();

    assertThat(result).isEqualTo(StaffInvitationStatus.ACCEPTED).isNotEqualTo(inviteStatus);
  }

  @Test
  @DisplayName("Staff invitation can transition to EXPIRED from PENDING")
  void staffInvitationCanTransitionToExpiredFromPending() {
    var inviteStatus = StaffInvitationStatus.PENDING;
    var result = inviteStatus.expire();

    assertThat(result).isEqualTo(StaffInvitationStatus.EXPIRED).isNotEqualTo(inviteStatus);
  }

  @ParameterizedTest
  @EnumSource(
      value = StaffInvitationStatus.class,
      names = {"ACCEPTED", "EXPIRED"})
  @DisplayName("accept method throws error if invitation status is other than PENDING")
  void acceptMethodThrowsErrorIfInvitationStatusIsOtherThanPending(StaffInvitationStatus status) {

    assertThatThrownBy(status::accept)
        .isInstanceOf(InvalidStaffInvitationStatusException.class)
        .hasMessageContaining("Invitation cannot be accepted when status is " + status.name());
  }

  @ParameterizedTest
  @EnumSource(
      value = StaffInvitationStatus.class,
      names = {"ACCEPTED", "EXPIRED"})
  @DisplayName("expire method throws error if invitation status is other than PENDING")
  void expireMethodThrowsErrorIfInvitationStatusIsOtherThanPending(StaffInvitationStatus status) {
    assertThatThrownBy(status::expire)
        .isInstanceOf(InvalidStaffInvitationStatusException.class)
        .hasMessageContaining("Invitation cannot be expired when status is " + status.name());
  }
}
