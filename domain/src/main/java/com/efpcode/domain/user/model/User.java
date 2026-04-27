package com.efpcode.domain.user.model;

import com.efpcode.domain.partner.model.PartnerId;
import com.efpcode.domain.user.exceptions.IllegalUserArgumentException;
import com.efpcode.domain.user.exceptions.InvalidUserRolePartnerMissingException;
import com.efpcode.domain.user.exceptions.UserStatusChangeException;
import java.util.Objects;
import java.util.Optional;

public record User(
    UserId id,
    UserName name,
    UserEmail email,
    UserPassword password,
    UserRole role,
    UserAccountStatus status,
    UserCreatedAt userCreatedAt,
    UserUpdateAt userUpdateAt,
    Optional<PartnerId> partnerId) {

  public User {
    Objects.requireNonNull(id, "Id cannot be null");
    Objects.requireNonNull(name, "Name cannot be null");
    Objects.requireNonNull(email, "Email cannot be null");
    Objects.requireNonNull(password, "Password cannot be null");
    Objects.requireNonNull(role, "User role cannot be null");
    Objects.requireNonNull(status, "User status cannot be null");
    Objects.requireNonNull(userCreatedAt, "userCreatedAt cannot be null");
    Objects.requireNonNull(userUpdateAt, "userUpdateAt cannot be null");
    Objects.requireNonNull(partnerId, "Optional <Partner> cannot be null");

    if (role.requiresPartner() && partnerId.isEmpty())
      throw new InvalidUserRolePartnerMissingException("User role must have a partnerId");
  }

  public User deactivate() {
    if (!isActive())
      throw new UserStatusChangeException(
          String.format("User is already %s. Cannot be deactivated", this.status));
    return new User(
        id,
        name,
        email,
        password,
        role,
        UserAccountStatus.DEACTIVATED,
        userCreatedAt,
        UserUpdateAt.createNow(),
        partnerId);
  }

  public User activate() {
    if (isActive())
      throw new UserStatusChangeException(
          String.format("User is already %s. Cannot be activated", this.status));
    return new User(
        id,
        name,
        email,
        password,
        role,
        UserAccountStatus.ACTIVATED,
        userCreatedAt,
        UserUpdateAt.createNow(),
        partnerId);
  }

  public User promoteToAdmin() {
    ensureActiveUser();
    return withRole(this.role.promote());
  }

  public User demoteToSupport() {
    ensureActiveUser();

    return withRole(this.role.demote());
  }

  public boolean canAssignTickets() {
    return isActive() && (this.role.canAssignTicket());
  }

  public boolean isActive() {
    return this.status().isActive();
  }

  public User createStaffMember(
      UserId id, UserName name, UserEmail email, UserPassword password, UserRole staffRole) {
    ensureActiveUser();
    this.role.roleGuardIsAdmin();
    Objects.requireNonNull(staffRole, "User role cannot be null");
    staffRole.roleGuardIsStaff();
    PartnerId partnerId =
        this.partnerId.orElseThrow(
            () -> new InvalidUserRolePartnerMissingException("User role must have a partnerId"));
    return new User(
        id,
        name,
        email,
        password,
        staffRole,
        UserAccountStatus.ACTIVATED,
        UserCreatedAt.createNow(),
        UserUpdateAt.createNow(),
        Optional.of(partnerId));
  }

  private User withRole(UserRole newRole) {
    return new User(
        id,
        name,
        email,
        password,
        newRole,
        status,
        userCreatedAt,
        UserUpdateAt.createNow(),
        partnerId);
  }

  private void ensureActiveUser() {
    if (!isActive()) {
      throw new UserStatusChangeException(
          String.format("User must have activated status current: %s", this.status()));
    }
  }

  public User changeName(UserName userName) {
    ensureActiveUser();
    if (userName == null) {
      throw new IllegalUserArgumentException("UserName cannot be null");
    }

    return new User(
        id,
        userName,
        email,
        password,
        role,
        status,
        userCreatedAt,
        UserUpdateAt.createNow(),
        partnerId);
  }

  public User changePassword(UserPassword newPassword) {
    ensureActiveUser();
    if (newPassword == null) {
      throw new IllegalUserArgumentException("UserPassword cannot be null");
    }
    return new User(
        id,
        name,
        email,
        newPassword,
        role,
        status,
        userCreatedAt,
        UserUpdateAt.createNow(),
        partnerId);
  }

  public User changeEmail(UserEmail newEmail) {
    ensureActiveUser();
    if (newEmail == null) {
      throw new IllegalUserArgumentException("UserEmail cannot be null");
    }

    return new User(
        id,
        name,
        newEmail,
        password,
        role,
        status,
        userCreatedAt,
        UserUpdateAt.createNow(),
        partnerId);
  }
}
