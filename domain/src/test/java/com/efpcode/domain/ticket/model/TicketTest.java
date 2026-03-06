package com.efpcode.domain.ticket.model;

import static org.assertj.core.api.Assertions.*;

import com.efpcode.domain.ticket.exceptions.IllegalTicketAssignmentException;
import com.efpcode.domain.ticket.exceptions.IllegalTicketPriorityException;
import com.efpcode.domain.ticket.exceptions.IllegalTicketStatusAssignmentException;
import com.efpcode.domain.ticket.exceptions.IllegalTicketStatusTransitionException;
import com.efpcode.domain.user.exceptions.IllegalUserRolePrivilegeException;
import com.efpcode.domain.user.model.UserId;
import com.efpcode.domain.user.model.UserRole;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

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
  @DisplayName("Opening a ticket that is not PENDING throws error")
  void openingATicketThatIsNotPendingThrowsError() {

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

    assertThatThrownBy(closedTicket::open)
        .isInstanceOf(IllegalTicketStatusTransitionException.class)
        .hasMessageContaining("TicketStatus cannot be transferred from " + closedTicket.status());
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

    assertThatThrownBy(pendingTicket::close)
        .isInstanceOf(IllegalTicketStatusTransitionException.class)
        .hasMessageContaining("TicketStatus cannot be transferred from " + pendingTicket.status());
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

    assertThatThrownBy(pendingTicket::archive)
        .isInstanceOf(IllegalTicketStatusTransitionException.class)
        .hasMessageContaining("TicketStatus cannot be transferred from " + pendingTicket.status());
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

  private static Stream<Arguments> provideNullArgumentsToPassInTicketAssignMethod() {

    UserId anyStaffId = UserId.generate();

    return Stream.of(
        Arguments.of(null, null),
        Arguments.of(anyStaffId, null),
        Arguments.of(null, UserRole.ADMIN));
  }

  @ParameterizedTest
  @MethodSource("provideNullArgumentsToPassInTicketAssignMethod")
  @DisplayName("Ticket assign methods throws error if staffId or actorRole is null")
  void ticketAssignMethodsThrowsErrorIfStaffIdOrActorRoleIsNull(
      UserId staffId, UserRole actorRole) {
    var ticket =
        new Ticket(
            anyId,
            anySlug,
            anyTitle,
            anyDescription,
            TicketStatus.PENDING,
            TicketPriority.LOW,
            anyTime,
            TicketAssignees.empty(),
            anyCustomer);

    assertThatThrownBy(() -> ticket.assign(staffId, actorRole))
        .isInstanceOf(IllegalTicketAssignmentException.class)
        .hasMessageContaining("TicketAssign method cannot pass null!");
  }

  @Test
  @DisplayName("TicketAssignMethod throws error is User Role is Customer")
  void ticketAssignMethodThrowsErrorIsUserRoleIsCustomer() {
    var ticket =
        new Ticket(
            anyId,
            anySlug,
            anyTitle,
            anyDescription,
            TicketStatus.PENDING,
            TicketPriority.LOW,
            anyTime,
            TicketAssignees.empty(),
            anyCustomer);

    var userRole = UserRole.CUSTOMER;

    assertThatThrownBy(() -> ticket.assign(anyCustomer, userRole))
        .isInstanceOf(IllegalUserRolePrivilegeException.class)
        .hasMessageContaining("User role: " + userRole);
  }

  @Test
  @DisplayName("Ticket assign throws error with status CLOSED")
  void ticketAssignThrowsErrorWithStatusClosed() {
    var ticketClosed =
        new Ticket(
            anyId,
            anySlug,
            anyTitle,
            anyDescription,
            TicketStatus.CLOSED,
            TicketPriority.LOW,
            anyTime,
            TicketAssignees.empty(),
            anyCustomer);
    assertThatThrownBy(() -> ticketClosed.assign(anyCustomer, UserRole.SUPPORT))
        .isInstanceOf(IllegalTicketStatusAssignmentException.class)
        .hasMessageContaining("TicketStatus: " + TicketStatus.CLOSED + " cannot assign users");
  }

  @Test
  @DisplayName("Ticket assign throws error with status ARCHIVED ")
  void ticketAssignThrowsErrorWithStatusArchived() {
    var ticketArchived =
        new Ticket(
            anyId,
            anySlug,
            anyTitle,
            anyDescription,
            TicketStatus.ARCHIVED,
            TicketPriority.LOW,
            anyTime,
            TicketAssignees.empty(),
            anyCustomer);

    assertThatThrownBy(() -> ticketArchived.assign(anyCustomer, UserRole.SUPPORT))
        .isInstanceOf(IllegalTicketStatusAssignmentException.class)
        .hasMessageContaining("TicketStatus: " + TicketStatus.ARCHIVED + " cannot assign users");
  }

  @ParameterizedTest
  @EnumSource(
      value = TicketStatus.class,
      names = {"PENDING", "OPEN"})
  @DisplayName("Ticket assign methods returns a new Ticket object with status PENDING and OPEN")
  void ticketAssignMethodsReturnsANewTicketObjectWithStatusPendingAndOpen(TicketStatus status) {

    var ticket =
        new Ticket(
            anyId,
            anySlug,
            anyTitle,
            anyDescription,
            status,
            TicketPriority.LOW,
            anyTime,
            TicketAssignees.empty(),
            anyCustomer);

    var staffId = UserId.generate();
    var result = ticket.assign(staffId, UserRole.SUPPORT);
    assertThat(result).isNotSameAs(ticket).isInstanceOf(Ticket.class);
    assertThat(result.workers().workers()).hasSize(1);
  }

  private static Stream<Arguments> provideStatusAndRoleCombinations() {
    return Stream.of(
        Arguments.of(TicketStatus.PENDING, UserRole.SUPPORT),
        Arguments.of(TicketStatus.PENDING, UserRole.ADMIN),
        Arguments.of(TicketStatus.OPEN, UserRole.SUPPORT),
        Arguments.of(TicketStatus.OPEN, UserRole.ADMIN));
  }

  @ParameterizedTest
  @MethodSource("provideStatusAndRoleCombinations")
  @DisplayName("Ticket assign works for valid Status and Staff Roles")
  void ticketAssignMethodsReturnsANewTicketObject(TicketStatus status, UserRole role) {
    var ticket =
        new Ticket(
            anyId,
            anySlug,
            anyTitle,
            anyDescription,
            status,
            TicketPriority.LOW,
            anyTime,
            TicketAssignees.empty(),
            anyCustomer);

    var staffId = UserId.generate();
    var result = ticket.assign(staffId, role);

    assertThat(result).isNotSameAs(ticket);
    assertThat(result.workers().workers()).hasSize(1);
  }

  @Test
  @DisplayName("Ticket method withPriority throws error if null is passed")
  void ticketMethodWithPriorityThrowsErrorIfNullIsPassed() {
    var ticket =
        new Ticket(
            anyId,
            anySlug,
            anyTitle,
            anyDescription,
            TicketStatus.PENDING,
            TicketPriority.LOW,
            anyTime,
            TicketAssignees.empty(),
            anyCustomer);

    assertThatThrownBy(() -> ticket.withPriority(null))
        .isInstanceOf(IllegalTicketPriorityException.class)
        .hasMessageContaining("Ticket priority passed cannot be null");
  }

  @ParameterizedTest
  @EnumSource(
      value = TicketStatus.class,
      names = {"ARCHIVED", "CLOSED"})
  @DisplayName("Ticket method withPriority throws error with status ARCHIVED and CLOSED")
  void ticketMethodWithPriorityThrowsErrorWithStatusArchivedAndClosed(TicketStatus status) {

    var ticket =
        new Ticket(
            anyId,
            anySlug,
            anyTitle,
            anyDescription,
            status,
            TicketPriority.LOW,
            anyTime,
            TicketAssignees.empty(),
            anyCustomer);

    assertThatThrownBy(() -> ticket.withPriority(TicketPriority.HIGH))
        .isInstanceOf(IllegalTicketStatusAssignmentException.class)
        .hasMessageContaining("TicketPriority cannot be altered in status: " + status);
  }

  @ParameterizedTest
  @EnumSource(
      value = TicketStatus.class,
      names = {"PENDING", "OPEN"})
  @DisplayName("Ticket method withPriority return valid object with status PENDING and OPEN")
  void ticketMethodWithPriorityReturnValidObjectWithStatusPendingAndOpen(TicketStatus status) {

    var ticket =
        new Ticket(
            anyId,
            anySlug,
            anyTitle,
            anyDescription,
            status,
            TicketPriority.LOW,
            anyTime,
            TicketAssignees.empty(),
            anyCustomer);

    assertThatCode(() -> ticket.withPriority(TicketPriority.HIGH)).doesNotThrowAnyException();
  }
}
