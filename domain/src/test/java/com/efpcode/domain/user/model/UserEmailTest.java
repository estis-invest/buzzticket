package com.efpcode.domain.user.model;

import static org.assertj.core.api.Assertions.*;

import com.efpcode.domain.user.exceptions.InvalidUserEmailException;
import com.efpcode.domain.user.exceptions.UserEmailFormatException;
import com.efpcode.domain.user.exceptions.UserEmailLengthException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class UserEmailTest {

  @ParameterizedTest
  @ValueSource(strings = {"test", "test@", "test@test", "test@test.", "test@test.a"})
  @DisplayName("UserEmail throws error when format is not followed")
  void userEmailThrowsErrorWhenFormatIsNotFollowed(String invalidEmail) {

    assertThatThrownBy(() -> new UserEmail(invalidEmail))
        .isInstanceOf(UserEmailFormatException.class)
        .hasMessageContaining("user@domain.io");
  }

  @Test
  @DisplayName("UserEmail cannot pass null throws error")
  void userEmailCannotPassNullThrowsError() {

    assertThatThrownBy(() -> new UserEmail(null))
        .isInstanceOf(InvalidUserEmailException.class)
        .hasMessageContaining("User email cannot be null");
  }

  @ParameterizedTest
  @ValueSource(strings = {"", "  ", "   "})
  @DisplayName("UserEmail cannot pass blank throws error")
  void userEmailCannotPassBlankThrowsError(String invalidEmail) {
    assertThatThrownBy(() -> new UserEmail(invalidEmail))
        .isInstanceOf(InvalidUserEmailException.class)
        .hasMessageContaining("cannot be null or blank");
  }

  @Test
  @DisplayName("UserEmail throws error if email exceeds limit")
  void userEmailThrowsErrorIfEmailExceedsLimit() {
    var testEmail = "test@" + "a".repeat(57) + ".xyz";

    assertThatThrownBy(() -> new UserEmail(testEmail))
        .isInstanceOf(UserEmailLengthException.class)
        .hasMessageContaining("Email exceeds character limit of 64");
  }

  @Test
  @DisplayName("UserEmail returns a valid object")
  void userEmailReturnsAValidObject() {

    var testEmail = "user.test-99@test1-test2.io";

    var results = new UserEmail(testEmail);

    assertThat(results.email()).hasToString(testEmail);
    assertThat(results).isNotNull();
    assertThat(results.email()).hasSameSizeAs(testEmail);
  }
}
