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
    var upperLimit = 254;
    var testEmail = "test@" + "a".repeat(246) + ".xyz";

    assertThatThrownBy(() -> new UserEmail(testEmail))
        .isInstanceOf(UserEmailLengthException.class)
        .hasMessageContaining(
            "Email exceeds character limit of "
                + upperLimit
                + ". Email length: "
                + testEmail.length());
  }

  @Test
  @DisplayName("UserEmail throws error if local exceeds limit")
  void userEmailThrowsErrorIfLocalExceedsLimit() {

    var upperLimit = 64;
    var testEmail = "a".repeat(65) + "@" + "domain" + ".xyz";

    assertThatThrownBy(() -> new UserEmail(testEmail))
        .isInstanceOf(UserEmailLengthException.class)
        .hasMessageContaining("Local-part (before @) exceeds " + upperLimit + " characters");
  }

  @Test
  @DisplayName("UserEmail throws error if domain exceeds limit")
  void userEmailThrowsErrorIfDomainExceedsLimit() {
    var upperLimit = 255;
    var testEmail = "a@" + "a".repeat(255) + ".io";

    assertThatThrownBy(() -> new UserEmail(testEmail))
        .isInstanceOf(UserEmailLengthException.class)
        .hasMessageContaining("Domain-part (after @) exceeds " + upperLimit + " characters");
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

  @Test
  @DisplayName("UserEmail accepts exactly 64 characters in local part")
  void userEmailAcceptsExactLocalLimit() {
    String local64 = "a".repeat(64) + "@domain.io";
    assertThatCode(() -> new UserEmail(local64)).doesNotThrowAnyException();
  }

  @Test
  @DisplayName("UserEmail accepts exactly 254 characters total")
  void userEmailAcceptsExactTotalLimit() {
    String total254 = "a".repeat(64) + "@" + "d".repeat(185) + ".com";
    assertThat(total254.length()).isEqualTo(254);
    assertThatCode(() -> new UserEmail(total254)).doesNotThrowAnyException();
  }
}
