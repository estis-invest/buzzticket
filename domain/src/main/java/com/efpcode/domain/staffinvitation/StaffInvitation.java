package com.efpcode.domain.staffinvitation;

import com.efpcode.domain.partner.model.PartnerId;
import com.efpcode.domain.staffinvitation.exceptions.IllegalStaffInvitationArgumentException;
import com.efpcode.domain.staffinvitation.exceptions.InvalidStaffInvitationDateException;
import com.efpcode.domain.staffinvitation.exceptions.InvalidStaffInvitationStatusException;
import com.efpcode.domain.user.model.UserEmail;
import com.efpcode.domain.user.model.UserId;
import com.efpcode.domain.user.model.UserRole;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

public record StaffInvitation(
    StaffInvitationId invitationId,
    UserId invitedByUserId,
    UserEmail inviteeEmail,
    UserRole role,
    PartnerId partnerId,
    StaffInvitationTokenHash invitationTokenHash,
    StaffInvitationStatus invitationStatus,
    StaffInvitationCreatedAt invitationCreatedAt,
    StaffInvitationExpiresAt invitationExpiresAt,
    StaffInvitationUpdatedAt invitationUpdatedAt,
    Optional<StaffInvitationAcceptedAt> acceptedAt,
    Optional<UserId> acceptedUserId) {

  public StaffInvitation {
    Objects.requireNonNull(invitationId, "Invitation Id cannot be null");
    Objects.requireNonNull(invitedByUserId, "Invited by user Id cannot be null");
    Objects.requireNonNull(inviteeEmail, "Invitation inviteeEmail cannot be null");
    Objects.requireNonNull(role, "Invitation role cannot be null");
    Objects.requireNonNull(partnerId, "Invitation partner cannot be null");
    Objects.requireNonNull(invitationTokenHash, "Invitation token cannot be null");
    Objects.requireNonNull(invitationStatus, "Invitation status cannot be null");
    Objects.requireNonNull(invitationCreatedAt, "Created date for invitation cannot be null");
    Objects.requireNonNull(invitationExpiresAt, "Expires date for invitation cannot be null");
    Objects.requireNonNull(invitationUpdatedAt, "Update date for invitation cannot be null");
    Objects.requireNonNull(acceptedAt, "Accepts date for invitation cannot be null");
    Objects.requireNonNull(acceptedUserId, "Accepted by user id invitation cannot be null");
    role.roleGuardIsStaff();

    validateAcceptanceMetadata(invitationStatus, acceptedAt, acceptedUserId);
  }

  public StaffInvitation cancel(Instant currentTime) {
    requireMethodArgument(currentTime, "Current time cannot be null");

    if (this.invitationStatus != StaffInvitationStatus.PENDING) {
      throw new InvalidStaffInvitationStatusException(
          "Only pending invitations can be cancelled", null);
    }

    return new StaffInvitation(
        this.invitationId,
        this.invitedByUserId,
        this.inviteeEmail,
        this.role,
        this.partnerId,
        this.invitationTokenHash,
        StaffInvitationStatus.EXPIRED,
        this.invitationCreatedAt,
        this.invitationExpiresAt,
        new StaffInvitationUpdatedAt(currentTime),
        Optional.empty(),
        Optional.empty());
  }

  public StaffInvitation accept(Instant currentTime, UserId acceptedUserId) {
    requireMethodArgument(currentTime, "Current time cannot be null");
    requireMethodArgument(acceptedUserId, "Accepted user id cannot be null");

    StaffInvitationStatus staffInvitationStatus = this.invitationStatus.accept();

    validateNotExpired(currentTime);

    return new StaffInvitation(
        this.invitationId,
        this.invitedByUserId,
        this.inviteeEmail,
        this.role,
        this.partnerId,
        this.invitationTokenHash,
        staffInvitationStatus,
        this.invitationCreatedAt,
        this.invitationExpiresAt,
        new StaffInvitationUpdatedAt(currentTime),
        Optional.of(new StaffInvitationAcceptedAt(currentTime)),
        Optional.of(acceptedUserId));
  }

  public StaffInvitation expire(Instant currentTime) {
    requireMethodArgument(currentTime, "Current time cannot be null");
    StaffInvitationStatus staffInvitationStatus = this.invitationStatus.expire();
    validateHasExpired(currentTime);

    return new StaffInvitation(
        this.invitationId,
        this.invitedByUserId,
        this.inviteeEmail,
        this.role,
        this.partnerId,
        this.invitationTokenHash,
        staffInvitationStatus,
        this.invitationCreatedAt,
        this.invitationExpiresAt,
        new StaffInvitationUpdatedAt(currentTime),
        Optional.empty(),
        Optional.empty());
  }

  public static StaffInvitation create(
      StaffInvitationId invitationId,
      UserId invitedByUserId,
      UserEmail inviteeEmail,
      UserRole role,
      PartnerId partnerId,
      StaffInvitationTokenHash invitationTokenHash,
      Instant currentTime,
      Instant expiryTime) {

    requireMethodArgument(currentTime, "Current time cannot be null");
    requireMethodArgument(expiryTime, "Expires time cannot be null");
    requireMethodArgument(role, "Invitation role cannot be null");

    StaffInvitationExpiresAt expiresAt = StaffInvitationExpiresAt.of(expiryTime, currentTime);

    return new StaffInvitation(
        invitationId,
        invitedByUserId,
        inviteeEmail,
        role,
        partnerId,
        invitationTokenHash,
        StaffInvitationStatus.PENDING,
        new StaffInvitationCreatedAt(currentTime),
        expiresAt,
        new StaffInvitationUpdatedAt(currentTime),
        Optional.empty(),
        Optional.empty());
  }

  private static void requireMethodArgument(Object object, String errorMessage) {
    if (object == null) throw new IllegalStaffInvitationArgumentException(errorMessage, null);
  }

  private void validateNotExpired(Instant currentTime) {

    if (!currentTime.isBefore(this.invitationExpiresAt.time())) {
      throw new InvalidStaffInvitationDateException("Expiration for invitation has passed", null);
    }
  }

  private void validateHasExpired(Instant currentTime) {
    if (currentTime.isBefore(invitationExpiresAt.time())) {
      throw new InvalidStaffInvitationDateException("Invitation has not expired yet", null);
    }
  }

  private static void validateAcceptanceMetadata(
      StaffInvitationStatus invitationStatus,
      Optional<StaffInvitationAcceptedAt> acceptedAt,
      Optional<UserId> acceptedUserId) {

    boolean hasAcceptedAt = acceptedAt.isPresent();
    boolean hasAcceptedUserId = acceptedUserId.isPresent();

    if (invitationStatus == StaffInvitationStatus.ACCEPTED
        && (!hasAcceptedAt || !hasAcceptedUserId)) {
      throw new InvalidStaffInvitationStatusException(
          "Accepted invitation requires acceptedAt and acceptedUserId", null);
    }

    if (invitationStatus != StaffInvitationStatus.ACCEPTED
        && (hasAcceptedAt || hasAcceptedUserId)) {
      throw new InvalidStaffInvitationStatusException(
          "Only accepted invitations can have acceptance metadata", null);
    }
  }
}
