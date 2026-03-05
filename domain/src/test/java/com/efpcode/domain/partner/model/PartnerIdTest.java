package com.efpcode.domain.partner.model;

import static org.assertj.core.api.Assertions.*;

import com.efpcode.domain.partner.exceptions.InvalidPartnerIdException;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PartnerIdTest {
  @Test
  @DisplayName("PartnerId cannot pass null throws exception")
  void partnerIdCannotPassNullThrowsException() {

    assertThatThrownBy(() -> new PartnerId(null))
        .isInstanceOf(InvalidPartnerIdException.class)
        .hasMessageContaining("Partner cannot be null");
  }

  @Test
  @DisplayName("PartnerId method generate returns a valid object")
  void partnerIdMethodGenerateReturnsAValidObject() {

    var result = PartnerId.generate();
    assertThat(result).isNotNull().isInstanceOf(PartnerId.class);
  }

  @Test
  @DisplayName("PartnerId method fromString returns a valid object")
  void partnerIdMethodFromStringReturnsAValidObject() {
    var uuidString = UUID.randomUUID().toString();
    PartnerId result = PartnerId.fromString(uuidString);
    assertThat(result).isNotNull().isInstanceOf(PartnerId.class);
    assertThat(result.partnerId().toString()).hasToString(uuidString);
  }
}
