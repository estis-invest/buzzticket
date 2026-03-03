package com.efpcode.domain.user.model;

import static org.assertj.core.api.Assertions.*;

import com.efpcode.domain.user.exceptions.IllegalRoleTransitionException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserRoleTest {
  @Test
  @DisplayName("UserRole method canChangeRoleTo transition admin to support")
  void userRoleMethodCanChangeRoleToTransitionAdminToSupport() {

    var adminRole = UserRole.ADMIN;
    var newRole = adminRole.canChangeRoleTo();

    assertThat(newRole).isNotEqualTo(adminRole).isEqualTo(UserRole.SUPPORT);
  }

  @Test
  @DisplayName("UserRole method canChangeRoleTo transition support to admin")
  void userRoleMethodCanChangeRoleToTransitionSupportToAdmin() {

    var supportRole = UserRole.SUPPORT;
    var newRole = supportRole.canChangeRoleTo();

    assertThat(newRole).isNotEqualTo(supportRole).isEqualTo(UserRole.ADMIN);
  }

  @Test
  @DisplayName("UserRole method canChangeRole to throws error if base role is Customer")
  void userRoleMethodCanChangeRoleToThrowsErrorIfBaseRoleIsCustomer() {

    var customerRole = UserRole.CUSTOMER;

    assertThatThrownBy(customerRole::canChangeRoleTo)
        .isInstanceOf(IllegalRoleTransitionException.class)
        .hasMessageContaining("User role " + customerRole + " cannot be changed");
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
  }

  @Test
  @DisplayName("UserRole method canAssignTicket is false for Customer")
  void userRoleMethodCanAssignTicketIsFalseForCustomer() {

    assertThat(UserRole.CUSTOMER.canAssignTicket()).isFalse();
  }
}
