package com.efpcode.domain.partner.model;

import static org.assertj.core.api.Assertions.*;

import com.efpcode.domain.partner.exceptions.IllegalPartnerIdArgumentException;
import com.efpcode.domain.partner.exceptions.InvalidPartnerIdException;
import com.efpcode.domain.testsupport.TestUUIDIds;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

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
    var expected = TestUUIDIds.partnerId();

    var result = PartnerId.of(expected.partnerId());
    assertThat(result).isNotNull().isInstanceOf(PartnerId.class).isEqualTo(expected);
  }

  @Test
  @DisplayName("PartnerId method fromString returns a valid object")
  void partnerIdMethodFromStringReturnsAValidObject() {
    var uuidString = "00000000-0000-0000-0000-000000000001";
    var expected = TestUUIDIds.partnerId(uuidString);
    PartnerId result = PartnerId.fromString(uuidString);
    assertThat(result).isNotNull().isInstanceOf(PartnerId.class);
    assertThat(result.partnerId().toString()).hasToString(expected.partnerId().toString());
  }

  private static Stream<Arguments> provideBlankAndNull() {
    return Stream.of(
        Arguments.of(" "), Arguments.of("   "), Arguments.of("\n"), Arguments.of("\t"));
  }

  @ParameterizedTest
  @NullAndEmptySource
  @MethodSource("provideBlankAndNull")
  @DisplayName("PartnerId method fromString throws error if null or blank is passed")
  void partnerIdMethodFromStringThrowsErrorIfNullOrBlankIsPassed(String uuidTest) {
    assertThatThrownBy(() -> PartnerId.fromString(uuidTest))
        .isInstanceOf(IllegalPartnerIdArgumentException.class)
        .hasMessageContaining("fromString method cannot pass null or blank");
  }
}
