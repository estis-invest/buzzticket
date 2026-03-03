package com.efpcode.domain.ticket.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.efpcode.domain.ticket.exceptions.MissingUserAssignmentException;
import com.efpcode.domain.ticket.exceptions.TicketAssigmentLimitException;
import com.efpcode.domain.user.model.UserId;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TicketAssigneesTest {
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
}
