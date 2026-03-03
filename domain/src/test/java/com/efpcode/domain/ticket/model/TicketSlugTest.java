package com.efpcode.domain.ticket.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.efpcode.domain.ticket.exceptions.InvalidTicketSlugException;
import com.efpcode.domain.ticket.exceptions.TicketSlugFormatException;
import com.efpcode.domain.ticket.exceptions.TicketSlugLengthException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TicketSlugTest {
  @Test
  @DisplayName("TicketSlug cannot pass null or blank throws error")
  void ticketSlugCannotPassNullOrBlankThrowsError() {

    assertThatThrownBy(() -> new TicketSlug(null))
        .isInstanceOf(InvalidTicketSlugException.class)
        .hasMessageContaining("be null");

    assertThatThrownBy(() -> new TicketSlug("   "))
        .isInstanceOf(InvalidTicketSlugException.class)
        .hasMessageContaining("or blank");
  }

  @Test
  @DisplayName("Ticket Slug must follow format else throws error")
  void ticketSlugMustFollowFormatElseThrowsError() {
    var invalidFormatSlug = "AAAA-0000";

    assertThatThrownBy(() -> new TicketSlug(invalidFormatSlug))
        .isInstanceOf(TicketSlugFormatException.class)
        .hasMessageContaining("AAA-0000...000");
  }

  @Test
  @DisplayName("TicketSlug has max length surpass throws error")
  void ticketSlugHasMaxLengthSurpassThrowsError() {
    var prefix = "AAA";
    var longSlug = "1".repeat(64);

    assertThatThrownBy(() -> new TicketSlug(prefix + "-" + longSlug))
        .isInstanceOf(TicketSlugLengthException.class)
        .hasMessageContaining("max range");
  }

  @Test
  @DisplayName("TicketSlug returns valid object if constraints are passed")
  void ticketSlugReturnsValidObjectIfConstraintsArePassed() {

    var prefix = "AAA-";
    var longText = "1".repeat(60);
    var longSlug = prefix + longText;

    var newSlug = new TicketSlug(longSlug);

    assertThat(newSlug.slug()).isEqualTo(longSlug);
    assertThat(newSlug.slug()).hasSameSizeAs(longSlug);
  }
}
