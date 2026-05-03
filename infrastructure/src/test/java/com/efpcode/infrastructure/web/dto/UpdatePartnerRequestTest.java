package com.efpcode.infrastructure.web.dto;

import static org.assertj.core.api.Assertions.*;

import com.efpcode.application.usecase.partner.exceptions.InvalidPartnerCommandArgumentException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UpdatePartnerRequestTest {

  @Test
  @DisplayName("Update Partner: Constructor should throw if all fields are blank")
  void shouldThrowExceptionWhenAllFieldsAreBlank() {
    assertThatThrownBy(() -> new UpdatePartnerRequest("", "", "", ""))
        .isInstanceOf(InvalidPartnerCommandArgumentException.class)
        .hasMessageContaining("At least one field must be provided");
  }
}
