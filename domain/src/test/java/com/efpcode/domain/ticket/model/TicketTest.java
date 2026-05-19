package com.efpcode.domain.ticket.model;

import static org.assertj.core.api.Assertions.*;

import com.efpcode.domain.partner.model.PartnerId;
import com.efpcode.domain.testsupport.TestUUIDIds;
import com.efpcode.domain.ticket.exceptions.*;
import com.efpcode.domain.user.exceptions.IllegalUserRolePrivilegeException;
import com.efpcode.domain.user.model.UserId;
import com.efpcode.domain.user.model.UserRole;
import java.time.Instant;
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
  private static final TicketCreatedAt anyTime = new TicketCreatedAt(Instant.now());
  private static final TicketUpdateAt anyUpdateTime = TicketUpdateAt.of(Instant.now());
  private static final TicketAssignees anyWorker =
      new TicketAssignees(Set.of(TestUUIDIds.userId()));
  private static final UserId anyCustomer = TestUUIDIds.userId();
  private static final PartnerId anyPartnerId = TestUUIDIds.partnerId();
  private static TicketUpdateAt UPDATE_AT = TicketUpdateAt.of(Instant.now());

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
  @DisplayName("TicketConstructor throws NullPointerException if any field is null")
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
      PartnerId owner,
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
  @DisplayName("Opening a ticket that is not PENDING throws error")
  void openingATicketThatIsNotPendingThrowsError() {
    Instant now = Instant.now();

    var closedTicket =
        new Ticket(
            anyId,
            anySlug,
            anyTitle,
            anyDescription,
            TicketStatus.CLOSED,
            TicketPriority.LOW,
            anyTime,
            TicketUpdateAt.of(now),
            anyWorker,
            anyCustomer,
            anyPartnerId);

    assertThatThrownBy(() -> closedTicket.open(UPDATE_AT))
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
    var result = pendingTicket.open(UPDATE_AT);

    assertThat(pendingTicket).isNotEqualTo(result);
    assertThat(pendingTicket.status()).isNotEqualTo(result.status());
    assertThat(result.status()).isEqualTo(TicketStatus.OPEN);
    assertThat(result.updatedAt()).isEqualTo(UPDATE_AT);

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

    var result = openTicket.close(UPDATE_AT);

    assertThat(openTicket).isNotEqualTo(result);
    assertThat(openTicket.status()).isNotEqualTo(result.status());

    assertThat(openTicket.id()).isEqualTo(result.id());
    assertThat(result.status()).isEqualTo(TicketStatus.CLOSED);
    assertThat(result.updatedAt()).isEqualTo(UPDATE_AT);
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
    var result = closedTicket.archive(UPDATE_AT);

    assertThat(closedTicket).isNotEqualTo(result);
    assertThat(closedTicket.status()).isNotEqualTo(result.status());
    assertThat(result.updatedAt()).isEqualTo(UPDATE_AT);

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

    assertThatThrownBy(() -> pendingTicket.close(UPDATE_AT))
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

    assertThatThrownBy(() -> pendingTicket.archive(UPDATE_AT))
        .isInstanceOf(IllegalTicketStatusTransitionException.class)
        .hasMessageContaining("TicketStatus cannot be transferred from " + pendingTicket.status());
  }

  @Test
  @DisplayName("Tickets can change priority")
  void ticketsCanChangePriority() {
    var updatedAt = TicketUpdateAt.of(Instant.now());
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

    var result = pendingTicket.withPriority(TicketPriority.HIGH, updatedAt);
    assertThat(pendingTicket).isNotEqualTo(result);
    assertThat(result.priority()).isNotEqualTo(pendingTicket.priority());
    assertThat(result.id()).isEqualTo(pendingTicket.id());
    assertThat(result.priority()).isEqualTo(TicketPriority.HIGH);
    assertThat(result.updatedAt()).isEqualTo(updatedAt);
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

    assertThatThrownBy(() -> ticket.assign(staffId, actorRole, UPDATE_AT))
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

    assertThatThrownBy(() -> ticket.assign(anyCustomer, userRole, UPDATE_AT))
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
    assertThatThrownBy(() -> ticketClosed.assign(anyCustomer, UserRole.SUPPORT, UPDATE_AT))
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

    assertThatThrownBy(() -> ticketArchived.assign(anyCustomer, UserRole.SUPPORT, UPDATE_AT))
        .isInstanceOf(IllegalTicketStatusAssignmentException.class)
        .hasMessageContaining("TicketStatus: " + TicketStatus.ARCHIVED + " cannot assign users");
  }

  @ParameterizedTest
  @EnumSource(
      value = TicketStatus.class,
      names = {"PENDING", "OPEN"})
  @DisplayName("Ticket assign methods returns a new Ticket object with status PENDING and OPEN")
  void ticketAssignMethodsReturnsANewTicketObjectWithStatusPendingAndOpen(TicketStatus status) {
    var updatedAt = TicketUpdateAt.of(Instant.now());

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
    var result = ticket.assign(staffId, UserRole.SUPPORT, updatedAt);
    assertThat(result).isNotSameAs(ticket).isInstanceOf(Ticket.class);
    assertThat(result.workers().workers()).hasSize(1);
    assertThat(result.updatedAt()).isEqualTo(updatedAt);
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
    var result = ticket.assign(staffId, role, UPDATE_AT);

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

    assertThatThrownBy(() -> ticket.withPriority(null, UPDATE_AT))
        .isInstanceOf(IllegalTicketPriorityException.class)
        .hasMessageContaining("Ticket priority or update time passed cannot be null");
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

    assertThatThrownBy(() -> ticket.withPriority(TicketPriority.HIGH, UPDATE_AT))
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

    var result = ticket.withPriority(TicketPriority.HIGH, UPDATE_AT);

    assertThat(result).isNotSameAs(ticket);
    assertThat(result.priority()).isEqualTo(TicketPriority.HIGH);
    assertThat(result.updatedAt()).isEqualTo(UPDATE_AT);
  }

  @Test
  @DisplayName("Open throws error if updateAt is null")
  void openThrowsErrorIfUpdateAtIsNull() {

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
            anyWorker,
            anyCustomer,
            anyPartnerId);

    assertThatThrownBy(() -> ticket.open(null))
        .isInstanceOf(IllegalTicketStatusTransitionException.class)
        .hasMessageContaining("Update time is required");
  }

  @Test
  @DisplayName("Close throws error if updateAt is null")
  void closeThrowsErrorIfUpdateAtIsNull() {

    var ticket =
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

    assertThatThrownBy(() -> ticket.close(null))
        .isInstanceOf(IllegalTicketStatusTransitionException.class)
        .hasMessageContaining("Update time is required");
  }

  @Test
  @DisplayName("Archive throws error if updateAt is null")
  void archiveThrowsErrorIfUpdateAtIsNull() {

    var ticket =
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

    assertThatThrownBy(() -> ticket.archive(null))
        .isInstanceOf(IllegalTicketStatusTransitionException.class)
        .hasMessageContaining("Update time is required");
  }

  @Test
  @DisplayName("Assign throws error when updateAt is null")
  void assignThrowsErrorWhenUpdateAtIsNull() {

    var ticket =
        new Ticket(
            anyId,
            anySlug,
            anyTitle,
            anyDescription,
            TicketStatus.PENDING, // valid for assignment
            TicketPriority.LOW,
            anyTime,
            anyUpdateTime,
            TicketAssignees.empty(),
            anyCustomer,
            anyPartnerId);

    var staffId = TestUUIDIds.userId();
    var role = UserRole.SUPPORT; // valid role

    assertThatThrownBy(() -> ticket.assign(staffId, role, null))
        .isInstanceOf(IllegalTicketAssignmentException.class)
        .hasMessageContaining("TicketAssign method cannot pass null!");
  }

  @Test
  @DisplayName("withPriority throws error when updateAt is null")
  void withPriorityThrowsErrorWhenUpdateAtIsNull() {

    var ticket =
        new Ticket(
            anyId,
            anySlug,
            anyTitle,
            anyDescription,
            TicketStatus.PENDING, // valid for priority change
            TicketPriority.LOW,
            anyTime,
            anyUpdateTime,
            anyWorker,
            anyCustomer,
            anyPartnerId);

    assertThatThrownBy(() -> ticket.withPriority(TicketPriority.HIGH, null))
        .isInstanceOf(IllegalTicketPriorityException.class)
        .hasMessageContaining("Ticket priority or update time passed cannot be null");
  }

  private static Stream<Arguments> provideInvalidDescriptionUpdateArgs() {
    return Stream.of(
        Arguments.of(null, UPDATE_AT),
        Arguments.of(anyDescription, null),
        Arguments.of(null, null));
  }

  @ParameterizedTest
  @MethodSource("provideInvalidDescriptionUpdateArgs")
  @DisplayName("updateTicketDescription throws error if description or updateAt is null")
  void updateTicketDescriptionThrowsErrorWhenArgumentsAreNull(
      TicketDescription description, TicketUpdateAt updateAt) {

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
            anyWorker,
            anyCustomer,
            anyPartnerId);

    assertThatThrownBy(() -> ticket.updateTicketDescription(description, updateAt))
        .isInstanceOf(IllegalTicketDescriptionUpdateException.class)
        .hasMessageContaining("Ticket description or update time passed cannot be null");
  }

  @ParameterizedTest
  @EnumSource(
      value = TicketStatus.class,
      names = {"CLOSED", "ARCHIVED"})
  @DisplayName("updateTicketDescription throws error for CLOSED and ARCHIVED status")
  void updateTicketDescriptionThrowsErrorForInvalidStatus(TicketStatus status) {

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
            anyWorker,
            anyCustomer,
            anyPartnerId);

    var newDescription = new TicketDescription("Updated description");

    assertThatThrownBy(() -> ticket.updateTicketDescription(newDescription, UPDATE_AT))
        .isInstanceOf(IllegalTicketDescriptionUpdateException.class)
        .hasMessageContaining("TicketStatus: " + status);
  }

  @ParameterizedTest
  @EnumSource(
      value = TicketStatus.class,
      names = {"PENDING", "OPEN"})
  @DisplayName("updateTicketDescription updates description for valid statuses")
  void updateTicketDescriptionUpdatesForValidStatus(TicketStatus status) {

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
            anyWorker,
            anyCustomer,
            anyPartnerId);

    var newDescription = new TicketDescription("Updated description");

    var result = ticket.updateTicketDescription(newDescription, UPDATE_AT);

    assertThat(result).isNotSameAs(ticket);
    assertThat(result.description()).isEqualTo(newDescription);
    assertThat(result.updatedAt()).isEqualTo(UPDATE_AT);

    // ensure immutability consistency
    assertThat(result.id()).isEqualTo(ticket.id());
    assertThat(result.status()).isEqualTo(ticket.status());
  }

  @Test
  @DisplayName("Constructor throws error if createdAt is after updatedAt")
  void constructorThrowsErrorIfCreatedAtIsAfterUpdatedAt() {

    Instant base = Instant.now();

    // createdAt is AFTER updatedAt → invalid
    TicketCreatedAt createdAt = new TicketCreatedAt(base.plusSeconds(60));
    TicketUpdateAt updatedAt = TicketUpdateAt.of(base);

    assertThatThrownBy(
            () ->
                new Ticket(
                    anyId,
                    anySlug,
                    anyTitle,
                    anyDescription,
                    TicketStatus.PENDING,
                    TicketPriority.LOW,
                    createdAt,
                    updatedAt,
                    anyWorker,
                    anyCustomer,
                    anyPartnerId))
        .isInstanceOf(InvalidTicketException.class)
        .hasMessageContaining("Ticket updatedAt cannot be before the createdAt time");
  }

  @Test
  @DisplayName("Constructor allows createdAt equal or before updatedAt")
  void constructorAllowsCreatedAtBeforeOrEqualUpdatedAt() {

    Instant base = Instant.now();

    TicketCreatedAt createdAt = new TicketCreatedAt(base);
    TicketUpdateAt updatedAt = TicketUpdateAt.of(base.plusSeconds(60));

    Ticket equalTicket =
        new Ticket(
            anyId,
            anySlug,
            anyTitle,
            anyDescription,
            TicketStatus.PENDING,
            TicketPriority.LOW,
            createdAt,
            TicketUpdateAt.of(base),
            anyWorker,
            anyCustomer,
            anyPartnerId);

    Ticket beforeTicket =
        new Ticket(
            anyId,
            anySlug,
            anyTitle,
            anyDescription,
            TicketStatus.PENDING,
            TicketPriority.LOW,
            createdAt,
            TicketUpdateAt.of(updatedAt.updatedAt()),
            anyWorker,
            anyCustomer,
            anyPartnerId);

    assertThat(equalTicket).isNotNull();
    assertThat(beforeTicket).isNotNull();
  }
}
