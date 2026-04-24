package com.efpcode.infrastructure.persistence.user;

import com.efpcode.infrastructure.persistence.partner.PartnerEntity;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "app_user")
public class UserEntity {

  @Id
  @Column(name = "user_id", unique = true, nullable = false)
  private UUID userId;

  @Column(name = "user_name", nullable = false)
  private String userName;

  @Column(name = "user_email", unique = true, nullable = false)
  private String userEmail;

  @Column(name = "user_password", nullable = false)
  private String userPassword;

  @Column(name = "user_role", nullable = false)
  private String userRole;

  @Column(name = "user_account_status", nullable = false)
  private String userAccountStatus;

  @Column(name = "user_created_at", nullable = false)
  private Instant userCreatedAt;

  @Column(name = "user_updated_at", nullable = false)
  private Instant userUpdatedAt;

  @ManyToOne(optional = true, fetch = FetchType.LAZY)
  @JoinColumn(name = "partner_id", nullable = true)
  private PartnerEntity partner;

  public UUID getUserId() {
    return userId;
  }

  public void setUserId(UUID userId) {
    this.userId = userId;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getUserEmail() {
    return userEmail;
  }

  public void setUserEmail(String userEmail) {
    this.userEmail = userEmail;
  }

  public String getUserPassword() {
    return userPassword;
  }

  public void setUserPassword(String userPassword) {
    this.userPassword = userPassword;
  }

  public String getUserRole() {
    return userRole;
  }

  public void setUserRole(String userRole) {
    this.userRole = userRole;
  }

  public String getUserAccountStatus() {
    return userAccountStatus;
  }

  public void setUserAccountStatus(String userAccountStatus) {
    this.userAccountStatus = userAccountStatus;
  }

  public Instant getUserCreatedAt() {
    return userCreatedAt;
  }

  public void setUserCreatedAt(Instant userCreatedAt) {
    this.userCreatedAt = userCreatedAt;
  }

  public Instant getUserUpdatedAt() {
    return userUpdatedAt;
  }

  public void setUserUpdatedAt(Instant userUpdateAt) {
    this.userUpdatedAt = userUpdateAt;
  }

  public PartnerEntity getPartner() {
    return partner;
  }

  public void setPartner(PartnerEntity partner) {
    this.partner = partner;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    UserEntity that = (UserEntity) o;
    return Objects.equals(userId, that.userId);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(userId);
  }

  @Override
  public String toString() {
    return "UserEntity{"
        + "userId="
        + userId
        + ", userName='"
        + userName
        + '\''
        + ", userEmail='"
        + userEmail
        + '\''
        + ", userRole='"
        + userRole
        + '\''
        + ", userAccountStatus='"
        + userAccountStatus
        + '\''
        + ", userCreatedAt="
        + userCreatedAt
        + ", userUpdatedAt="
        + userUpdatedAt
        + '}';
  }
}
