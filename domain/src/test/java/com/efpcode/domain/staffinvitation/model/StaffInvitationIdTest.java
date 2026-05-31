package com.efpcode.domain.staffinvitation.model;

import static org.assertj.core.api.Assertions.*;

import com.efpcode.domain.staffinvitation.exceptions.IllegalStaffInvitationIdArgumentException;
import com.efpcode.domain.staffinvitation.exceptions.InvalidStaffInvitationIdException;
import com.efpcode.domain.testsupport.TestUUIDIds;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class StaffInvitationIdTest {
  @Test
  @DisplayName("StaffInvitationId cannot pass null throws exception")
  void staffInvitationIdCannotPassNullThrowsException() {
    assertThatThrownBy(() -> new StaffInvitationId(null))
        .isInstanceOf(InvalidStaffInvitationIdException.class)
        .hasMessageContaining("UUID is required and cannot be null");
  }

  @Test
  @DisplayName("StaffInvitationId method of returns a valid object")
  void staffInvitationIdMethodOfReturnsAValidObject() {
    var expected = TestUUIDIds.invitationId();

    var result = StaffInvitationId.of(expected.invitationId());

    assertThat(result).isNotNull().isInstanceOf(StaffInvitationId.class).isEqualTo(expected);
  }

  @Test
  @DisplayName("StaffInvitationId method fromString returns a valid object")
  void staffInvitationIdMethodFromStringReturnsAValidObject() {
    var uuidString = "00000000-0000-0000-0000-000000000001";
    var expected = TestUUIDIds.invitationId(uuidString);

    StaffInvitationId result = StaffInvitationId.fromString(uuidString);

    assertThat(result).isNotNull().isInstanceOf(StaffInvitationId.class);
    assertThat(result.invitationId()).isEqualTo(expected.invitationId());
  }

  private static Stream<Arguments> provideBlankAndNull() {
    return Stream.of(
        Arguments.of(" "), Arguments.of("   "), Arguments.of("\n"), Arguments.of("\t"));
  }

  @ParameterizedTest
  @NullAndEmptySource
  @MethodSource("provideBlankAndNull")
  @DisplayName("StaffInvitationId method fromString throws error if null or blank is passed")
  void staffInvitationIdMethodFromStringThrowsErrorIfNullOrBlankIsPassed(String uuidTest) {
    assertThatThrownBy(() -> StaffInvitationId.fromString(uuidTest))
        .isInstanceOf(IllegalStaffInvitationIdArgumentException.class)
        .hasMessageContaining("fromString method cannot pass null or blank");
  }

  @Test
  @DisplayName("malformatted uuid will throw error")
  void malformattedUuidWillThrowError() {
    var malFormattedUuID = "not-a-valid-uuid";

    assertThatThrownBy(() -> StaffInvitationId.fromString(malFormattedUuID))
        .isInstanceOf(InvalidStaffInvitationIdException.class)
        .hasMessageContaining("Invalid or malformatted uuid");
  }
}
