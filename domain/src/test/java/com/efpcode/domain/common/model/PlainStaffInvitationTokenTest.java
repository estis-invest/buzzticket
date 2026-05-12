package com.efpcode.domain.common.model;

import static org.assertj.core.api.Assertions.*;

import com.efpcode.domain.common.exceptions.InvalidCommonInvitationTokenException;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class PlainStaffInvitationTokenTest {
  private static final int MIN_LENGTH = 32;

  @Test
  @DisplayName("PlainStaffInvitationToken can create a valid object")
  void plainStaffInvitationTokenCanCreateAValidObject() {

    var longString = "a".repeat(8) + "B".repeat(8) + "c".repeat(8) + "D".repeat(8);

    var result = new PlainStaffInvitationToken(longString);

    assertThat(result.plainToken()).isNotNull().isEqualTo(longString);
  }

  @Test
  @DisplayName("Plain token cannot be smaller than 32 characters throws error")
  void plainTokenCannotBeSmallerThan32CharactersThrowsError() {
    var tooShortString = "a".repeat(31);

    assertThatThrownBy(() -> new PlainStaffInvitationToken(tooShortString))
        .isInstanceOf(InvalidCommonInvitationTokenException.class)
        .hasMessageContaining("Plain token must be at least " + MIN_LENGTH + " characters long");
  }

  private static Stream<Arguments> provideBlankAndNull() {
    return Stream.of(
        Arguments.of(" "),
        Arguments.of("   "),
        Arguments.of("\n"),
        Arguments.of("\t"),
        Arguments.of(" ".repeat(32)));
  }

  @ParameterizedTest
  @NullAndEmptySource
  @MethodSource("provideBlankAndNull")
  @DisplayName("StaffInvitationToken cannot construct object with null or blank throws error")
  void staffInvitationTokenCannotConstructObjectWithNullOrBlankThrowsError(String malformed) {

    assertThatThrownBy(() -> new PlainStaffInvitationToken(malformed))
        .isInstanceOf(InvalidCommonInvitationTokenException.class)
        .hasMessageContaining("Plain token cannot pass null or blank");
  }

  @Test
  @DisplayName("Plain token removes leading spaces and trailing spaces")
  void plainTokenRemovesLeadingSpacesAndTrailingSpaces() {

    var malformedToken = " " + "a".repeat(30) + " ";

    assertThatThrownBy(() -> new PlainStaffInvitationToken(malformedToken))
        .isInstanceOf(InvalidCommonInvitationTokenException.class)
        .hasMessageContaining("Plain token must be at least " + MIN_LENGTH + " characters long");
  }

  @Test
  @DisplayName("Plain token is normalized by trimming leading and trailing whitespace")
  void plainTokenIsNormalizedByTrimmingWhitespace() {

    var tokenWithWhitespace = " " + "a".repeat(32) + " ";

    var token = new PlainStaffInvitationToken(tokenWithWhitespace);

    assertThat(token.plainToken()).isEqualTo("a".repeat(32));
  }
}
