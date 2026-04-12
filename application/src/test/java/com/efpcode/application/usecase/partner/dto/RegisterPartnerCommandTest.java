package com.efpcode.application.usecase.partner.dto;

import static org.assertj.core.api.Assertions.*;

import com.efpcode.application.usecase.partner.exceptions.InvalidPartnerCommandArgumentException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class RegisterPartnerCommandTest {
  @Test
  @DisplayName("Should create valid command when all fields are provided")
  void shouldCreateValidCommand() {
    var command = new RegisterPartnerCommand("Name", "City", "Country", "TST");

    assertThat(command.name()).isEqualTo("Name");
    assertThat(command.isoCode()).isEqualTo("TST");
  }

  @ParameterizedTest(name = "Field {0} with value [{1}] should throw exception")
  @CsvSource({"Name, ''", "Name, ' '", "City, ", "Country, ''", "IsoCode, '  '"})
  @DisplayName("Should throw exception for null or blank fields")
  void shouldThrowExceptionForInvalidFields(String fieldName, String value) {
    assertThatThrownBy(
            () -> {
              if (fieldName.equals("Name")) new RegisterPartnerCommand(value, "C", "C", "I");
              if (fieldName.equals("City")) new RegisterPartnerCommand("N", value, "C", "I");
              if (fieldName.equals("Country")) new RegisterPartnerCommand("N", "C", value, "I");
              if (fieldName.equals("IsoCode")) new RegisterPartnerCommand("N", "C", "C", value);
            })
        .isInstanceOf(InvalidPartnerCommandArgumentException.class)
        .hasMessageContaining(fieldName + " is required");
  }
}
