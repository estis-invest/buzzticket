package com.efpcode.domain.user.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.efpcode.domain.user.exceptions.InvalidUserCreatedAtException;
import com.efpcode.domain.user.exceptions.UserCreatedAtDateException;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserCreatedAtTest {

  @Test
  @DisplayName("UserCreatedAt cannot be null error")
  void userCreatedAtCannotBeNullThrowsError() {

    assertThatThrownBy(() -> new UserCreatedAt(null))
        .isInstanceOf(InvalidUserCreatedAtException.class)
        .hasMessageContaining("Time is required");
  }

  @Test
  @DisplayName("UserCreatedAt cannot pass zero userCreatedAt")
  void userCreatedAtCannotPassZeroTime() {

    var timeZero = Instant.ofEpochMilli(0);

    assertThatThrownBy(() -> new UserCreatedAt(timeZero))
        .isInstanceOf(InvalidUserCreatedAtException.class)
        .hasMessageContaining("Time is required");
  }

  @Test
  @DisplayName(
      "UserCreatedAt cannot create users in the future if createdAt exceed margin of error throws error")
  void userCreatedAtCannotCreateUsersInTheFutureIfTimeExceedMarginOfErrorThrowsError() {
    var futureTime = Instant.now().plusSeconds(90);

    assertThatThrownBy(() -> new UserCreatedAt(futureTime))
        .isInstanceOf(UserCreatedAtDateException.class)
        .hasMessageContaining("User cannot be created in the future");
  }

  @Test
  @DisplayName("UserCreated within grace period returns a valid object")
  void userCreatedWithinGracePeriodReturnsAValidObject() {

    var slightFutureInTime = Instant.now().plusSeconds(30);

    var result = new UserCreatedAt(slightFutureInTime);

    assertThat(result.time()).isNotNull().isAfterOrEqualTo(slightFutureInTime);
  }

  @Test
  @DisplayName("UserCreated with Instant now method returns valid object")
  void userCreatedWithInstantNowMethodReturnsValidObject() {

    var nowTime = Instant.now();

    var result = new UserCreatedAt(nowTime);

    assertThat(result.time()).isNotNull().isAfterOrEqualTo(nowTime);
  }

  @Test
  @DisplayName("UserCreatedAt createNow returns a valid object")
  void userCreatedAtCreateNowReturnsAValidObject() {

    UserCreatedAt results = UserCreatedAt.createNow();
    var now = Instant.now();
    assertThat(results).isNotNull().isInstanceOf(UserCreatedAt.class);
    assertThat(results.time()).isBeforeOrEqualTo(now);
    assertThat(results.time()).isAfter(now.minusSeconds(5));
  }

  @Test
  @DisplayName("UserCreatedAt object created from createNow are unique")
  void userCreatedAtObjectCreatedFromCreateNowAreUnique() {

    var time1 = UserCreatedAt.createNow();
    var time2 = UserCreatedAt.createNow();

    assertThat(time1).isNotSameAs(time2);
  }
}
