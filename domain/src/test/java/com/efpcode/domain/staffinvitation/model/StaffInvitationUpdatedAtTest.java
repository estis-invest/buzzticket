package com.efpcode.domain.staffinvitation.model;

import static org.assertj.core.api.Assertions.*;

import com.efpcode.domain.staffinvitation.exceptions.InvalidStaffInvitationDateException;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class StaffInvitationUpdatedAtTest {

  @Test
  @DisplayName("UpdatedAt invitation cannot be null throws error")
  void updatedAtInvitationCannotBeNullThrowsError() {
    assertThatThrownBy(() -> new StaffInvitationUpdatedAt(null))
        .isInstanceOf(InvalidStaffInvitationDateException.class)
        .hasMessageContaining("Staff invitation requires date");
  }

  @Test
  @DisplayName("UpdatedAt invitation cannot pass zero as argument throws error")
  void updatedAtInvitationCannotPassZeroAsArgumentThrowsError() {
    var timeZero = Instant.ofEpochMilli(0);

    assertThatThrownBy(() -> new StaffInvitationUpdatedAt(timeZero))
        .isInstanceOf(InvalidStaffInvitationDateException.class)
        .hasMessageContaining("Staff invitation requires date");
  }

  @Test
  @DisplayName("UpdatedAt cannot be created when margin is exceeded throws error")
  void updatedAtCannotBeCreatedWhenMarginIsExceededThrowsError() {
    var futureTime = Instant.now().plusSeconds(90);

    assertThatThrownBy(() -> new StaffInvitationUpdatedAt(futureTime))
        .isInstanceOf(InvalidStaffInvitationDateException.class)
        .hasMessageContaining("Staff invitation updated-at cannot be in the future");
  }

  @Test
  @DisplayName("UpdatedAt invitation can create a valid object")
  void updatedAtInvitationCanCreateAValidObject() {
    var nowTime = Instant.now();
    var result = new StaffInvitationUpdatedAt(nowTime);

    assertThat(result.time()).isNotNull().isAfterOrEqualTo(nowTime);
  }
}
