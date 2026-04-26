package com.efpcode.domain.ticket.model;

import static org.assertj.core.api.Assertions.*;

import com.efpcode.domain.partner.model.PartnerId;
import com.efpcode.domain.testsupport.TestUUIDIds;
import com.efpcode.domain.ticket.exceptions.*;
import com.efpcode.domain.user.exceptions.IllegalUserRolePrivilegeException;
import com.efpcode.domain.user.model.UserId;
import com.efpcode.domain.user.model.UserRole;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

class TicketTest {

  private static final TicketId anyId = TestUUIDIds.ticketId();
  private static final TicketSlug anySlug = new TicketSlug("BZT-00001");
  private static final TicketTitle anyTitle = new TicketTitle("Fix broken build");
  private static final TicketDescription anyDescription =
      new TicketDescription("This ticket is broken");
  private static final TicketCreatedAt anyTime = TicketCreatedAt.createNow();
  private static final TicketUpdateAt anyUpdateTime = TicketUpdateAt.createNow();
  private static final TicketAssignees anyWorker =
      new TicketAssignees(Set.of(TestUUIDIds.userId()));
  private static final UserId anyCustomer = TestUUIDIds.userId();
  private static final Optional<PartnerId> anyPartnerId = Optional.of(TestUUIDIds.partnerId());

  private static Stream<Arguments> providesInvalidConstructorArgs() {

    var id = anyId;
    var slug = anySlug;
    var title = anyTitle;
    var description = anyDescription;
    var status = TicketStatus.PENDING;
    var prio = TicketPriority.LOW;
    var createdAt = anyTime;
    var updatedAt = anyUpdateTime;
    var worker = anyWorker;
    var customer = anyCustomer;
    var owner = anyPartnerId;

    return Stream.of(
        Arguments.of(
            null,
            slug,
            title,
            description,
            status,
            prio,
            createdAt,
            updatedAt,
            worker,
            customer,
            owner,
            "TicketId cannot be null"),
        Arguments.of(
            id,
            null,
            title,
            description,
            status,
            prio,
            createdAt,
            updatedAt,
            worker,
            customer,
            owner,
            "TicketSlug cannot be null"),
        Arguments.of(
            id,
            slug,
            null,
            description,
            status,
            prio,
            createdAt,
            updatedAt,
            worker,
            customer,
            owner,
            "TicketTitle cannot be null"),
        Arguments.of(
            id,
            slug,
            title,
            null,
            status,
            prio,
            createdAt,
            updatedAt,
            worker,
            customer,
            owner,
            "TicketDescription cannot be null"),
        Arguments.of(
            id,
            slug,
            title,
            description,
            null,
            prio,
            createdAt,
            updatedAt,
            worker,
            customer,
            owner,
            "TicketStatus cannot be null"),
        Arguments.of(
            id,
            slug,
            title,
            description,
            status,
            null,
            createdAt,
            updatedAt,
            worker,
            customer,
            owner,
            "TicketPriority cannot be null"),
        Arguments.of(
            id,
            slug,
            title,
            description,
            status,
            prio,
            null,
            updatedAt,
            worker,
            customer,
            owner,
            "TicketCreatedAt cannot be null"),
        Arguments.of(
            id,
            slug,
            title,
            description,
            status,
            prio,
            createdAt,
            null,
            worker,
            customer,
            owner,
            "TicketUpdateAt cannot be null"),
        Arguments.of(
            id,
            slug,
            title,
            description,
            status,
            prio,
            createdAt,
            updatedAt,
            null,
            customer,
            owner,
            "TicketAssignees cannot be null"),
        Arguments.of(
            id,
            slug,
            title,
            description,
            status,
            prio,
            createdAt,
            updatedAt,
            worker,
            null,
            owner,
            "UserId cannot be null"),
        Arguments.of(
            id,
            slug,
            title,
            description,
            status,
            prio,
            createdAt,
            updatedAt,
            worker,
            customer,
            null,
            "PartnerId cannot be null"));
  }

