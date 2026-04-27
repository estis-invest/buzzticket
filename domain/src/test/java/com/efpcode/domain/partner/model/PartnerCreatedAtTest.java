package com.efpcode.domain.partner.model;

import static org.assertj.core.api.Assertions.*;

import com.efpcode.domain.partner.exceptions.InvalidPartnerCreatedAtException;
import com.efpcode.domain.partner.exceptions.PartnerCreatedAtDateException;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PartnerCreatedAtTest {

  @Test
  @DisplayName("PartnerCreatedAt cannot be null")
  void partnerCreatedAtCannotBeNull() {
    assertThatThrownBy(() -> new PartnerCreatedAt(null))
        .isInstanceOf(InvalidPartnerCreatedAtException.class)
        .hasMessageContaining("Time is required");
  }

  @Test
  @DisplayName("PartnerCreatedAt cannot pass zero createdAt as argument")
  void partnerCreatedAtCannotPassZeroTimeAsArgument() {
    var timeZero = Instant.ofEpochMilli(0);
    assertThatThrownBy(() -> new PartnerCreatedAt(timeZero))
        .isInstanceOf(InvalidPartnerCreatedAtException.class)
        .hasMessageContaining("Time is required");
  }

  @Test
  @DisplayName(
      "PartnerCreatedAt cannot create Partner in the future if createdAt exceeds margin of error throws error")
  void partnerCreatedAtCannotCreatePartnerInTheFutureIfCreatedAtExceedsMarginOfErrorThrowsError() {
    var futureTime = Instant.now().plusSeconds(90);
    assertThatThrownBy(() -> new PartnerCreatedAt(futureTime))
        .isInstanceOf(PartnerCreatedAtDateException.class)
        .hasMessageContaining("Partner cannot be created in the future");
  }

  @Test
  @DisplayName("PartnerCreatedAt within grace period returns a valid object")
  void partnerCreatedAtWithinGracePeriodReturnsAValidObject() {
    var slightFutureInTime = Instant.now().plusSeconds(30);
    var result = new PartnerCreatedAt(slightFutureInTime);
    assertThat(result).isNotNull().isInstanceOf(PartnerCreatedAt.class);
    assertThat(result.createdAt()).isEqualTo(slightFutureInTime);
  }

  @Test
  @DisplayName("PartnerCreatedAt with instant new method returns a valid object")
  void partnerCreatedAtWithInstantNewMethodReturnsAValidObject() {

    var now = Instant.now();
    var result = new PartnerCreatedAt(now);
    assertThat(result).isNotNull().isInstanceOf(PartnerCreatedAt.class);
    assertThat(result.createdAt()).isEqualTo(now);
    assertThat(result.createdAt()).isBeforeOrEqualTo(now.plusSeconds(5));
  }

  @Test
  @DisplayName("PartnerCreatedAt createNow return a valid object")
  void partnerCreatedAtCreateNowReturnAValidObject() {
    var now = Instant.now();
    var result = PartnerCreatedAt.createNow();
    assertThat(result).isNotNull().isInstanceOf(PartnerCreatedAt.class);
    assertThat(result.createdAt()).isAfterOrEqualTo(now);
    assertThat(result.createdAt()).isBeforeOrEqualTo(now.plusSeconds(5));
  }

  @Test
  @DisplayName("PartnerCreatedAt object created from createNow are unique")
  void partnerCreatedAtObjectCreatedFromCreateNowAreUnique() {
    var time1 = PartnerCreatedAt.createNow();
    var time2 = PartnerCreatedAt.createNow();
    assertThat(time1).isNotSameAs(time2);
    assertThat(time1.createdAt()).isBeforeOrEqualTo(time2.createdAt());
  }
}
