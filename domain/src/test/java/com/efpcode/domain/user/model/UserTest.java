package com.efpcode.domain.user.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.efpcode.domain.partner.model.PartnerId;
import com.efpcode.domain.testsupport.TestUUIDIds;
import com.efpcode.domain.user.exceptions.*;
import java.time.Instant;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class UserTest {

  // Easy setup things
  private static final UserId ANY_ID = TestUUIDIds.userId();
  private static final UserName ANY_NAME = new UserName("John Doe");
  private static final UserEmail ANY_EMAIL = new UserEmail("test@example.com");
  private static final UserPassword ANY_PASS = new UserPassword("secure123");
  private static final UserCreatedAt ANY_TIME = UserCreatedAt.createNow();
  private static final UserUpdateAt ANY_UPDATE = UserUpdateAt.createNow();
  private static final PartnerId ANY_PARTNER = TestUUIDIds.partnerId();

  private static Stream<Arguments> provideInvalidConstructorArgs() {
    var id = ANY_ID;
    var name = ANY_NAME;
    var email = ANY_EMAIL;
    var pass = ANY_PASS;
    var role = UserRole.CUSTOMER;
    var status = UserAccountStatus.ACTIVATED;
    var time = ANY_TIME;
    var update = ANY_UPDATE;
    var partner = Optional.of(ANY_PARTNER);

    return Stream.of(
        Arguments.of(
            null, name, email, pass, role, status, time, update, partner, "Id cannot be null"),
        Arguments.of(
            id, null, email, pass, role, status, time, update, partner, "Name cannot be null"),
        Arguments.of(
            id, name, null, pass, role, status, time, update, partner, "Email cannot be null"),
        Arguments.of(
            id, name, email, null, role, status, time, update, partner, "Password cannot be null"),
        Arguments.of(
            id, name, email, pass, null, status, time, update, partner, "User role cannot be null"),
        Arguments.of(
            id, name, email, pass, role, null, time, update, partner, "User status cannot be null"),
        Arguments.of(
            id,
            name,
            email,
            pass,
            role,
            status,
            null,
            update,
            partner,
            "userCreatedAt cannot be null"),
        Arguments.of(
            id,
            name,
            email,
            pass,
            role,
            status,
            time,
            null,
            partner,
            "userUpdateAt cannot be null"),
        Arguments.of(
            id,
            name,
            email,
            pass,
            role,
            status,
            time,
            update,
            null,
            "Optional <Partner> cannot be null"));
  }

  @ParameterizedTest
  @MethodSource("provideInvalidConstructorArgs")
  @DisplayName("UserConstructor throws NullPointException If any field is null")
  void userConstructorThrowsNullPointExceptionIfAnyFieldIsNull(
      UserId id,
      UserName name,
      UserEmail email,
      UserPassword password,
      UserRole role,
      UserAccountStatus status,
      UserCreatedAt time,
      UserUpdateAt update,
      Optional<PartnerId> partner,
      String expectedMessage) {
    assertThatThrownBy(
            () -> new User(id, name, email, password, role, status, time, update, partner))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining(expectedMessage);
  }

  @Test
  @DisplayName("User with Customer role can be created without a partner")
  void userWithCustomerRoleCanBeCreatedWithoutAPartner() {
    Optional<PartnerId> noPartner = Optional.empty();

    var customer =
        new User(
            ANY_ID,
            ANY_NAME,
            ANY_EMAIL,
            ANY_PASS,
            UserRole.CUSTOMER,
            UserAccountStatus.ACTIVATED,
            ANY_TIME,
            ANY_UPDATE,
            noPartner);
    assertThat(customer.role()).isEqualTo(UserRole.CUSTOMER);
    assertThat(customer.partnerId()).isEmpty();
  }

  @Test
  @DisplayName("User with Support role requires partner throws error if partner is missing")
  void userWithSupportRoleRequiresPartnerThrowsErrorIfPartnerIsMissing() {
    Optional<PartnerId> partnerId = Optional.empty();

    assertThatThrownBy(
            () ->
                new User(
                    ANY_ID,
                    ANY_NAME,
                    ANY_EMAIL,
                    ANY_PASS,
                    UserRole.SUPPORT,
                    UserAccountStatus.ACTIVATED,
                    ANY_TIME,
                    ANY_UPDATE,
                    partnerId))
        .isInstanceOf(InvalidUserRolePartnerMissingException.class)
        .hasMessageContaining("User role must have a partnerId");
  }

  @Test
  @DisplayName("User with Admin Role requires a partner throws error if partner is Missing")
  void userWithAdminRoleRequiresAPartnerThrowsErrorIfPartnerIsMissing() {
    Optional<PartnerId> partnerId = Optional.empty();

    assertThatThrownBy(
            () ->
                new User(
                    ANY_ID,
                    ANY_NAME,
                    ANY_EMAIL,
                    ANY_PASS,
                    UserRole.ADMIN,
                    UserAccountStatus.ACTIVATED,
                    ANY_TIME,
                    ANY_UPDATE,
                    partnerId))
        .isInstanceOf(InvalidUserRolePartnerMissingException.class)
        .hasMessageContaining("User role must have a partnerId");
  }

  @Test
  @DisplayName("User can only be deactivated if status activated else throws error")
  void userCanOnlyBeDeactivatedIfStatusActivatedElseThrowsError() {

    var deactivedUser =
        new User(
            ANY_ID,
            ANY_NAME,
            ANY_EMAIL,
            ANY_PASS,
            UserRole.SUPPORT,
            UserAccountStatus.DEACTIVATED,
            ANY_TIME,
            ANY_UPDATE,
            Optional.of(ANY_PARTNER));

    assertThatThrownBy(deactivedUser::deactivate)
        .isInstanceOf(UserStatusChangeException.class)
        .hasMessageContaining(
            String.format("User is already %s. Cannot be deactivated", deactivedUser.status()));
  }

  @Test
  @DisplayName("User with activated status can be deactivated")
  void userWithActivatedStatusCanBeDeactivated() {

    var user =
        new User(
            ANY_ID,
            ANY_NAME,
            ANY_EMAIL,
            ANY_PASS,
            UserRole.SUPPORT,
            UserAccountStatus.ACTIVATED,
            ANY_TIME,
            new UserUpdateAt(Instant.now().minusSeconds(5)),
            Optional.of(ANY_PARTNER));

    var deactivatedUser = user.deactivate();

    assertThat(user.status()).isNotEqualTo(deactivatedUser.status());

    assertThat(deactivatedUser.isActive()).isFalse();
    assertThat(deactivatedUser.status()).isEqualTo(UserAccountStatus.DEACTIVATED);
    assertThat(deactivatedUser.userUpdateAt().updatedAt()).isAfter(user.userUpdateAt().updatedAt());
  }

  @Test
  @DisplayName("User can only be activated if status is deactivated else throws errors")
  void userCanOnlyBeActivatedIfStatusIsDeactivatedElseThrowsErrors() {

    var activedUser =
        new User(
            ANY_ID,
            ANY_NAME,
            ANY_EMAIL,
            ANY_PASS,
            UserRole.SUPPORT,
            UserAccountStatus.ACTIVATED,
            ANY_TIME,
            ANY_UPDATE,
            Optional.of(ANY_PARTNER));

    assertThatThrownBy(activedUser::activate)
        .isInstanceOf(UserStatusChangeException.class)
        .hasMessageContaining(
            String.format("User is already %s. Cannot be activated", activedUser.status()));
  }

  @Test
  @DisplayName("User with deactivated status can change to activated status")
  void userWithDeactivatedStatusCanChangeToActivatedStatus() {
    var user =
        new User(
            ANY_ID,
            ANY_NAME,
            ANY_EMAIL,
            ANY_PASS,
            UserRole.SUPPORT,
            UserAccountStatus.DEACTIVATED,
            ANY_TIME,
            ANY_UPDATE,
            Optional.of(ANY_PARTNER));

    var activeUser = user.activate();

    assertThat(user).isNotSameAs(activeUser);
    assertThat(activeUser.status()).isNotEqualTo(user.status());
    assertThat(activeUser.isActive()).isTrue();
  }

  @Test
  @DisplayName("User cannot get promoted if status is deactivated throws error")
  void userCannotGetPromotedIfStatusIsDeactivatedThrowsError() {

    var user =
        new User(
            ANY_ID,
            ANY_NAME,
            ANY_EMAIL,
            ANY_PASS,
            UserRole.SUPPORT,
            UserAccountStatus.DEACTIVATED,
            ANY_TIME,
            ANY_UPDATE,
            Optional.of(ANY_PARTNER));

    assertThatThrownBy(user::promoteToAdmin)
        .isInstanceOf(UserStatusChangeException.class)
        .hasMessageContaining("status current: " + user.status());
  }

  @Test
  @DisplayName("User cannot be promoted if role is already Admin throws error")
  void userCannotBePromotedIfRoleIsAlreadyAdminThrowsError() {
    var user =
        new User(
            ANY_ID,
            ANY_NAME,
            ANY_EMAIL,
            ANY_PASS,
            UserRole.ADMIN,
            UserAccountStatus.ACTIVATED,
            ANY_TIME,
            ANY_UPDATE,
            Optional.of(ANY_PARTNER));

    assertThatThrownBy(user::promoteToAdmin)
        .isInstanceOf(IllegalRoleTransitionException.class)
        .hasMessageContaining("User is already " + user.role());
  }

  @Test
  @DisplayName("User cannot be promoted if role is Customer throws error")
  void userCannotBePromotedIfRoleIsCustomerThrowsError() {
    var user =
        new User(
            ANY_ID,
            ANY_NAME,
            ANY_EMAIL,
            ANY_PASS,
            UserRole.CUSTOMER,
            UserAccountStatus.ACTIVATED,
            ANY_TIME,
            ANY_UPDATE,
            Optional.of(ANY_PARTNER));

    assertThatThrownBy(user::promoteToAdmin)
        .isInstanceOf(IllegalRoleTransitionException.class)
        .hasMessageContaining("User role " + user.role());
  }

  @Test
  @DisplayName("User can be promoted if role is Support and status is activated")
  void userCanBePromotedIfRoleIsSupportAndStatusIsActivated() {

    var user =
        new User(
            ANY_ID,
            ANY_NAME,
            ANY_EMAIL,
            ANY_PASS,
            UserRole.SUPPORT,
            UserAccountStatus.ACTIVATED,
            new UserCreatedAt(Instant.now().minusSeconds(3)),
            ANY_UPDATE,
            Optional.of(ANY_PARTNER));

    var promptedUser = user.promoteToAdmin();

    assertThat(promptedUser).isNotNull().isInstanceOf(User.class).isNotSameAs(user);
    assertThat(promptedUser.role()).isNotEqualTo(user.role());
    assertThat(promptedUser.userUpdateAt()).isNotEqualTo(user.userUpdateAt());
  }

  @Test
  @DisplayName("User cannot be demoted if status is not activated throws error")
  void userCannotBeDemotedIfStatusIsNotActivatedThrowsError() {

    var user =
        new User(
            ANY_ID,
            ANY_NAME,
            ANY_EMAIL,
            ANY_PASS,
            UserRole.ADMIN,
            UserAccountStatus.DEACTIVATED,
            ANY_TIME,
            ANY_UPDATE,
            Optional.of(ANY_PARTNER));

    assertThatThrownBy(user::demoteToSupport)
        .isInstanceOf(UserStatusChangeException.class)
        .hasMessageContaining("status current: " + user.status());
  }

  @Test
  @DisplayName("User cannot be demoted if role is Support throws error")
  void userCannotBeDemotedIfRoleIsSupportThrowsError() {
    var user =
        new User(
            ANY_ID,
            ANY_NAME,
            ANY_EMAIL,
            ANY_PASS,
            UserRole.SUPPORT,
            UserAccountStatus.ACTIVATED,
            ANY_TIME,
            ANY_UPDATE,
            Optional.of(ANY_PARTNER));

    assertThatThrownBy(user::demoteToSupport)
        .isInstanceOf(IllegalRoleTransitionException.class)
        .hasMessageContaining("User is already " + user.role());
  }

  @Test
  @DisplayName("User cannot be demoted if role is Customer throws error")
  void userCannotBeDemotedIfRoleIsCustomerThrowsError() {
    var user =
        new User(
            ANY_ID,
            ANY_NAME,
            ANY_EMAIL,
            ANY_PASS,
            UserRole.CUSTOMER,
            UserAccountStatus.ACTIVATED,
            ANY_TIME,
            ANY_UPDATE,
            Optional.of(ANY_PARTNER));

    assertThatThrownBy(user::demoteToSupport)
        .isInstanceOf(IllegalRoleTransitionException.class)
        .hasMessageContaining("User role " + user.role() + " cannot");
  }

  @Test
  @DisplayName("User can be demoted if role is Admin and status is activated")
  void userCanBeDemotedIfRoleIsAdminAndStatusIsActivated() {

    var user =
        new User(
            ANY_ID,
            ANY_NAME,
            ANY_EMAIL,
            ANY_PASS,
            UserRole.ADMIN,
            UserAccountStatus.ACTIVATED,
            ANY_TIME,
            ANY_UPDATE,
            Optional.of(ANY_PARTNER));

    var demotedUser = user.demoteToSupport();

    assertThat(demotedUser).isNotSameAs(user);
    assertThat(demotedUser.role()).isNotEqualTo(user.role()).isEqualTo(UserRole.SUPPORT);
  }

  @Test
  @DisplayName("User with customer role cannot assign tickets")
  void userWithCustomerRoleCannotAssignTickets() {
    var user =
        new User(
            ANY_ID,
            ANY_NAME,
            ANY_EMAIL,
            ANY_PASS,
            UserRole.CUSTOMER,
            UserAccountStatus.ACTIVATED,
            ANY_TIME,
            ANY_UPDATE,
            Optional.of(ANY_PARTNER));
    assertThat(user.canAssignTickets()).isFalse();
  }

  @Test
  @DisplayName("User that are deactivated cannot assign tickets")
  void userThatAreDeactivatedCannotAssignTickets() {

    var user =
        new User(
            ANY_ID,
            ANY_NAME,
            ANY_EMAIL,
            ANY_PASS,
            UserRole.ADMIN,
            UserAccountStatus.DEACTIVATED,
            ANY_TIME,
            ANY_UPDATE,
            Optional.of(ANY_PARTNER));
    assertThat(user.canAssignTickets()).isFalse();
  }

  @Test
  @DisplayName("User with roles Admin and Support can assign tickets")
  void userWithRolesAdminAndSupportCanAssignTickets() {

    var user =
        new User(
            ANY_ID,
            ANY_NAME,
            ANY_EMAIL,
            ANY_PASS,
            UserRole.ADMIN,
            UserAccountStatus.ACTIVATED,
            ANY_TIME,
            ANY_UPDATE,
            Optional.of(ANY_PARTNER));

    var user2 =
        new User(
            TestUUIDIds.userId(),
            ANY_NAME,
            ANY_EMAIL,
            ANY_PASS,
            UserRole.SUPPORT,
            UserAccountStatus.ACTIVATED,
            ANY_TIME,
            ANY_UPDATE,
            Optional.of(ANY_PARTNER));
    assertThat(user.canAssignTickets()).isTrue();
    assertThat(user2.canAssignTickets()).isTrue();
  }

  @Test
  @DisplayName("Promotion should preserve all user data except role")
  void promotionPreservesUserData() {
    User user =
        UserFactory.createSupportUserWithPartner(
            ANY_ID, ANY_NAME, ANY_EMAIL, ANY_PASS, ANY_PARTNER);

    User promoted = user.promoteToAdmin();

    assertThat(promoted.id()).isEqualTo(user.id());
    assertThat(promoted.name()).isEqualTo(user.name());
    assertThat(promoted.email()).isEqualTo(user.email());
    assertThat(promoted.password()).isEqualTo(user.password());
    assertThat(promoted.status()).isEqualTo(user.status());
    assertThat(promoted.userCreatedAt()).isEqualTo(user.userCreatedAt());
    assertThat(promoted.partnerId()).isEqualTo(user.partnerId());
  }

  @Test
  @DisplayName("Demotion should preserve all user data except role")
  void demotionShouldPreserveAllUserDataExceptRole() {
    User user =
        UserFactory.createAdminUserWithPartner(ANY_ID, ANY_NAME, ANY_EMAIL, ANY_PASS, ANY_PARTNER);
    User demoted = user.demoteToSupport();

    assertThat(demoted.id()).isEqualTo(user.id());
    assertThat(demoted.name()).isEqualTo(user.name());
    assertThat(demoted.email()).isEqualTo(user.email());
    assertThat(demoted.password()).isEqualTo(user.password());
    assertThat(demoted.status()).isEqualTo(user.status());
    assertThat(demoted.userCreatedAt()).isEqualTo(user.userCreatedAt());
    assertThat(demoted.userUpdateAt()).isNotEqualTo(user.userUpdateAt());
    assertThat(demoted.partnerId()).isEqualTo(user.partnerId());
  }

  @Test
  @DisplayName("Deactivated Support user cannot assign tickets")
  void deactivatedSupportCannotAssignTickets() {
    User support =
        UserFactory.createSupportUserWithPartner(ANY_ID, ANY_NAME, ANY_EMAIL, ANY_PASS, ANY_PARTNER)
            .deactivate();
    assertThat(support.canAssignTickets()).isFalse();
  }

  @Test
  @DisplayName("changeName method update User name field")
  void changeNameMethodUpdateUserNameField() {
    var expected = "Jane Doe";
    var user =
        new User(
            ANY_ID,
            ANY_NAME,
            ANY_EMAIL,
            ANY_PASS,
            UserRole.CUSTOMER,
            UserAccountStatus.ACTIVATED,
            ANY_TIME,
            ANY_UPDATE,
            Optional.of(ANY_PARTNER));
    var userUpdateName = user.changeName(new UserName(expected));

    assertThat(userUpdateName.name().name()).isEqualTo(expected);
    assertThat(userUpdateName.name()).isNotEqualTo(user.name());
    assertThat(userUpdateName).isNotSameAs(user);
    assertThat(userUpdateName.userUpdateAt().updatedAt()).isAfter(user.userUpdateAt().updatedAt());
  }

  @Test
  @DisplayName("changeName cannot be called if user is not activated")
  void changeNameCannotBeCalledIfUserIsNotActivated() {
    var expected = new UserName("Jane Doe");
    var user =
        new User(
            ANY_ID,
            ANY_NAME,
            ANY_EMAIL,
            ANY_PASS,
            UserRole.CUSTOMER,
            UserAccountStatus.DEACTIVATED,
            ANY_TIME,
            ANY_UPDATE,
            Optional.of(ANY_PARTNER));
    assertThatThrownBy(() -> user.changeName(expected))
        .isInstanceOf(UserStatusChangeException.class)
        .hasMessageContaining(
            "User must have activated status current: " + UserAccountStatus.DEACTIVATED.name());
  }

  @Test
  @DisplayName("changeName cannot pass null as argument")
  void changeNameCannotPassNullAsArgument() {
    var user =
        new User(
            ANY_ID,
            ANY_NAME,
            ANY_EMAIL,
            ANY_PASS,
            UserRole.CUSTOMER,
            UserAccountStatus.ACTIVATED,
            ANY_TIME,
            ANY_UPDATE,
            Optional.of(ANY_PARTNER));

    assertThatThrownBy(() -> user.changeName(null))
        .isInstanceOf(UserNameArgumentDuplicationException.class)
        .hasMessageContaining("UserName cannot be null");
  }

  @Test
  @DisplayName("changePassword updates current password and returns new instance")
  void changePasswordUpdatesCurrentPasswordAndReturnsNewInstance() {
    var expected = "SECRET101";
    var user =
        new User(
            ANY_ID,
            ANY_NAME,
            ANY_EMAIL,
            ANY_PASS,
            UserRole.CUSTOMER,
            UserAccountStatus.ACTIVATED,
            ANY_TIME,
            ANY_UPDATE,
            Optional.of(ANY_PARTNER));

    var userWithNewPassword = user.changePassword(new UserPassword(expected));

    assertThat(userWithNewPassword).isNotSameAs(user);
    assertThat(userWithNewPassword.password().hashedPassword())
        .isNotEqualTo(user.password().hashedPassword());
    assertThat(userWithNewPassword.password().hashedPassword()).isEqualTo(expected);
    assertThat(userWithNewPassword.userUpdateAt().updatedAt())
        .isAfter(user.userUpdateAt().updatedAt());
  }

  @Test
  @DisplayName("changePassword throws error if user status is not activated")
  void changePasswordThrowsErrorIfUserStatusIsNotActivated() {

    var expected = new UserPassword("SECRET101");
    var user =
        new User(
            ANY_ID,
            ANY_NAME,
            ANY_EMAIL,
            ANY_PASS,
            UserRole.CUSTOMER,
            UserAccountStatus.DEACTIVATED,
            ANY_TIME,
            ANY_UPDATE,
            Optional.of(ANY_PARTNER));

    assertThatThrownBy(() -> user.changePassword(expected))
        .isInstanceOf(UserStatusChangeException.class)
        .hasMessageContaining(
            "User must have activated status current: " + UserAccountStatus.DEACTIVATED.name());
  }

  @Test
  @DisplayName("changePassword throws error if argument passed is null")
  void changePasswordThrowsErrorIfArgumentPassedIsNull() {

    var user =
        new User(
            ANY_ID,
            ANY_NAME,
            ANY_EMAIL,
            ANY_PASS,
            UserRole.CUSTOMER,
            UserAccountStatus.ACTIVATED,
            ANY_TIME,
            ANY_UPDATE,
            Optional.of(ANY_PARTNER));
    assertThatThrownBy(() -> user.changePassword(null))
        .isInstanceOf(UserPasswordNullPointerException.class)
        .hasMessageContaining("UserPassword cannot be null");
  }

  @Test
  @DisplayName("changeEmail updates email and returns new instance object")
  void changeEmailUpdatesEmailAndReturnsNewInstanceObject() {
    var expected = "test@domain.io";
    var user =
        new User(
            ANY_ID,
            ANY_NAME,
            ANY_EMAIL,
            ANY_PASS,
            UserRole.CUSTOMER,
            UserAccountStatus.ACTIVATED,
            ANY_TIME,
            ANY_UPDATE,
            Optional.of(ANY_PARTNER));

    var userUpdateEmail = user.changeEmail(new UserEmail(expected));

    assertThat(user).isNotSameAs(userUpdateEmail);
    assertThat(userUpdateEmail.email().email()).isEqualTo(expected);
    assertThat(userUpdateEmail.userUpdateAt().updatedAt()).isAfter(user.userUpdateAt().updatedAt());
  }

  @Test
  @DisplayName("changeEmail throws error if user is not activated")
  void changeEmailThrowsErrorIfUserIsNotActivated() {
    var expected = new UserEmail("test@domain.io");
    var user =
        new User(
            ANY_ID,
            ANY_NAME,
            ANY_EMAIL,
            ANY_PASS,
            UserRole.CUSTOMER,
            UserAccountStatus.DEACTIVATED,
            ANY_TIME,
            ANY_UPDATE,
            Optional.of(ANY_PARTNER));

    assertThatThrownBy(() -> user.changeEmail(expected))
        .isInstanceOf(UserStatusChangeException.class)
        .hasMessageContaining(
            "User must have activated status current: " + UserAccountStatus.DEACTIVATED.name());
  }

  @Test
  @DisplayName("changeEmail cannot pass null throws exception")
  void changeEmailCannotPassNullThrowsException() {
    var user =
        new User(
            ANY_ID,
            ANY_NAME,
            ANY_EMAIL,
            ANY_PASS,
            UserRole.CUSTOMER,
            UserAccountStatus.ACTIVATED,
            ANY_TIME,
            ANY_UPDATE,
            Optional.of(ANY_PARTNER));
    assertThatThrownBy(() -> user.changeEmail(null))
        .isInstanceOf(UserEmailArgumentDuplicationException.class)
        .hasMessageContaining("UserEmail cannot be null");
  }
}
