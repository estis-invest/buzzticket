package com.efpcode.domain.staffinvitation.model;

import static org.assertj.core.api.Assertions.*;

import com.efpcode.domain.staffinvitation.exceptions.InvalidStaffInvitationDateException;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class StaffInvitationCreatedAtTest {

  @Test
  @DisplayName("CreatedAt invitation cannot be null throws error")
  void createdAtInvitationCannotBeNullThrowsError() {
    assertThatThrownBy(() -> new StaffInvitationCreatedAt(null))
        .isInstanceOf(InvalidStaffInvitationDateException.class)
        .hasMessageContaining("Staff invitation requires date");
  }

  @Test
  @DisplayName("CreatedAt invitation cannot pass zero as argument throws error")
  void createdAtInvitationCannotPassZeroAsArgumentThrowsError() {
    var timeZero = Instant.ofEpochMilli(0);

    assertThatThrownBy(() -> new StaffInvitationCreatedAt(timeZero))
        .isInstanceOf(InvalidStaffInvitationDateException.class)
        .hasMessageContaining("Staff invitation requires date");
  }

  @Test
  @DisplayName("CreatedAt cannot be created when margin is exceeded throws error")
  void createdAtCannotBeCreatedWhenMarginIsExceededThrowsError() {
    var futureTime = Instant.now().plusSeconds(90);

    assertThatThrownBy(() -> new StaffInvitationCreatedAt(futureTime))
        .isInstanceOf(InvalidStaffInvitationDateException.class)
        .hasMessageContaining("Staff invitation cannot be created in the future");
  }

  @Test
  @DisplayName("CreatedAt invitation can create a valid object")
  void createdAtInvitationCanCreateAValidObject() {
    var nowTime = Instant.now();
    var result = new StaffInvitationCreatedAt(nowTime);

    assertThat(result.time()).isNotNull().isAfterOrEqualTo(nowTime);
  }
}
