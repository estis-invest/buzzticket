package com.efpcode.domain.user.model;

import com.efpcode.domain.partner.model.PartnerId;
import com.efpcode.domain.user.exceptions.InvalidUserRolePartnerMissingException;
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
    return new User(
        id, name, email, password, role, UserAccountStatus.DEACTIVATED, time, partnerId);
  }

  public User activate() {
    return new User(id, name, email, password, role, UserAccountStatus.ACTIVATED, time, partnerId);
  }

  public User promoteToAdmin() {
    if (this.role != UserRole.SUPPORT || !isActive()) {
      return this;
    }

    return withRole(UserRole.ADMIN);
  }

  public User demoteToSupport() {
    if (this.role != UserRole.ADMIN || !isActive()) {
      return this;
    }
    return withRole(UserRole.SUPPORT);
  }

  public boolean canAssignTickets() {
    return isActive() && (this.role == UserRole.SUPPORT || this.role == UserRole.ADMIN);
  }

  public boolean isActive() {
    return this.status() == UserAccountStatus.ACTIVATED;
  }

  private User withRole(UserRole newRole) {
    return new User(id, name, email, password, newRole, status, time, partnerId);
  }
}
