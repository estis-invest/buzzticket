package com.efpcode.domain.staffinvitation.model;

import static org.assertj.core.api.Assertions.*;

import com.efpcode.domain.partner.model.PartnerId;
import com.efpcode.domain.staffinvitation.exceptions.IllegalStaffInvitationArgumentException;
import com.efpcode.domain.staffinvitation.exceptions.InvalidStaffInvitationDateException;
import com.efpcode.domain.staffinvitation.exceptions.InvalidStaffInvitationStatusException;
import com.efpcode.domain.testsupport.TestUUIDIds;
import com.efpcode.domain.user.exceptions.IllegalUserRolePrivilegeException;
import com.efpcode.domain.user.model.UserEmail;
import com.efpcode.domain.user.model.UserId;
import com.efpcode.domain.user.model.UserRole;
import java.time.Instant;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class StaffInvitationTest {

  private static final Instant NOW = Instant.parse("2026-05-08T12:00:00Z");
  private static final Instant EXPIRES_AT = NOW.plusSeconds(120);
  private static final Instant ACCEPTED_AT = NOW.plusSeconds(30);
  private static final Instant EXPIRED_AT = EXPIRES_AT.plusSeconds(1);

  private static final StaffInvitationId ANY_INVITATION_ID = TestUUIDIds.invitationId();
  private static final UserId ANY_INVITED_BY_USER_ID = TestUUIDIds.userId();
  private static final UserEmail ANY_EMAIL = new UserEmail("support@example.com");
  private static final UserRole ANY_ROLE = UserRole.SUPPORT;
  private static final PartnerId ANY_PARTNER_ID = TestUUIDIds.partnerId();
  private static final StaffInvitationTokenHash ANY_TOKEN_HASH =
      StaffInvitationTokenHash.of("hashed-token");
  private static final StaffInvitationCreatedAt ANY_CREATED_AT = new StaffInvitationCreatedAt(NOW);
  private static final StaffInvitationExpiresAt ANY_EXPIRES_AT =
      StaffInvitationExpiresAt.of(EXPIRES_AT, NOW);
  private static final StaffInvitationUpdatedAt ANY_UPDATED_AT = new StaffInvitationUpdatedAt(NOW);

  private static Stream<Arguments> invalidArgumentsThatFail() {
    return Stream.of(
        Arguments.of(
            null,
            ANY_INVITED_BY_USER_ID,
            ANY_EMAIL,
            ANY_ROLE,
            ANY_PARTNER_ID,
            ANY_TOKEN_HASH,
            StaffInvitationStatus.PENDING,
            ANY_CREATED_AT,
            ANY_EXPIRES_AT,
            ANY_UPDATED_AT,
            Optional.empty(),
            Optional.empty(),
            "Invitation Id cannot be null"),
        Arguments.of(
            ANY_INVITATION_ID,
            null,
            ANY_EMAIL,
            ANY_ROLE,
            ANY_PARTNER_ID,
            ANY_TOKEN_HASH,
            StaffInvitationStatus.PENDING,
            ANY_CREATED_AT,
            ANY_EXPIRES_AT,
            ANY_UPDATED_AT,
            Optional.empty(),
            Optional.empty(),
            "Invited by user Id cannot be null"),
        Arguments.of(
            ANY_INVITATION_ID,
            ANY_INVITED_BY_USER_ID,
            null,
            ANY_ROLE,
            ANY_PARTNER_ID,
            ANY_TOKEN_HASH,
            StaffInvitationStatus.PENDING,
            ANY_CREATED_AT,
            ANY_EXPIRES_AT,
            ANY_UPDATED_AT,
            Optional.empty(),
            Optional.empty(),
            "Invitation inviteeEmail cannot be null"),
        Arguments.of(
            ANY_INVITATION_ID,
            ANY_INVITED_BY_USER_ID,
            ANY_EMAIL,
            null,
            ANY_PARTNER_ID,
            ANY_TOKEN_HASH,
            StaffInvitationStatus.PENDING,
            ANY_CREATED_AT,
            ANY_EXPIRES_AT,
            ANY_UPDATED_AT,
            Optional.empty(),
            Optional.empty(),
            "Invitation role cannot be null"),
        Arguments.of(
            ANY_INVITATION_ID,
            ANY_INVITED_BY_USER_ID,
            ANY_EMAIL,
            ANY_ROLE,
            null,
            ANY_TOKEN_HASH,
            StaffInvitationStatus.PENDING,
            ANY_CREATED_AT,
            ANY_EXPIRES_AT,
            ANY_UPDATED_AT,
            Optional.empty(),
            Optional.empty(),
            "Invitation partner cannot be null"),
        Arguments.of(
            ANY_INVITATION_ID,
            ANY_INVITED_BY_USER_ID,
            ANY_EMAIL,
            ANY_ROLE,
            ANY_PARTNER_ID,
            null,
            StaffInvitationStatus.PENDING,
            ANY_CREATED_AT,
            ANY_EXPIRES_AT,
            ANY_UPDATED_AT,
            Optional.empty(),
            Optional.empty(),
            "Invitation token cannot be null"),
        Arguments.of(
            ANY_INVITATION_ID,
            ANY_INVITED_BY_USER_ID,
            ANY_EMAIL,
            ANY_ROLE,
            ANY_PARTNER_ID,
            ANY_TOKEN_HASH,
            null,
            ANY_CREATED_AT,
            ANY_EXPIRES_AT,
            ANY_UPDATED_AT,
            Optional.empty(),
            Optional.empty(),
            "Invitation status cannot be null"),
        Arguments.of(
            ANY_INVITATION_ID,
            ANY_INVITED_BY_USER_ID,
            ANY_EMAIL,
            ANY_ROLE,
            ANY_PARTNER_ID,
            ANY_TOKEN_HASH,
            StaffInvitationStatus.PENDING,
            null,
            ANY_EXPIRES_AT,
            ANY_UPDATED_AT,
            Optional.empty(),
            Optional.empty(),
            "Created date for invitation cannot be null"),
        Arguments.of(
            ANY_INVITATION_ID,
            ANY_INVITED_BY_USER_ID,
            ANY_EMAIL,
            ANY_ROLE,
            ANY_PARTNER_ID,
            ANY_TOKEN_HASH,
            StaffInvitationStatus.PENDING,
            ANY_CREATED_AT,
            null,
            ANY_UPDATED_AT,
            Optional.empty(),
            Optional.empty(),
            "Expires date for invitation cannot be null"),
        Arguments.of(
            ANY_INVITATION_ID,
            ANY_INVITED_BY_USER_ID,
            ANY_EMAIL,
            ANY_ROLE,
            ANY_PARTNER_ID,
            ANY_TOKEN_HASH,
            StaffInvitationStatus.PENDING,
            ANY_CREATED_AT,
            ANY_EXPIRES_AT,
            null,
            Optional.empty(),
            Optional.empty(),
            "Update date for invitation cannot be null"),
        Arguments.of(
            ANY_INVITATION_ID,
            ANY_INVITED_BY_USER_ID,
            ANY_EMAIL,
            ANY_ROLE,
            ANY_PARTNER_ID,
            ANY_TOKEN_HASH,
            StaffInvitationStatus.PENDING,
            ANY_CREATED_AT,
            ANY_EXPIRES_AT,
            ANY_UPDATED_AT,
            null,
            Optional.empty(),
            "Accepts date for invitation cannot be null"),
        Arguments.of(
            ANY_INVITATION_ID,
            ANY_INVITED_BY_USER_ID,
            ANY_EMAIL,
            ANY_ROLE,
            ANY_PARTNER_ID,
            ANY_TOKEN_HASH,
            StaffInvitationStatus.PENDING,
            ANY_CREATED_AT,
            ANY_EXPIRES_AT,
            ANY_UPDATED_AT,
            Optional.empty(),
            null,
            "Accepted by user id invitation cannot be null"));
  }

  @ParameterizedTest
  @MethodSource("invalidArgumentsThatFail")
  @DisplayName("StaffInvitation throws NullPointerException if any required field is null")
  void staffInvitationThrowsNullPointerExceptionIfAnyRequiredFieldIsNull(
      StaffInvitationId invitationId,
      UserId invitedByUserId,
      UserEmail email,
      UserRole role,
      PartnerId partnerId,
      StaffInvitationTokenHash invitationTokenHash,
      StaffInvitationStatus invitationStatus,
      StaffInvitationCreatedAt invitationCreatedAt,
      StaffInvitationExpiresAt invitationExpiresAt,
      StaffInvitationUpdatedAt invitationUpdatedAt,
      Optional<StaffInvitationAcceptedAt> acceptedAt,
      Optional<UserId> acceptedUserId,
      String expectedMessage) {

    assertThatThrownBy(
            () ->
                new StaffInvitation(
                    invitationId,
                    invitedByUserId,
                    email,
                    role,
                    partnerId,
                    invitationTokenHash,
                    invitationStatus,
                    invitationCreatedAt,
                    invitationExpiresAt,
                    invitationUpdatedAt,
                    acceptedAt,
                    acceptedUserId))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining(expectedMessage);
  }

  @Test
  @DisplayName("StaffInvitation create returns pending invitation")
  void staffInvitationCreateReturnsPendingInvitation() {
    StaffInvitation result =
        StaffInvitation.create(
            ANY_INVITATION_ID,
            ANY_INVITED_BY_USER_ID,
            ANY_EMAIL,
            ANY_ROLE,
            ANY_PARTNER_ID,
            ANY_TOKEN_HASH,
            NOW,
            EXPIRES_AT);

    assertThat(result.invitationStatus()).isEqualTo(StaffInvitationStatus.PENDING);
    assertThat(result.acceptedAt()).isEmpty();
    assertThat(result.acceptedUserId()).isEmpty();
    assertThat(result.invitationUpdatedAt().time()).isAfterOrEqualTo(NOW);
  }

  private static Stream<Arguments> createMethodArgumentsThatFail() {
    return Stream.of(
        Arguments.of(null, EXPIRES_AT, ANY_ROLE, "Current time cannot be null"),
        Arguments.of(NOW, null, ANY_ROLE, "Expires time cannot be null"),
        Arguments.of(NOW, EXPIRES_AT, null, "Invitation role cannot be null"));
  }

  @ParameterizedTest
  @MethodSource("createMethodArgumentsThatFail")
  @DisplayName("StaffInvitation create throws error when required method arguments are null")
  void staffInvitationCreateThrowsErrorWhenRequiredMethodArgumentsAreNull(
      Instant currentTime, Instant expiryTime, UserRole role, String expectedMessage) {
    assertThatThrownBy(
            () ->
                StaffInvitation.create(
                    ANY_INVITATION_ID,
                    ANY_INVITED_BY_USER_ID,
                    ANY_EMAIL,
                    role,
                    ANY_PARTNER_ID,
                    ANY_TOKEN_HASH,
                    currentTime,
                    expiryTime))
        .isInstanceOf(IllegalStaffInvitationArgumentException.class)
        .hasMessageContaining(expectedMessage);
  }

  @Test
  @DisplayName("StaffInvitation create rejects customer role")
  void staffInvitationCreateRejectsCustomerRole() {
    assertThatThrownBy(
            () ->
                StaffInvitation.create(
                    ANY_INVITATION_ID,
                    ANY_INVITED_BY_USER_ID,
                    ANY_EMAIL,
                    UserRole.CUSTOMER,
                    ANY_PARTNER_ID,
                    ANY_TOKEN_HASH,
                    NOW,
                    EXPIRES_AT))
        .isInstanceOf(IllegalUserRolePrivilegeException.class)
        .hasMessageContaining("This role is CUSTOMER and cannot be created by an ADMIN user");
  }

  @Test
  @DisplayName("Accepted status requires acceptance metadata")
  void acceptedStatusRequiresAcceptanceMetadata() {
    assertThatThrownBy(
            () ->
                new StaffInvitation(
                    ANY_INVITATION_ID,
                    ANY_INVITED_BY_USER_ID,
                    ANY_EMAIL,
                    ANY_ROLE,
                    ANY_PARTNER_ID,
                    ANY_TOKEN_HASH,
                    StaffInvitationStatus.ACCEPTED,
                    ANY_CREATED_AT,
                    ANY_EXPIRES_AT,
                    ANY_UPDATED_AT,
                    Optional.empty(),
                    Optional.empty()))
        .isInstanceOf(InvalidStaffInvitationStatusException.class)
        .hasMessageContaining("Accepted invitation requires acceptedAt and acceptedUserId");
  }

  private static Stream<Arguments> acceptedStatusIncompleteMetadataCases() {
    return Stream.of(
        Arguments.of(Optional.of(new StaffInvitationAcceptedAt(ACCEPTED_AT)), Optional.empty()),
        Arguments.of(Optional.empty(), Optional.of(TestUUIDIds.userId())));
  }

  @ParameterizedTest
  @MethodSource("acceptedStatusIncompleteMetadataCases")
  @DisplayName("Accepted status requires both acceptedAt and acceptedUserId")
  void acceptedStatusRequiresBothAcceptedAtAndAcceptedUserId(
      Optional<StaffInvitationAcceptedAt> acceptedAt, Optional<UserId> acceptedUserId) {
    assertThatThrownBy(
            () ->
                new StaffInvitation(
                    ANY_INVITATION_ID,
                    ANY_INVITED_BY_USER_ID,
                    ANY_EMAIL,
                    ANY_ROLE,
                    ANY_PARTNER_ID,
                    ANY_TOKEN_HASH,
                    StaffInvitationStatus.ACCEPTED,
                    ANY_CREATED_AT,
                    ANY_EXPIRES_AT,
                    ANY_UPDATED_AT,
                    acceptedAt,
                    acceptedUserId))
        .isInstanceOf(InvalidStaffInvitationStatusException.class)
        .hasMessageContaining("Accepted invitation requires acceptedAt and acceptedUserId");
  }

  @Test
  @DisplayName("Pending status cannot have acceptance metadata")
  void pendingStatusCannotHaveAcceptanceMetadata() {
    assertThatThrownBy(
            () ->
                new StaffInvitation(
                    ANY_INVITATION_ID,
                    ANY_INVITED_BY_USER_ID,
                    ANY_EMAIL,
                    ANY_ROLE,
                    ANY_PARTNER_ID,
                    ANY_TOKEN_HASH,
                    StaffInvitationStatus.PENDING,
                    ANY_CREATED_AT,
                    ANY_EXPIRES_AT,
                    ANY_UPDATED_AT,
                    Optional.of(new StaffInvitationAcceptedAt(ACCEPTED_AT)),
                    Optional.of(TestUUIDIds.userId())))
        .isInstanceOf(InvalidStaffInvitationStatusException.class)
        .hasMessageContaining("Only accepted invitations can have acceptance metadata");
  }

  private static Stream<Arguments> nonAcceptedStatusMetadataCases() {
    return Stream.of(
        Arguments.of(
            StaffInvitationStatus.PENDING,
            Optional.of(new StaffInvitationAcceptedAt(ACCEPTED_AT)),
            Optional.empty()),
        Arguments.of(
            StaffInvitationStatus.PENDING, Optional.empty(), Optional.of(TestUUIDIds.userId())),
        Arguments.of(
            StaffInvitationStatus.EXPIRED,
            Optional.of(new StaffInvitationAcceptedAt(ACCEPTED_AT)),
            Optional.empty()),
        Arguments.of(
            StaffInvitationStatus.EXPIRED, Optional.empty(), Optional.of(TestUUIDIds.userId())));
  }

  @ParameterizedTest
  @MethodSource("nonAcceptedStatusMetadataCases")
  @DisplayName("Non accepted status cannot have partial acceptance metadata")
  void nonAcceptedStatusCannotHavePartialAcceptanceMetadata(
      StaffInvitationStatus invitationStatus,
      Optional<StaffInvitationAcceptedAt> acceptedAt,
      Optional<UserId> acceptedUserId) {
    assertThatThrownBy(
            () ->
                new StaffInvitation(
                    ANY_INVITATION_ID,
                    ANY_INVITED_BY_USER_ID,
                    ANY_EMAIL,
                    ANY_ROLE,
                    ANY_PARTNER_ID,
                    ANY_TOKEN_HASH,
                    invitationStatus,
                    ANY_CREATED_AT,
                    ANY_EXPIRES_AT,
                    ANY_UPDATED_AT,
                    acceptedAt,
                    acceptedUserId))
        .isInstanceOf(InvalidStaffInvitationStatusException.class)
        .hasMessageContaining("Only accepted invitations can have acceptance metadata");
  }

  @Nested
  class StaffInvitationLifecycleTest {
    private StaffInvitation pending;
    private StaffInvitation accepted;
    private StaffInvitation expired;
    private UserId acceptedUserId;

    @BeforeEach
    void setUp() {
      acceptedUserId = TestUUIDIds.userId();
      pending =
          StaffInvitation.create(
              ANY_INVITATION_ID,
              ANY_INVITED_BY_USER_ID,
              ANY_EMAIL,
              ANY_ROLE,
              ANY_PARTNER_ID,
              ANY_TOKEN_HASH,
              NOW,
              EXPIRES_AT);
      accepted = pending.accept(ACCEPTED_AT, acceptedUserId);
      expired = pending.expire(EXPIRED_AT);
    }

    @Test
    @DisplayName("Pending invitation cannot be expired before natural expiry")
    void pendingInvitationCannotBeExpiredBeforeNaturalExpiry() {
      Instant cancelTime = NOW.plusSeconds(10); // BEFORE EXPIRES_AT

      assertThatThrownBy(() -> pending.expire(cancelTime))
          .isInstanceOf(InvalidStaffInvitationDateException.class)
          .hasMessageContaining("Invitation has not expired yet");
    }

    @Test
    @DisplayName("Pending invitation can be cancelled by cancel method before expiry")
    void pendingInvitationCanBeCancelledByCancelMethodBeforeExpiry() {
      Instant cancelTime = NOW.plusSeconds(10);

      StaffInvitation cancelled = pending.cancel(cancelTime);

      assertThat(cancelled.invitationStatus()).isEqualTo(StaffInvitationStatus.EXPIRED);

      assertThat(cancelled.acceptedAt()).isEmpty();
      assertThat(cancelled.acceptedUserId()).isEmpty();
      assertThat(cancelled.invitationUpdatedAt().time()).isAfterOrEqualTo(cancelTime);
    }

    @Test
    @DisplayName("Invitation cannot be cancelled by cancel method if not PENDING throws error")
    void invitationCannotBeCancelledByCancelMethodIfNotPendingThrowsError() {
      Instant cancelTime = NOW.plusSeconds(10);

      assertThatThrownBy(() -> accepted.cancel(cancelTime))
          .isInstanceOf(InvalidStaffInvitationStatusException.class)
          .hasMessageContaining("Only pending invitations can be cancelled");
    }

    @Test
    @DisplayName("Invitation cancel method throws error if status expires status")
    void invitationCancelMethodThrowsErrorIfStatusExpiresStatus() {
      Instant cancelTime = NOW.plusSeconds(10);
      assertThatThrownBy(() -> expired.cancel(cancelTime))
          .isInstanceOf(InvalidStaffInvitationStatusException.class)
          .hasMessageContaining("Only pending invitations can be cancelled");
    }

    @Test
    @DisplayName("Pending invitation can be accepted")
    void pendingInvitationCanBeAccepted() {
      StaffInvitation result = pending.accept(ACCEPTED_AT, acceptedUserId);

      assertThat(result.invitationStatus()).isEqualTo(StaffInvitationStatus.ACCEPTED);
      assertThat(result.acceptedAt()).contains(new StaffInvitationAcceptedAt(ACCEPTED_AT));
      assertThat(result.acceptedUserId()).contains(acceptedUserId);
      assertThat(result.invitationUpdatedAt().time()).isAfterOrEqualTo(ACCEPTED_AT);
    }

    @Test
    @DisplayName("Pending invitation can be expired")
    void pendingInvitationCanBeExpired() {
      StaffInvitation result = pending.expire(EXPIRED_AT);

      assertThat(result.invitationStatus()).isEqualTo(StaffInvitationStatus.EXPIRED);
      assertThat(result.acceptedAt()).isEmpty();
      assertThat(result.acceptedUserId()).isEmpty();
      assertThat(result.invitationUpdatedAt().time()).isAfterOrEqualTo(EXPIRED_AT);
    }

    @Test
    @DisplayName("Pending invitation cannot be accepted after expiry")
    void pendingInvitationCannotBeAcceptedAfterExpiry() {
      assertThatThrownBy(() -> pending.accept(EXPIRED_AT, acceptedUserId))
          .isInstanceOf(InvalidStaffInvitationDateException.class)
          .hasMessageContaining("Expiration for invitation has passed");
    }

    @Test
    @DisplayName("Pending invitation cannot be expired before expiry time")
    void pendingInvitationCannotBeExpiredBeforeExpiryTime() {
      assertThatThrownBy(() -> pending.expire(ACCEPTED_AT))
          .isInstanceOf(InvalidStaffInvitationDateException.class)
          .hasMessageContaining("Invitation has not expired yet");
    }

    @Test
    @DisplayName("Accepted invitation cannot be accepted again")
    void acceptedInvitationCannotBeAcceptedAgain() {
      assertThatThrownBy(() -> accepted.accept(ACCEPTED_AT, acceptedUserId))
          .isInstanceOf(InvalidStaffInvitationStatusException.class)
          .hasMessageContaining("Invitation cannot be accepted when status is ACCEPTED");
    }

    @Test
    @DisplayName("Expired invitation cannot be accepted")
    void expiredInvitationCannotBeAccepted() {
      assertThatThrownBy(() -> expired.accept(EXPIRED_AT, acceptedUserId))
          .isInstanceOf(InvalidStaffInvitationStatusException.class)
          .hasMessageContaining("Invitation cannot be accepted when status is EXPIRED");
    }
  }
}
