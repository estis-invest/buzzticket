package com.efpcode.domain.user.model;

import com.efpcode.domain.partner.model.PartnerId;
import com.efpcode.domain.user.exceptions.IllegalRoleTransitionException;
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
    UserCreatedAt time,
    Optional<PartnerId> partnerId) {

  public User {
    Objects.requireNonNull(id, "Id cannot be null");
    Objects.requireNonNull(name, "Name cannot be null");
    Objects.requireNonNull(email, "Email cannot be null");
    Objects.requireNonNull(password, "Password cannot be null");
    Objects.requireNonNull(role, "User role cannot be null");
    Objects.requireNonNull(status, "User status cannot be null");
    Objects.requireNonNull(time, "Time cannot be null");
    Objects.requireNonNull(partnerId, "Optional <Partner> cannot be null");

    if (role.requiresPartner() && partnerId.isEmpty())
      throw new InvalidUserRolePartnerMissingException("User role must have a partnerId");
  }

  public User deactivate() {
    if (!isActive())
      throw new UserStatusChangeException(
          String.format("User is already %s. Cannot be deactivated", this.status));
    return new User(
        id, name, email, password, role, UserAccountStatus.DEACTIVATED, time, partnerId);
  }

  public User activate() {
    if (isActive())
      throw new UserStatusChangeException(
          String.format("User is already %s. Cannot be activated", this.status));
    return new User(id, name, email, password, role, UserAccountStatus.ACTIVATED, time, partnerId);
  }

  public User promoteToAdmin() {
    if (!isActive()) {
      throw new UserStatusChangeException(
          String.format("User must have activated status current: %s", this.status()));
    }

    if (this.role == UserRole.ADMIN)
      throw new IllegalRoleTransitionException(
          String.format("User is already: %s . Cannot be promoted", this.role));

    var newStatus = this.role.canChangeRoleTo();

    return withRole(newStatus);
  }

  public User demoteToSupport() {
    if (!isActive()) {
      throw new UserStatusChangeException(
          String.format("User must have activated status current: %s", this.status()));
    }
    if (this.role == UserRole.SUPPORT)
      throw new IllegalRoleTransitionException(
          String.format("User is already: %s . Cannot be promoted", this.role));

    var newStatus = this.role.canChangeRoleTo();

    return withRole(newStatus);
  }

  public boolean canAssignTickets() {
    return isActive() && (this.role.canAssignTicket());
  }

  public boolean isActive() {
    return this.status().isActive();
  }

  private User withRole(UserRole newRole) {
    return new User(id, name, email, password, newRole, status, time, partnerId);
  }
}
