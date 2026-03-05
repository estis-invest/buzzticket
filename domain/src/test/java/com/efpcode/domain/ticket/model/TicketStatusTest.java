package com.efpcode.domain.ticket.model;

import static org.assertj.core.api.Assertions.*;

import com.efpcode.domain.ticket.exceptions.IllegalTicketStatusAssignmentException;
import com.efpcode.domain.ticket.exceptions.IllegalTicketStatusTransitionException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class TicketStatusTest {

  @ParameterizedTest
  @EnumSource(
      value = TicketStatus.class,
      names = {"OPEN", "CLOSED", "ARCHIVED"})
  @DisplayName("TicketStatus method open throws error if status is not PENDING")
  void ticketStatusMethodOpenThrowsErrorIfStatusIsNotPending(TicketStatus status) {

    assertThatThrownBy(status::open)
        .isInstanceOf(IllegalTicketStatusTransitionException.class)
        .hasMessageContaining(
            String.format(
                "TicketStatus cannot be transferred from %s to %s", status, TicketStatus.OPEN));
  }

  @ParameterizedTest
  @EnumSource(
      value = TicketStatus.class,
      names = {"PENDING", "CLOSED", "ARCHIVED"})
  @DisplayName("TicketStatus method close throws error if status is not OPEN")
  void ticketStatusMethodCloseThrowsErrorIfStatusIsNotOpen(TicketStatus status) {
    assertThatThrownBy(status::close)
        .isInstanceOf(IllegalTicketStatusTransitionException.class)
        .hasMessageContaining(
            String.format(
                "TicketStatus cannot be transferred from %s to %s", status, TicketStatus.CLOSED));
  }

  @ParameterizedTest
  @EnumSource(
      value = TicketStatus.class,
      names = {"PENDING", "OPEN", "ARCHIVED"})
  @DisplayName("TicketStatus method archive throws error if status is not Closed")
  void ticketStatusMethodArchiveThrowsErrorIfStatusIsNotClosed(TicketStatus status) {
    assertThatThrownBy(status::archive)
        .isInstanceOf(IllegalTicketStatusTransitionException.class)
        .hasMessageContaining(
            String.format(
                "TicketStatus cannot be transferred from %s to %s", status, TicketStatus.ARCHIVED));
  }

  @Test
  @DisplayName("TicketStatus method open transition from PENDING to OPEN")
  void ticketStatusMethodOpenTransitionFromPendingToOpen() {

    var ticketPending = TicketStatus.PENDING;
    var ticket = ticketPending.open();

    assertThat(ticket)
        .isNotSameAs(ticketPending)
        .isNotEqualTo(ticketPending)
        .isEqualTo(TicketStatus.OPEN);
  }

  @Test
  @DisplayName("TicketStatus method close transition from OPEN to CLOSED")
  void ticketStatusMethodCloseTransitionFromOpenToClosed() {

    var ticketOpen = TicketStatus.OPEN;
    var ticket = ticketOpen.close();

    assertThat(ticket)
        .isNotSameAs(ticketOpen)
        .isNotEqualTo(ticketOpen)
        .isEqualTo(TicketStatus.CLOSED);
  }

  @Test
  @DisplayName("TicketStatus method archive transition from CLOSED to ARCHIVED")
  void ticketStatusMethodArchiveTransitionFromClosedToArchived() {

    var ticketClosed = TicketStatus.CLOSED;
    var ticket = ticketClosed.archive();

    assertThat(ticket)
        .isNotSameAs(ticketClosed)
        .isNotEqualTo(ticketClosed)
        .isEqualTo(TicketStatus.ARCHIVED);
  }

  @Test
  @DisplayName("TicketStatus method isTransitionAllowed returns false if null is passed")
  void ticketStatusMethodIsTransitionAllowedReturnsFalseIfNullIsPassed() {

    assertThat(TicketStatus.ARCHIVED.isTransitionAllowed(null)).isFalse();
  }

  @Test
  @DisplayName("TicketStatus method isTransitionAllowed returns false if PENDING is passed")
  void ticketStatusMethodIsTransitionAllowedReturnsFalseIfPendingIsPassed() {

    assertThat(TicketStatus.OPEN.isTransitionAllowed(TicketStatus.PENDING)).isFalse();
  }

  @ParameterizedTest
  @EnumSource(
      value = TicketStatus.class,
      names = {"CLOSED", "ARCHIVED"})
  @DisplayName("TicketStatus method isTicketAssignable return false for status CLOSED and ARCHIVED")
  void ticketStatusMethodIsTicketAssignableReturnFalseForStatusClosedAndArchived(
      TicketStatus status) {
    assertThat(status.isTicketAssignable()).isFalse();
  }

  @ParameterizedTest
  @EnumSource(
      value = TicketStatus.class,
      names = {"CLOSED", "ARCHIVED"})
  @DisplayName(
      "TicketStatus method ticketStatusAssignGuard throws error for status CLOSED and ARCHIVED")
  void ticketStatusMethodTicketStatusAssignGuardThrowsErrorForStatusClosedAndArchived(
      TicketStatus status) {
    assertThatThrownBy(status::ticketStatusAssignGuard)
        .isInstanceOf(IllegalTicketStatusAssignmentException.class)
        .hasMessageContaining(String.format("TicketStatus: %s cannot assign users", status));
  }

  @ParameterizedTest
  @EnumSource(
      value = TicketStatus.class,
      names = {"OPEN", "PENDING"})
  @DisplayName("TicketStatus method isTicketAssignable return true for status PENDING and OPEN")
  void ticketStatusMethodIsTicketAssignableReturnTrueForStatusPendingAndOpen(TicketStatus status) {
    assertThat(status.isTicketAssignable()).isTrue();
  }

  @ParameterizedTest
  @EnumSource(
      value = TicketStatus.class,
      names = {"PENDING", "OPEN"})
  @DisplayName(
      "TicketStatus method ticketStatusAssignGuard throws error for status PENDING and OPEN")
  void ticketStatusMethodTicketStatusAssignGuardThrowsErrorForStatusPendingAndOPEN(
      TicketStatus status) {
    assertThatCode(status::ticketStatusAssignGuard).doesNotThrowAnyException();
  }
}
