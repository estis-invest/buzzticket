package com.efpcode.domain.staffinvitation.model;

import static org.assertj.core.api.Assertions.*;

import com.efpcode.domain.staffinvitation.exceptions.InvalidStaffInvitationTokenException;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class StaffInvitationTokenHashTest {

  @Test
  @DisplayName("Staff invitation token creates a valid object")
  void staffInvitationTokenCreatesAValidObject() {
    String hash = "3b9c1ec7f06b0d9f2c1a9e3a07a6f28d0d8c9f4b2e1c6a98ff10a3b7c9d1e2f";

    var result = new StaffInvitationTokenHash(hash);

    assertThat(result.value()).isNotNull().isEqualTo(hash);
  }

  private static Stream<Arguments> provideBlankAndNull() {
    return Stream.of(
        Arguments.of(" "), Arguments.of("   "), Arguments.of("\n"), Arguments.of("\t"));
  }

  @ParameterizedTest
  @NullAndEmptySource
  @MethodSource("provideBlankAndNull")
  @DisplayName("Invitation token cannot be created if null or blank is passed throws error")
  void invitationTokenCannotBeCreatedIfNullOrBlankIsPassedThrowsError(String malformed) {

    assertThatThrownBy(() -> new StaffInvitationTokenHash(malformed))
        .isInstanceOf(InvalidStaffInvitationTokenException.class)
        .hasMessageContaining("Invitation token hash is required");
  }

  @ParameterizedTest
  @NullAndEmptySource
  @MethodSource("provideBlankAndNull")
  @DisplayName(
      "Invitation token cannot be created if null or blank is passed throws error with of Hashed method")
  void invitationTokenCannotBeCreatedIfNullOrBlankIsPassedThrowsErrorWithOfHashedMethod(
      String malformed) {

    assertThatThrownBy(() -> StaffInvitationTokenHash.of(malformed))
        .isInstanceOf(InvalidStaffInvitationTokenException.class)
        .hasMessageContaining("Invitation token hash is required");
  }
}
