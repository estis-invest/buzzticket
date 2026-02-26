package com.efpcode.domain.model;

import static org.assertj.core.api.Assertions.*;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class TicketDomainTests {

  @Nested
  class TicketTest {

    private final TicketId anyId = new TicketId(UUID.randomUUID());
    private final TicketSlug anySlug = new TicketSlug("BZT-00001");
    private final TicketTitle anyTitle = new TicketTitle("Fix broken build");
    private final TicketDescription anyDescription = new TicketDescription("This ticket is broken");
    private final TicketCreatAt anyTime = new TicketCreatAt(Instant.now());
    private final TicketAssignees anyWorker =
        new TicketAssignees(Set.of(new UserId(UUID.randomUUID(), "Random", "test@test.com")));
    private final UserId anyCustomer = new UserId(UUID.randomUUID(), "Random2", "test2@test2.com");

    @Test
    @DisplayName("Opening a ticket that is not PENDING return the same instance")
    void openingATicketThatIsNotPendingReturnTheSameInstance() {

      var closedTicket =
          new Ticket(
              anyId,
              anySlug,
              anyTitle,
              anyDescription,
              TicketStatus.CLOSED,
              anyTime,
              anyWorker,
              anyCustomer);
      var result = closedTicket.open();

      assertThat(closedTicket).isSameAs(result);
      assertThat(closedTicket.status()).isEqualTo(result.status());
    }

    @Test
    @DisplayName("Opening a ticket in pending returns new status open")
    void openingATicketInPendingReturnsNewStatusOpen() {

      var pendingTicket =
          new Ticket(
              anyId,
              anySlug,
              anyTitle,
              anyDescription,
              TicketStatus.PENDING,
              anyTime,
              anyWorker,
              anyCustomer);
      var result = pendingTicket.open();

      assertThat(pendingTicket).isNotEqualTo(result);
      assertThat(pendingTicket.status()).isNotEqualTo(result.status());
      assertThat(result.status()).isEqualTo(TicketStatus.OPEN);

      assertThat(result.id()).isEqualTo(pendingTicket.id());
      assertThat(result.title()).isEqualTo(pendingTicket.title());
    }

    @Test
    @DisplayName("Ticket with open status can change to closed status")
    void ticketWithOpenStatusCanChangeToClosedStatus() {

      var openTicket =
          new Ticket(
              anyId,
              anySlug,
              anyTitle,
              anyDescription,
              TicketStatus.OPEN,
              anyTime,
              anyWorker,
              anyCustomer);

      var result = openTicket.close();

      assertThat(openTicket).isNotEqualTo(result);
      assertThat(openTicket.status()).isNotEqualTo(result.status());

      assertThat(openTicket.id()).isEqualTo(result.id());
      assertThat(result.status()).isEqualTo(TicketStatus.CLOSED);
    }

    @Test
    @DisplayName("Ticket with closed status can change to archive status")
    void ticketWithClosedStatusCanChangeToArchiveStatus() {

      var closedTicket =
          new Ticket(
              anyId,
              anySlug,
              anyTitle,
              anyDescription,
              TicketStatus.CLOSED,
              anyTime,
              anyWorker,
              anyCustomer);
      var result = closedTicket.archive();

      assertThat(closedTicket).isNotEqualTo(result);
      assertThat(closedTicket.status()).isNotEqualTo(result.status());

      assertThat(closedTicket.id()).isEqualTo(result.id());
      assertThat(result.status()).isEqualTo(TicketStatus.ARCHIVED);
    }

    @Test
    @DisplayName("New ticket has status Pending and no workers")
    void newTicketHasStatusPendingAndNoWorkers() {

      var ticket =
          Ticket.createPending(anyId, anySlug, anyTitle, anyDescription, anyTime, anyCustomer);
      assertThat(ticket.status()).isEqualTo(TicketStatus.PENDING);
      assertThat(ticket.workers().workers()).isEmpty();
    }

    @Test
    @DisplayName("Ticket that have not status OPEN cannot be closed")
    void ticketThatHaveNotStatusOpenCannotBeClosed() {
      var pendingTicket =
          new Ticket(
              anyId,
              anySlug,
              anyTitle,
              anyDescription,
              TicketStatus.PENDING,
              anyTime,
              anyWorker,
              anyCustomer);

      var result = pendingTicket.close();

      assertThat(pendingTicket).isSameAs(result);
      assertThat(result.status()).isEqualTo(pendingTicket.status());
      assertThat(result.id()).isEqualTo(pendingTicket.id());
      assertThat(result.status()).isEqualTo(TicketStatus.PENDING);
    }
  }

  @Nested
  class TicketRecordTests {

    @Test
    @DisplayName("TicketAssignees throws error if more size is greather than three")
    void ticketAssigneesThrowsErrorIfMoreSizeIsGreatherThanThree() {
      var worker1 = new UserId(UUID.randomUUID(), "W1", "w1@test.com");
      var worker2 = new UserId(UUID.randomUUID(), "W2", "w2@test.com");
      var worker3 = new UserId(UUID.randomUUID(), "W3", "w3@test.com");
      var worker4 = new UserId(UUID.randomUUID(), "W4", "w4@test.com");

      Set<UserId> overLimit = Set.of(worker1, worker2, worker3, worker4);

      assertThatThrownBy(() -> new TicketAssignees(overLimit))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("3 workers");
    }
  }
}
