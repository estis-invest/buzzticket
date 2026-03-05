package com.efpcode.domain.user.model;

import static org.assertj.core.api.Assertions.*;

import com.efpcode.domain.user.exceptions.InvalidUserPasswordException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class UserPasswordTest {

  @Test
  @DisplayName("UserPassword cannot pass null throws error")
  void userPasswordCannotPassNullThrowsError() {

    assertThatThrownBy(() -> new UserPassword(null))
        .isInstanceOf(InvalidUserPasswordException.class)
        .hasMessageContaining("UserPassword cannot be null or blank");
  }

  @ParameterizedTest
  @ValueSource(strings = {"", "  "})
  @DisplayName("UserPassword cannot pass blank throws error")
  void UserPasswordCannotPassBlankThrowsError(String invalidPassword) {

    assertThatThrownBy(() -> new UserPassword(invalidPassword))
        .isInstanceOf(InvalidUserPasswordException.class)
        .hasMessageContaining("be null or blank");
  }

  @Test
  @DisplayName("UserPassword fromString methods returns a valid object")
  void userPasswordFromStringMethodsReturnsAValidObject() {
    var testHash = "$2a$10$762vH6.L...";
    var password = UserPassword.fromString(testHash);
    assertThat(password.hashedPassword()).isEqualTo(testHash);
  }

  @Test
  @DisplayName("UserPassword fromHash method returns a valid object")
  void userPasswordFromHashMethodReturnsAValidObject() {

    var testHash = "$2a$10$762vH6.L...";
    var password = UserPassword.fromHash(testHash);
    assertThat(password.hashedPassword()).isEqualTo(testHash);
  }
}