  @ParameterizedTest
  @MethodSource("providesInvalidConstructorArgs")
  @DisplayName("TicketConstructor throws NullPointException if any field is null")
  void ticketConstructorThrowsNullPointExceptionIfAnyFieldIsNull(
      TicketId id,
      TicketSlug slug,
      TicketTitle title,
      TicketDescription description,
      TicketStatus status,
      TicketPriority priority,
      TicketCreatedAt createdAt,
      TicketUpdateAt updatedAt,
      TicketAssignees worker,
      UserId customer,
      Optional<PartnerId> owner,
      String expectedMessage) {

    assertThatThrownBy(
            () ->
                new Ticket(
                    id,
                    slug,
                    title,
                    description,
                    status,
                    priority,
                    createdAt,
                    updatedAt,
                    worker,
                    customer,
                    owner))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining(expectedMessage);
  }

  @Test
  @DisplayName("Ticket throws error if PartnerId is empty")
  void ticketThrowsErrorIfPartnerIdIsEmpty() {

    Optional<PartnerId> partnerId = Optional.empty();

    assertThatThrownBy(
            () ->
                new Ticket(
                    anyId,
                    anySlug,
                    anyTitle,
                    anyDescription,
                    TicketStatus.PENDING,
                    TicketPriority.LOW,
                    anyTime,
                    anyUpdateTime,
                    anyWorker,
                    anyCustomer,
                    partnerId))
        .isInstanceOf(InvalidTicketException.class)
        .hasMessageContaining("Ticket must have an owner partner");
  }

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
            TicketUpdateAt.createNow(),
            anyWorker,
            anyCustomer,
            anyPartnerId);

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
            anyUpdateTime,
            anyWorker,
            anyCustomer,
            anyPartnerId);
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
            anyUpdateTime,
            anyWorker,
            anyCustomer,
            anyPartnerId);

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
            anyUpdateTime,
            anyWorker,
            anyCustomer,
            anyPartnerId);
    var result = closedTicket.archive();

    assertThat(closedTicket).isNotEqualTo(result);
    assertThat(closedTicket.status()).isNotEqualTo(result.status());

    assertThat(closedTicket.id()).isEqualTo(result.id());
    assertThat(result.status()).isEqualTo(TicketStatus.ARCHIVED);
  }

  @Test
  @DisplayName("New ticket has status Pending and no workers")
  void newTicketHasStatusPendingAndNoWorkers() {
    PartnerId partnerId = TestUUIDIds.partnerId();

    var ticket =
        Ticket.createPending(
            anyId,
            anySlug,
            anyTitle,
            anyDescription,
            TicketPriority.LOW,
            anyTime,
            anyCustomer,
            partnerId);
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
            anyUpdateTime,
            anyWorker,
            anyCustomer,
            anyPartnerId);

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
            anyUpdateTime,
            anyWorker,
            anyCustomer,
            anyPartnerId);

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
            anyUpdateTime,
            anyWorker,
            anyCustomer,
            anyPartnerId);

    var result = pendingTicket.withPriority(TicketPriority.HIGH);
    assertThat(pendingTicket).isNotEqualTo(result);
    assertThat(result.priority()).isNotEqualTo(pendingTicket.priority());
    assertThat(result.id()).isEqualTo(pendingTicket.id());
    assertThat(result.priority()).isEqualTo(TicketPriority.HIGH);
  }

  private static Stream<Arguments> provideNullArgumentsToPassInTicketAssignMethod() {

    UserId anyStaffId = TestUUIDIds.userId();

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
            anyUpdateTime,
            TicketAssignees.empty(),
            anyCustomer,
            anyPartnerId);

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
            anyUpdateTime,
            TicketAssignees.empty(),
            anyCustomer,
            anyPartnerId);

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
            anyUpdateTime,
            TicketAssignees.empty(),
            anyCustomer,
            anyPartnerId);
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
            anyUpdateTime,
            TicketAssignees.empty(),
            anyCustomer,
            anyPartnerId);

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
            anyUpdateTime,
            TicketAssignees.empty(),
            anyCustomer,
            anyPartnerId);

    var staffId = TestUUIDIds.userId();
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
            anyUpdateTime,
            TicketAssignees.empty(),
            anyCustomer,
            anyPartnerId);

    var staffId = TestUUIDIds.userId();
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
            anyUpdateTime,
            TicketAssignees.empty(),
            anyCustomer,
            anyPartnerId);

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
            anyUpdateTime,
            TicketAssignees.empty(),
            anyCustomer,
            anyPartnerId);

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
            anyUpdateTime,
            TicketAssignees.empty(),
            anyCustomer,
            anyPartnerId);

    assertThatCode(() -> ticket.withPriority(TicketPriority.HIGH)).doesNotThrowAnyException();
  }
}
