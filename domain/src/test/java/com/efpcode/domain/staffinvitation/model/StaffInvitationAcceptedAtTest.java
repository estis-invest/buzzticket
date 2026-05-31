package com.efpcode.domain.staffinvitation.model;

import static org.assertj.core.api.Assertions.*;

import com.efpcode.domain.staffinvitation.exceptions.InvalidStaffInvitationDateException;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class StaffInvitationAcceptedAtTest {

  @Test
  @DisplayName("AcceptedAt invitation cannot be null throws erro")
  void acceptedAtInvitationCannotBeNullThrowsErro() {
    assertThatThrownBy(() -> new StaffInvitationAcceptedAt(null))
        .isInstanceOf(InvalidStaffInvitationDateException.class)
        .hasMessageContaining("Staff invitation requires date");
  }

  @Test
  @DisplayName("AcceptedAt invitation cannot pass zero as argument throws error")
  void acceptedAtInvitationCannotPassZeroAsArgumentThrowsError() {
    var timeZero = Instant.ofEpochMilli(0);
    assertThatThrownBy(() -> new StaffInvitationAcceptedAt(timeZero))
        .isInstanceOf(InvalidStaffInvitationDateException.class)
        .hasMessageContaining("Staff invitation requires date");
  }

  @Test
  @DisplayName("AcceptedAt cannot be created when margin is exceeded throws error")
  void acceptedAtCannotBeCreatedWhenMarginIsExceededThrowsError() {

    var futureTime = Instant.now().plusSeconds(90);

    assertThatThrownBy(() -> new StaffInvitationAcceptedAt(futureTime))
        .isInstanceOf(InvalidStaffInvitationDateException.class)
        .hasMessageContaining("Staff invitation cannot be created in the future");
  }

  @Test
  @DisplayName("AcceptedAt invitation can create a valid object")
  void acceptedAtInvitationCanCreateAValidObject() {

    var nowTime = Instant.now();
    var result = new StaffInvitationAcceptedAt(nowTime);

    assertThat(result.time()).isNotNull().isAfterOrEqualTo(nowTime);
  }
}
