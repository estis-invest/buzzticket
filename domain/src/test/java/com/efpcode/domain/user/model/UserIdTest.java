package com.efpcode.domain.user.model;

import static org.assertj.core.api.Assertions.*;

import com.efpcode.domain.testsupport.TestUUIDIds;
import com.efpcode.domain.user.exceptions.InvalidUserIdException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserIdTest {

  @Test
  @DisplayName("UserId cannot pass null throws error")
  void userIdCannotPassNullThrowsError() {

    assertThatThrownBy(() -> new UserId(null))
        .isInstanceOf(InvalidUserIdException.class)
        .hasMessageContaining("cannot pass null");
  }

  @Test
  @DisplayName("UserId method of returns a valid UserId object")
  void userIdMethodCreateRandomReturnsAValidUserIdObject() {
    var result = TestUUIDIds.userId();
    assertThat(result).isInstanceOf(UserId.class).isNotNull();
  }

  @Test
  @DisplayName("UserId method fromString returns a valid UserId object")
  void userIdMethodFromStringReturnsAValidUserIdObject() {

    var stringUUID = "00000000-0000-0000-0000-000000000001";
    var expected = TestUUIDIds.userId(stringUUID);

    var results = UserId.fromString(stringUUID);

    assertThat(results).isInstanceOf(UserId.class).isNotNull();
    assertThat(results.id()).isEqualTo(expected.id());
  }
}
