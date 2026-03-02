package com.efpcode.domain.ticket.model;

import static org.assertj.core.api.Assertions.*;

import com.efpcode.domain.ticket.exceptions.*;
import com.efpcode.domain.user.model.UserId;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class TicketTest {

  @Nested
  class TicketTestObject {

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

  @Nested
  class TicketRecordTests {

    @Test
    @DisplayName("TicketAssignees throws error if more size is greater than three")
    void ticketAssigneesThrowsErrorIfMoreSizeIsGreaterThanThree() {
      var worker1 = UserId.generate();
      var worker2 = UserId.generate();
      var worker3 = UserId.generate();
      var worker4 = UserId.generate();

      Set<UserId> overLimit = Set.of(worker1, worker2, worker3, worker4);
      Set<UserId> underLimit = Set.of(worker1, worker2, worker3);

      var ticketWorkers = new TicketAssignees(underLimit);

      assertThatThrownBy(() -> new TicketAssignees(overLimit))
          .isInstanceOf(TicketAssigmentLimitException.class)
          .hasMessageContaining("3 workers");

      assertThatThrownBy(() -> ticketWorkers.add(worker4))
          .isInstanceOf(TicketAssigmentLimitException.class)
          .hasMessageContaining("than 3");
    }

    @Test
    @DisplayName("TicketAssignees return empty set if null is passed")
    void ticketAssigneesReturnEmptySetIfNullIsPassed() {
      var emptySet = new TicketAssignees(null);
      assertThat(emptySet.workers()).isEmpty();
      assertThat(emptySet).isInstanceOf(TicketAssignees.class).isNotNull();
    }

    @Test
    @DisplayName("TicketAssignees has add method that returns a new object")
    void ticketAssigneesHasAddMethodThatReturnsANewObject() {
      var ticketWorkers = new TicketAssignees(Set.of(UserId.generate()));
      var newWorker = UserId.generate();

      var result = ticketWorkers.add(newWorker);
      assertThat(result).isNotNull();
      assertThat(result.workers()).hasSize(2);
      assertThat(result.workers()).contains(newWorker);
      assertThat(result).isNotEqualTo(ticketWorkers);
    }

    @Test
    @DisplayName("TicketAssignees cannot assignee the same user with add")
    void ticketAssigneesCannotAssigneeTheSameUserWithAdd() {

      var newWorker = UserId.generate();
      var ticketWorker = new TicketAssignees(Set.of(newWorker));

      var tempWorker = ticketWorker.add(newWorker);
      var result = tempWorker.add(newWorker);

      assertThat(result.workers()).hasSize(1);
      assertThat(result.workers()).isEqualTo(ticketWorker.workers());
      assertThat(result).isSameAs(ticketWorker);
    }

    @Test
    @DisplayName("TicketAssignees has remove method that returns new object")
    void ticketAssigneesHasRemoveMethodThatReturnsNewObject() {
      var worker1 = UserId.generate();
      var worker2 = UserId.generate();

      var ticketWorkers = new TicketAssignees(Set.of(worker1, worker2));

      var result = ticketWorkers.remove(worker2);

      assertThat(result.workers()).isNotEmpty().doesNotContain(worker2);
      assertThat(result.workers()).hasSize(1);
      assertThat(result.workers()).isNotEmpty().contains(worker1);
    }

    @Test
    @DisplayName("TicketAssignees cannot remove user that is not present returns valid object")
    void ticketAssigneesCannotRemoveUserThatIsNotPresentReturnsValidObject() {

      var worker1 = UserId.generate();
      var worker2 = UserId.generate();

      var ticketWorkers = new TicketAssignees(Set.of(worker1));
      var result = ticketWorkers.remove(worker2);

      assertThat(result.workers()).hasSize(1);
      assertThat(result.workers()).isEqualTo(ticketWorkers.workers());
      assertThat(result).isSameAs(ticketWorkers);
    }

    @Test
    @DisplayName("TicketAssignees add method cannot pass null throws error")
    void ticketAssigneesAddMethodCannotPassNullThrowsError() {

      var worker1 = UserId.generate();

      var ticketWorkers = new TicketAssignees(Set.of(worker1));
      assertThatThrownBy(() -> ticketWorkers.add(null))
          .isInstanceOf(MissingUserAssignmentException.class)
          .hasMessageContaining("be null");
    }

    @Test
    @DisplayName("TicketAssignees remove method cannot pass null throws error")
    void ticketAssigneesRemoveMethodCannotPassNullThrowsError() {

      var worker1 = UserId.generate();

      var ticketWorkers = new TicketAssignees(Set.of(worker1));
      assertThatThrownBy(() -> ticketWorkers.remove(null))
          .isInstanceOf(MissingUserAssignmentException.class)
          .hasMessageContaining("be null");
    }

    @Test
    @DisplayName("TicketCreateAt cannot pass null value or zero time")
    void ticketCreateAtCannotPassNullValueOrZeroTime() {

      assertThatThrownBy(() -> new TicketCreatedAt(null))
          .isInstanceOf(InvalidCreatedAtException.class)
          .hasMessageContaining("Time is required");

      var emptyTime = Instant.ofEpochMilli(0);

      assertThatThrownBy(() -> new TicketCreatedAt(emptyTime))
          .isInstanceOf(InvalidCreatedAtException.class)
          .hasMessageContaining("Time is required");
    }

    @Test
    @DisplayName("Time cannot be created in the future")
    void timeCannotBeCreatedInTheFuture() {

      var futureTime = Instant.now().plusSeconds(90);

      assertThatThrownBy(() -> new TicketCreatedAt(futureTime))
          .isInstanceOf(InvalidTicketDateException.class)
          .hasMessageContaining("the future");
    }

    @Test
    @DisplayName("Time has createNow method that returns new objects")
    void timeHasCreateNowMethodThatReturnsNewObjects() {

      var pastTime = TicketCreatedAt.createNow();
      var currentTime = TicketCreatedAt.createNow();

      assertThat(pastTime).isNotSameAs(currentTime);
    }

    @Test
    @DisplayName("Time has createNow method and returns valid objects")
    void timeHasCreateNowMethodAndReturnsValidObjects() {

      var now = TicketCreatedAt.createNow();
      assertThat(now.time()).isNotNull();
      assertThat(now.time()).isBeforeOrEqualTo(Instant.now());
    }

    @Test
    @DisplayName("TicketId cannot pass null value")
    void ticketIdCannotPassNullValue() {

      assertThatThrownBy(() -> new TicketId(null))
          .isInstanceOf(InvalidTicketIdException.class)
          .hasMessageContaining("UUID is required");
    }

    @Test
    @DisplayName("TicketId generate method returns valid UUID object")
    void ticketIdGenerateMethodReturnsValidUuidObject() {
      var result = TicketId.generate();
      assertThat(result).isNotNull().isInstanceOf(TicketId.class);
    }

    @Test
    @DisplayName("TicketId fromString method returns a valid TicketID object")
    void ticketIdFromStringMethodReturnsAValidTicketIdObject() {

      var stringUUID = UUID.randomUUID().toString();

      TicketId result = TicketId.fromString(stringUUID);

      assertThat(result).isNotNull().isInstanceOf(TicketId.class);
      assertThat(result.value().toString()).hasToString(stringUUID);
    }

    @Test
    @DisplayName("TicketDescription cannot pass null or blank")
    void ticketDescriptionCannotPassNullOrBlank() {
      assertThatThrownBy(() -> new TicketDescription("    "))
          .isInstanceOf(InvalidTicketDescriptionException.class)
          .hasMessageContaining("be empty");

      assertThatThrownBy(() -> new TicketDescription(null))
          .isInstanceOf(InvalidTicketDescriptionException.class)
          .hasMessageContaining("be empty");
    }

    @Test
    @DisplayName("TicketDescription has max length and surpassed throws error")
    void ticketDescriptionHasMaxLengthAndSurpassedThrowsError() {

      var longText = "a".repeat(1801);

      assertThatThrownBy(() -> new TicketDescription(longText))
          .isInstanceOf(TicketDescriptionLengthException.class)
          .hasMessageContaining("exceeds max length");
    }

    @Test
    @DisplayName("TicketDescription can be created if length is under max length")
    void ticketDescriptionCanBeCreatedIfLengthIsUnderMaxLength() {
      var lengthOfText = 1800;
      var longText = "a".repeat(lengthOfText);
      var ticketDescription = new TicketDescription(longText);

      assertThat(ticketDescription.description()).isEqualTo(longText);
      assertThat(ticketDescription.description()).isNotBlank().isNotNull();
      assertThat(ticketDescription.description()).hasSameSizeAs(longText);
    }

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

    @Test
    @DisplayName("TicketTitle cannot pass null or blank")
    void ticketTitleCannotPassNullOrBlank() {
      assertThatThrownBy(() -> new TicketTitle("  "))
          .isInstanceOf(InvalidTicketTitleException.class)
          .hasMessageContaining("be blank");

      assertThatThrownBy(() -> new TicketTitle(null))
          .isInstanceOf(InvalidTicketTitleException.class)
          .hasMessageContaining("or null");
    }

    @Test
    @DisplayName("TicketTitle has max length if surpass throws error")
    void ticketTitleHasMaxLengthIfSurpassThrowsError() {

      var longTitle = "a".repeat(51);

      assertThatThrownBy(() -> new TicketTitle(longTitle))
          .isInstanceOf(TicketTitleLengthException.class)
          .hasMessageContaining("Max length");
    }
  }
}
