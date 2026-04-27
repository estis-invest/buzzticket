package com.efpcode.domain.user.model;

import static org.assertj.core.api.Assertions.*;

import com.efpcode.domain.user.exceptions.InvalidUserUpdateAtException;
import com.efpcode.domain.user.exceptions.UserUpdateAtDateException;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserUpdateAtTest {

  @Test
  @DisplayName("UserUpdateAt cannot be null")
  void userUpdateAtCannotBeNull() {
    assertThatThrownBy(() -> new UserUpdateAt(null))
        .isInstanceOf(InvalidUserUpdateAtException.class)
        .hasMessageContaining("Update timestamp is required");
  }

  @Test
  @DisplayName("UserUpdateAt cannot pass zero updatedAt as argument")
  void userUpdateAtCannotPassZeroUpdatedAtAsArgument() {
    var timeZero = Instant.ofEpochMilli(0);
    assertThatThrownBy(() -> new UserUpdateAt(timeZero))
        .isInstanceOf(InvalidUserUpdateAtException.class)
        .hasMessageContaining("Update timestamp is required");
  }

  @Test
  @DisplayName("UserUpdateAt cannot be in the future beyond margin of error")
  void userUpdateAtCannotBeInTheFutureBeyondMarginOfError() {
    var futureTime = Instant.now().plusSeconds(90);
    assertThatThrownBy(() -> new UserUpdateAt(futureTime))
        .isInstanceOf(UserUpdateAtDateException.class)
        .hasMessageContaining("Update timestamp cannot be in the future");
  }

  @Test
  @DisplayName("UserUpdateAt within grace period returns a valid object")
  void userUpdateAtWithinGracePeriodReturnsAValidObject() {
    var slightFutureInTime = Instant.now().plusSeconds(30);
    var result = new UserUpdateAt(slightFutureInTime);
    assertThat(result).isNotNull().isInstanceOf(UserUpdateAt.class);
    assertThat(result.updatedAt()).isEqualTo(slightFutureInTime);
  }

  @Test
  @DisplayName("UserUpdateAt created with current updatedAt returns a valid object")
  void userUpdateAtWithInstantNewMethodReturnsAValidObject() {
    var now = Instant.now();
    var result = new UserUpdateAt(now);
    assertThat(result).isNotNull().isInstanceOf(UserUpdateAt.class);
    assertThat(result.updatedAt()).isEqualTo(now);
    assertThat(result.updatedAt()).isBeforeOrEqualTo(now.plusSeconds(5));
  }

  @Test
  @DisplayName("UserUpdateAt createNow returns a valid object")
  void userUpdateAtCreateNowReturnAValidObject() {
    var before = Instant.now();
    var result = UserUpdateAt.createNow();
    var after = Instant.now();
    assertThat(result).isNotNull().isInstanceOf(UserUpdateAt.class);
    assertThat(result.updatedAt()).isAfterOrEqualTo(before);
    assertThat(result.updatedAt()).isBeforeOrEqualTo(after);
  }

  @Test
  @DisplayName("UserUpdateAt objects created from createNow are unique")
  void userUpdateAtObjectCreatedFromCreateNowAreUnique() {
    var time1 = UserUpdateAt.createNow();
    var time2 = UserUpdateAt.createNow();
    assertThat(time1).isNotSameAs(time2);
  }
}
