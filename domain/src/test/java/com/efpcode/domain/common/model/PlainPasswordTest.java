package com.efpcode.domain.common.model;

import static org.assertj.core.api.Assertions.*;

import com.efpcode.domain.common.exceptions.InvalidCommonPasswordException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class PlainPasswordTest {

  private static final String ANY_PASSWORD = "!SECRET123456";

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {" ", "  ", "\n", "\t"})
  @DisplayName("PlainPassword does not accept blank or null as arguments")
  void plainPasswordDoesNotAcceptBlankOrNullAsArguments(String invalid) {

    assertThatThrownBy(() -> new PlainPassword(invalid))
        .isInstanceOf(InvalidCommonPasswordException.class)
        .hasMessageContaining("PlainPassword cannot pass null or blank as argument");
  }

  @Test
  @DisplayName("PlainPassword creates valid object from string")
  void plainPasswordCreatesValidObjectFromString() {
    var result = new PlainPassword(ANY_PASSWORD);

    assertThat(result.plainPassword()).isEqualTo(ANY_PASSWORD);
  }

  @Test
  @DisplayName("toString masks password with exactly 16 asterisks")
  void toStringMasksPasswordWithExactly16Asterisks() {

    PlainPassword password = new PlainPassword(ANY_PASSWORD);

    String result = password.toString();

    assertThat(result).isEqualTo("PlainPassword{plainPassword='****************'}");
  }

  @Test
  @DisplayName("PlainPassword has minium length and throws error if length is shorter")
  void plainPasswordHasMiniumLengthAndThrowsErrorIfLengthIsShorter() {

    var tooShortPassword = "!abF567";

    assertThatThrownBy(() -> new PlainPassword(tooShortPassword))
        .isInstanceOf(InvalidCommonPasswordException.class)
        .hasMessageContaining("Password needs to be equal or longer than: 8 characters");
  }

  @Test
  @DisplayName("PlainPassword requires at lease one uppercase character")
  void plainPasswordRequiresAtLeaseOneUppercaseCharacter() {
    var noUppercasePassword = "123secret!";

    assertThatThrownBy(() -> new PlainPassword(noUppercasePassword))
        .isInstanceOf(InvalidCommonPasswordException.class)
        .hasMessageContaining("Password must contain at least one uppercase letter");
  }

  @Test
  @DisplayName("PlainPassword requires at least one digit")
  void plainPasswordRequiresAtLeastOneDigit() {

    var noDigitPassword = "Secretabc";

    assertThatThrownBy(() -> new PlainPassword(noDigitPassword))
        .isInstanceOf(InvalidCommonPasswordException.class)
        .hasMessageContaining("Password must contain at least one digit");
  }

  @Test
  @DisplayName("PlainPassword requires at least on symbol")
  void plainPasswordRequiresAtLeastOnSymbol() {
    var noSymbolPassword = "Secret123";

    assertThatThrownBy(() -> new PlainPassword(noSymbolPassword))
        .isInstanceOf(InvalidCommonPasswordException.class)
        .hasMessageContaining("Password must contain at least one symbol");
  }
}
