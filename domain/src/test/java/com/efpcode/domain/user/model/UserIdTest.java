package com.efpcode.domain.user.model;

import static org.assertj.core.api.Assertions.*;

import com.efpcode.domain.user.exceptions.InvalidUserIdException;
import java.util.UUID;
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
  @DisplayName("UserId method generate returns a valid UserId object")
  void userIdMethodCreateRandomReturnsAValidUserIdObject() {
    var result = UserId.generate();
    assertThat(result).isInstanceOf(UserId.class).isNotNull();
  }

  @Test
  @DisplayName("UserId method fromString returns a valid UserId object")
  void userIdMethodFromStringReturnsAValidUserIdObject() {

    var stringUUID = UUID.randomUUID().toString();

    var results = UserId.fromString(stringUUID);

    assertThat(results).isInstanceOf(UserId.class).isNotNull();
    assertThat(results.id()).isEqualTo(UUID.fromString(stringUUID));
  }
}
