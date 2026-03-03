package com.efpcode.domain.ticket.model;

import static org.assertj.core.api.Assertions.assertThat;

import com.efpcode.domain.user.model.UserId;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TicketTest {

  private final TicketId anyId = TicketId.generate();
  private final TicketSlug anySlug = new TicketSlug("BZT-00001");
  private final TicketTitle anyTitle = new TicketTitle("Fix broken build");
  private final TicketDescription anyDescription = new TicketDescription("This ticket is broken");
  private final TicketCreatedAt anyTime = TicketCreatedAt.createNow();
  private final TicketAssignees anyWorker =
      new TicketAssignees(Set.of(new UserId(UUID.randomUUID())));
  private final UserId anyCustomer = UserId.generate();

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
            TicketPriority.LOW,
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
            TicketPriority.LOW,
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
            TicketPriority.LOW,
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
            TicketPriority.LOW,
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
        Ticket.createPending(
            anyId, anySlug, anyTitle, anyDescription, TicketPriority.LOW, anyTime, anyCustomer);
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
            TicketPriority.LOW,
            anyTime,
            anyWorker,
            anyCustomer);

    var result = pendingTicket.close();

    assertThat(pendingTicket).isSameAs(result);
    assertThat(result.status()).isEqualTo(pendingTicket.status());
    assertThat(result.id()).isEqualTo(pendingTicket.id());
    assertThat(result.status()).isEqualTo(TicketStatus.PENDING);
  }

  @Test
  @DisplayName("Only ticket with status closed can change to status archive")
  void onlyTicketWithStatusClosedCanChangeToStatusArchive() {

    var pendingTicket =
        new Ticket(
            anyId,
            anySlug,
            anyTitle,
            anyDescription,
            TicketStatus.PENDING,
            TicketPriority.LOW,
            anyTime,
            anyWorker,
            anyCustomer);

    var result = pendingTicket.archive();

    assertThat(pendingTicket).isSameAs(result);
    assertThat(result.status()).isEqualTo(pendingTicket.status());
    assertThat(result.id()).isEqualTo(pendingTicket.id());
    assertThat(result.status()).isEqualTo(TicketStatus.PENDING);
  }

  @Test
  @DisplayName("Tickets can change priority")
  void ticketsCanChangePriority() {
    var pendingTicket =
        new Ticket(
            anyId,
            anySlug,
            anyTitle,
            anyDescription,
            TicketStatus.PENDING,
            TicketPriority.LOW,
            anyTime,
            anyWorker,
            anyCustomer);

    var result = pendingTicket.withPriority(TicketPriority.HIGH);
    assertThat(pendingTicket).isNotEqualTo(result);
    assertThat(result.priority()).isNotEqualTo(pendingTicket.priority());
    assertThat(result.id()).isEqualTo(pendingTicket.id());
    assertThat(result.priority()).isEqualTo(TicketPriority.HIGH);
  }
}
