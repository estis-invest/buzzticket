package com.efpcode.infrastructure.persistence.staffinvitation;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "staff_invitation")
public class StaffInvitationEntity {

  protected StaffInvitationEntity() {}

  @Id
  @Column(name = "staff_invitation_id", unique = true, nullable = false)
  private UUID invitationId;

  @Column(name = "staff_invitation_invited_by_user_id", nullable = false)
  private UUID invitedByUserId;

  @Column(name = "staff_invitation_email", nullable = false)
  private String inviteeEmail;

  @Column(name = "staff_invitation_role", nullable = false)
  private String role;

  @Column(name = "staff_invitation_partner_id", nullable = false)
  private UUID partnerId;

  @Column(name = "staff_invitation_token_hash", nullable = false, unique = true)
  private String invitationTokenHash;

  @Column(name = "staff_invitation_status", nullable = false)
  private String status;

  @Column(name = "staff_invitation_created_at", nullable = false)
  private Instant invitationCreatedAt;

  @Column(name = "staff_invitation_expires_at", nullable = false)
  private Instant invitationExpiresAt;

  @Column(name = "staff_invitation_updated_at", nullable = false)
  private Instant invitationUpdatedAt;

  @Column(name = "staff_invitation_accepted_at")
  private Instant acceptedAt;

  @Column(name = "staff_invitation_accepted_user_id")
  private UUID acceptedUserId;

  public UUID getInvitationId() {
    return invitationId;
  }

  public void setInvitationId(UUID invitationId) {
    this.invitationId = invitationId;
  }

  public UUID getInvitedByUserId() {
    return invitedByUserId;
  }

  public void setInvitedByUserId(UUID invitedByUserId) {
    this.invitedByUserId = invitedByUserId;
  }

  public String getInviteeEmail() {
    return inviteeEmail;
  }

  public void setInviteeEmail(String inviteeEmail) {
    this.inviteeEmail = inviteeEmail;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public UUID getPartnerId() {
    return partnerId;
  }

  public void setPartnerId(UUID partnerId) {
    this.partnerId = partnerId;
  }

  public String getInvitationTokenHash() {
    return invitationTokenHash;
  }

  public void setInvitationTokenHash(String invitationTokenHash) {
    this.invitationTokenHash = invitationTokenHash;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Instant getInvitationCreatedAt() {
    return invitationCreatedAt;
  }

  public void setInvitationCreatedAt(Instant invitationCreatedAt) {
    this.invitationCreatedAt = invitationCreatedAt;
  }

  public Instant getInvitationExpiresAt() {
    return invitationExpiresAt;
  }

  public void setInvitationExpiresAt(Instant invitationExpiresAt) {
    this.invitationExpiresAt = invitationExpiresAt;
  }

  public Instant getInvitationUpdatedAt() {
    return invitationUpdatedAt;
  }

  public void setInvitationUpdatedAt(Instant invitationUpdatedAt) {
    this.invitationUpdatedAt = invitationUpdatedAt;
  }

  public Instant getAcceptedAt() {
    return acceptedAt;
  }

  public void setAcceptedAt(Instant acceptedAt) {
    this.acceptedAt = acceptedAt;
  }

  public UUID getAcceptedUserId() {
    return acceptedUserId;
  }

  public void setAcceptedUserId(UUID acceptedUserId) {
    this.acceptedUserId = acceptedUserId;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    StaffInvitationEntity that = (StaffInvitationEntity) o;
    return Objects.equals(invitationId, that.invitationId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(invitationId);
  }

  @Override
  public String toString() {
    return "StaffInvitationEntity{"
        + "invitationId="
        + invitationId
        + ", invitedByUserId="
        + invitedByUserId
        + ", inviteeEmail='<redacted>'"
        + '\''
        + ", role='"
        + role
        + '\''
        + ", partnerId="
        + partnerId
        + ", status='"
        + status
        + '\''
        + ", invitationCreatedAt="
        + invitationCreatedAt
        + ", invitationExpiresAt="
        + invitationExpiresAt
        + ", invitationUpdatedAt="
        + invitationUpdatedAt
        + ", acceptedAt="
        + acceptedAt
        + ", acceptedUserId="
        + acceptedUserId
        + '}';
  }
}
