package com.efpcode.domain.user.model;

import static org.assertj.core.api.Assertions.*;

import com.efpcode.domain.user.exceptions.InvalidUserNameException;
import com.efpcode.domain.user.exceptions.UserNameLengthException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserNameTest {

  @Test
  @DisplayName("UserName cannot pass blank or null throws error")
  void userNameCannotPassBlankOrNullThrowsError() {

    assertThatThrownBy(() -> new UserName(null))
        .isInstanceOf(InvalidUserNameException.class)
        .hasMessageContaining("cannot be null");

    assertThatThrownBy(() -> new UserName("  "))
        .isInstanceOf(InvalidUserNameException.class)
        .hasMessageContaining("or blank");
  }

  @Test
  @DisplayName("Username can create a valid object")
  void usernameCanCreateAValidObject() {

    var testName = "Test 1";
    var results = new UserName("Test 1");

    assertThat(results.name()).hasToString(testName);
    assertThat(results).isNotNull().isInstanceOf(UserName.class);
  }

  @Test
  @DisplayName("Username cannot exceed limit throws error")
  void usernameCannotExceedLimitThrowsError() {

    var testName = "a".repeat(51);

    assertThatThrownBy(() -> new UserName(testName))
        .isInstanceOf(UserNameLengthException.class)
        .hasMessageContaining("Username cannot exceed 50 limit");
  }

  @Test
  @DisplayName("Username below limit can return valid object")
  void usernameBelowLimitCanReturnValidObject() {

    var testName = "a".repeat(50);
    var result = new UserName(testName);

    assertThat(result.name()).hasToString(testName);
    assertThat(result.name().length()).isEqualTo(testName.length());
  }
}
