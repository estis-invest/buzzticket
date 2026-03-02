package com.efpcode.domain.user.model;

import static org.assertj.core.api.Assertions.*;

import com.efpcode.domain.user.exceptions.InvalidUserNameException;
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
}
