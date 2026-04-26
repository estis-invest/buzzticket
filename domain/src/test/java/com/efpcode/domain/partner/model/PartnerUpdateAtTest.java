package com.efpcode.domain.partner.model;

import static org.assertj.core.api.Assertions.*;

import com.efpcode.domain.partner.exceptions.InvalidPartnerUpdateAtException;
import com.efpcode.domain.partner.exceptions.PartnerUpdateAtDateException;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PartnerUpdateAtTest {
  @Test
  @DisplayName("PartnerUpdateAt cannot be null")
  void partnerUpdateAtCannotBeNull() {
    assertThatThrownBy(() -> new PartnerUpdateAt(null))
        .isInstanceOf(InvalidPartnerUpdateAtException.class)
        .hasMessageContaining("Update timestamp is required");
  }

  @Test
  @DisplayName("PartnerUpdateAt cannot pass zero createdAt as argument")
  void partnerUpdateAtCannotPassZeroTimeAsArgument() {
    var timeZero = Instant.ofEpochMilli(0);
    assertThatThrownBy(() -> new PartnerUpdateAt(timeZero))
        .isInstanceOf(InvalidPartnerUpdateAtException.class)
        .hasMessageContaining("Update timestamp is required");
  }

  @Test
  @DisplayName("PartnerUpdateAt cannot be in the future beyond margin of error")
  void partnerUpdateAtCannotBeInTheFutureBeyondMarginOfError() {
    var futureTime = Instant.now().plusSeconds(90);
    assertThatThrownBy(() -> new PartnerUpdateAt(futureTime))
        .isInstanceOf(PartnerUpdateAtDateException.class)
        .hasMessageContaining("Update timestamp cannot be in the future");
  }

  @Test
  @DisplayName("PartnerUpdateAt within grace period returns a valid object")
  void partnerUpdateAtWithinGracePeriodReturnsAValidObject() {
    var slightFutureInTime = Instant.now().plusSeconds(30);
    var result = new PartnerUpdateAt(slightFutureInTime);
    assertThat(result).isNotNull().isInstanceOf(PartnerUpdateAt.class);
    assertThat(result.updatedAt()).isEqualTo(slightFutureInTime);
  }

  @Test
  @DisplayName("PartnerUpdateAt created with current createdAt returns a valid object")
  void partnerUpdateAtWithInstantNewMethodReturnsAValidObject() {
    var now = Instant.now();
    var result = new PartnerUpdateAt(now);
    assertThat(result).isNotNull().isInstanceOf(PartnerUpdateAt.class);
    assertThat(result.updatedAt()).isEqualTo(now);
    assertThat(result.updatedAt()).isBeforeOrEqualTo(now.plusSeconds(5));
  }

  @Test
  @DisplayName("PartnerUpdateAt createNow returns a valid object")
  void partnerUpdateAtCreateNowReturnAValidObject() {
    var now = Instant.now();
    var result = PartnerUpdateAt.createNow();
    assertThat(result).isNotNull().isInstanceOf(PartnerUpdateAt.class);
    assertThat(result.updatedAt()).isAfterOrEqualTo(now);
    assertThat(result.updatedAt()).isBeforeOrEqualTo(now.plusSeconds(5));
  }

  @Test
  @DisplayName("PartnerUpdateAt objects created from createNow are unique")
  void partnerUpdateAtObjectCreatedFromCreateNowAreUnique() {
    var time1 = PartnerUpdateAt.createNow();
    var time2 = PartnerUpdateAt.createNow();
    assertThat(time1).isNotSameAs(time2);
    assertThat(time1.updatedAt()).isBeforeOrEqualTo(time2.updatedAt());
  }
}
