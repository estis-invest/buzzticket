package com.efpcode.domain.user.model;

import static org.assertj.core.api.Assertions.*;

import com.efpcode.domain.user.exceptions.IllegalRoleTransitionException;
import com.efpcode.domain.user.exceptions.IllegalUserRolePrivilegeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserRoleTest {
  @Test
  @DisplayName("UserRole method demote transition admin to support")
  void userRoleMethodDemoteTransitionAdminToSupport() {

    var adminRole = UserRole.ADMIN;
    var newRole = adminRole.demote();

    assertThat(newRole).isNotEqualTo(adminRole).isEqualTo(UserRole.SUPPORT);
  }

  @Test
  @DisplayName("UserRole method promote transition support to admin")
  void userRoleMethodPromoteTransitionSupportToAdmin() {

    var supportRole = UserRole.SUPPORT;
    var newRole = supportRole.promote();

    assertThat(newRole).isNotEqualTo(supportRole).isEqualTo(UserRole.ADMIN);
  }

  @Test
  @DisplayName("UserRole method promote to throws error if base role is Customer")
  void userRoleMethodPromoteThrowsErrorIfBaseRoleIsCustomer() {

    var customerRole = UserRole.CUSTOMER;

    assertThatThrownBy(customerRole::promote)
        .isInstanceOf(IllegalRoleTransitionException.class)
        .hasMessageContaining("User role " + customerRole + " cannot be promoted");
  }

  @Test
  @DisplayName("UserRole method demote to throws error if base role is Customer")
  void userRoleMethodDemoteThrowsErrorIfBaseRoleIsCustomer() {

    var customerRole = UserRole.CUSTOMER;

    assertThatThrownBy(customerRole::demote)
        .isInstanceOf(IllegalRoleTransitionException.class)
        .hasMessageContaining("User role " + customerRole + " cannot be demoted");
  }

  @Test
  @DisplayName("UserRole method promote cannot Promote Admin throws error")
  void userRoleMethodPromoteCannotPromoteAdminThrowsError() {
    var adminRole = UserRole.ADMIN;

    assertThatThrownBy(adminRole::promote)
        .isInstanceOf(IllegalRoleTransitionException.class)
        .hasMessageContaining("User is already " + adminRole);
  }

  @Test
  @DisplayName("UserRole method demote cannot Promote Admin throws error")
  void userRoleMethodDemoteCannotPromoteAdminThrowsError() {
    var supportRole = UserRole.SUPPORT;

    assertThatThrownBy(supportRole::demote)
        .isInstanceOf(IllegalRoleTransitionException.class)
        .hasMessageContaining("User is already " + supportRole);
  }

  @Test
  @DisplayName("UserRole method requiresPartner is true for roles Admin and Support")
  void userRoleMethodRequiresPartnerIsTrueForRolesAdminAndSupport() {

    assertThat(UserRole.ADMIN.requiresPartner()).isTrue();
    assertThat(UserRole.SUPPORT.requiresPartner()).isTrue();
  }

  @Test
  @DisplayName("UserRole method requiresPartner is false for Customer")
  void userRoleMethodRequiresPartnerIsFalseForCustomer() {

    assertThat(UserRole.CUSTOMER.requiresPartner()).isFalse();
  }

  @Test
  @DisplayName("UserRole method canAssignTicket is true for roles Admin and Support")
  void userRoleMethodCanAssignTicketIsTrueForRolesAdminAndSupport() {

    assertThat(UserRole.ADMIN.canAssignTicket()).isTrue();
    assertThat(UserRole.SUPPORT.canAssignTicket()).isTrue();

    assertThatCode(UserRole.ADMIN::roleGuardAssignTickets).doesNotThrowAnyException();
    assertThatCode(UserRole.SUPPORT::roleGuardAssignTickets).doesNotThrowAnyException();
  }

  @Test
  @DisplayName("UserRole method canAssignTicket is false for Customer")
  void userRoleMethodCanAssignTicketIsFalseForCustomer() {

    assertThat(UserRole.CUSTOMER.canAssignTicket()).isFalse();
  }

  @Test
  @DisplayName("UserRole method roleGuardAssignTickets throws error if UserRole is Customer")
  void userRoleMethodRoleGuardAssignTicketsThrowsErrorIfUserRoleIsCustomer() {

    var customerRole = UserRole.CUSTOMER;

    assertThatThrownBy(customerRole::roleGuardAssignTickets)
        .isInstanceOf(IllegalUserRolePrivilegeException.class)
        .hasMessageContaining("User role: " + customerRole + " cannot assign ticket");
  }
}
