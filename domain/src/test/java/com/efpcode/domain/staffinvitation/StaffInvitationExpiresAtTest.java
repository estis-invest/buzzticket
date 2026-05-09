package com.efpcode.domain.staffinvitation;

import static org.assertj.core.api.Assertions.*;

import com.efpcode.domain.staffinvitation.exceptions.InvalidStaffInvitationDateException;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class StaffInvitationExpiresAtTest {

  @Test
  @DisplayName("ExpiresAt invitation cannot be null throws erro")
  void expiresAtInvitationCannotBeNullThrowsErro() {
    assertThatThrownBy(() -> new StaffInvitationExpiresAt(null))
        .isInstanceOf(InvalidStaffInvitationDateException.class)
        .hasMessageContaining("Staff invitation expiry date is required");
  }

  @Test
  @DisplayName("ExpiresAt invitation cannot pass zero as argument throws error")
  void expiresAtInvitationCannotPassZeroAsArgumentThrowsError() {
    var timeZero = Instant.ofEpochMilli(0);

    assertThatThrownBy(() -> new StaffInvitationExpiresAt(timeZero))
        .isInstanceOf(InvalidStaffInvitationDateException.class)
        .hasMessageContaining("Staff invitation expiry date is required");
  }

  @Test
  @DisplayName("ExpiresAt cannot be created when current time is null throws error")
  void expiresAtCannotBeCreatedWhenCurrentTimeIsNullThrowsError() {
    var expiryTime = Instant.now().plusSeconds(90);

    assertThatThrownBy(() -> StaffInvitationExpiresAt.of(expiryTime, null))
        .isInstanceOf(InvalidStaffInvitationDateException.class)
        .hasMessageContaining("Current time is required");
  }

  @Test
  @DisplayName("ExpiresAt cannot be created when current time is zero throws error")
  void expiresAtCannotBeCreatedWhenCurrentTimeIsZeroThrowsError() {
    var expiryTime = Instant.now().plusSeconds(90);
    var zeroTime = Instant.ofEpochMilli(0);
    assertThatThrownBy(() -> StaffInvitationExpiresAt.of(expiryTime, zeroTime))
        .isInstanceOf(InvalidStaffInvitationDateException.class)
        .hasMessageContaining("Current time is required");
  }

  @Test
  @DisplayName("ExpiresAt cannot be created before minimum expiry throws error")
  void expiresAtCannotBeCreatedBeforeMinimumExpiryThrowsError() {
    var nowTime = Instant.now();
    var timeThreshold = 60;
    var expiryTime = nowTime.plusSeconds(30);

    assertThatThrownBy(() -> StaffInvitationExpiresAt.of(expiryTime, nowTime))
        .isInstanceOf(InvalidStaffInvitationDateException.class)
        .hasMessageContaining(
            String.format(
                "Staff invitation expiry must be at least %d seconds in the future",
                timeThreshold));
  }

  @Test
  @DisplayName("ExpiresAt invitation can create a valid object")
  void expiresAtInvitationCanCreateAValidObject() {
    var nowTime = Instant.now();
    var expiryTime = nowTime.plusSeconds(60);
    var result = StaffInvitationExpiresAt.of(expiryTime, nowTime);

    assertThat(result.time()).isNotNull().isAfterOrEqualTo(expiryTime);
  }
}
