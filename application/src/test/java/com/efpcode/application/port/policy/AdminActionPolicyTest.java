package com.efpcode.application.port.policy;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.efpcode.application.context.RequestContext;
import com.efpcode.application.policy.admin.AdminActionPolicy;
import com.efpcode.application.policy.admin.dto.AdminContext;
import com.efpcode.application.testsupport.TestUUIDIds;
import com.efpcode.application.usecase.partner.exceptions.IllegalPartnerStatusException;
import com.efpcode.application.usecase.partner.exceptions.PartnerNotFoundException;
import com.efpcode.application.usecase.user.exceptions.IllegalUserNotFoundException;
import com.efpcode.application.usecase.user.exceptions.IllegalUserStatusException;
import com.efpcode.domain.partner.model.*;
import com.efpcode.domain.partner.port.PartnerRepository;
import com.efpcode.domain.user.exceptions.IllegalUserRolePrivilegeException;
import com.efpcode.domain.user.model.*;
import com.efpcode.domain.user.port.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AdminActionPolicyTest {

  @Mock private UserRepository userRepository;

  @Mock private PartnerRepository partnerRepository;

  @Mock private RequestContext requestContext;

  private AdminActionPolicy adminActionPolicy;

  private User admin;

  private Partner partner;

  private static final UserId ANY_USERID = TestUUIDIds.userId();
  private static final UserName ANY_USERNAME = new UserName("ADMIN");
  private static final UserEmail ANY_EMAIL = new UserEmail("test@test.com");
  private static final UserPassword ANY_PASSWORD = UserPassword.fromHash("abc123!!Z");
  private static final PartnerId ANY_PARTNERID = TestUUIDIds.partnerId();
  private static final PartnerName ANY_PARTNERNAME = new PartnerName("ADMIN TEST");
  private static final PartnerCity ANY_CITY = new PartnerCity("TEST");
  private static final PartnerCountry ANY_COUNTRY = new PartnerCountry("TEST LANDIA");
  private static final PartnerIsoCode ANY_ISO = new PartnerIsoCode("TST");

  @BeforeEach
  void setUp() {

    adminActionPolicy = new AdminActionPolicy(userRepository, partnerRepository);
    admin =
        UserFactory.createAdminUserWithPartner(
            ANY_USERID, ANY_USERNAME, ANY_EMAIL, ANY_PASSWORD, ANY_PARTNERID);
    Partner draftPartner =
        Partner.createDraftPartner(
            ANY_PARTNERID,
            ANY_PARTNERNAME.partnerName(),
            ANY_CITY.partnerCity(),
            ANY_COUNTRY.partnerCountry(),
            ANY_ISO.isoCode());
    partner = draftPartner.toActivate();
  }

  @Test
  @DisplayName("AdminContext returns a valid object")
  void adminContextReturnsAValidObject() {

    when(requestContext.userId()).thenReturn(ANY_USERID);
    when(requestContext.role()).thenReturn(UserRole.ADMIN);
    when(userRepository.findUserById(ANY_USERID)).thenReturn(Optional.of(admin));
    when(partnerRepository.findById(ANY_PARTNERID)).thenReturn(Optional.of(partner));

    AdminContext result = adminActionPolicy.adminValidator(requestContext);

    assertThat(result).isNotNull();
    assertThat(result.admin()).isEqualTo(admin);
    assertThat(result.partner()).isEqualTo(partner);
    verify(partnerRepository, atLeastOnce()).findById(ANY_PARTNERID);
    verify(userRepository, atLeastOnce()).findUserById(ANY_USERID);
  }

  @ParameterizedTest
  @EnumSource(
      value = UserRole.class,
      names = {"CUSTOMER", "SUPPORT"})
  @DisplayName("Non admin role in token throws error")
  void nonAdminRoleInTokenThrowsError(UserRole role) {

    when(requestContext.role()).thenReturn(role);

    assertThatThrownBy(() -> adminActionPolicy.adminValidator(requestContext))
        .isInstanceOf(IllegalUserRolePrivilegeException.class)
        .hasMessageContaining("Action requires ADMIN role, but current role is " + role.toString());

    verifyNoInteractions(userRepository, partnerRepository);
  }

  @Test
  @DisplayName("Admin requester not found will throw error")
  void adminRequesterNotFoundWillThrowError() {
    when(requestContext.role()).thenReturn(UserRole.ADMIN);
    when(requestContext.userId()).thenReturn(ANY_USERID);
    when(userRepository.findUserById(ANY_USERID)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> adminActionPolicy.adminValidator(requestContext))
        .isInstanceOf(IllegalUserNotFoundException.class)
        .hasMessageContaining("Request handler is not found");

    verifyNoInteractions(partnerRepository);
  }

  @Test
  @DisplayName("Token and DB mismatch on UserRole throws error")
  void tokenAndDbMismatchOnUserRoleThrowsError() {
    UserId supportId = TestUUIDIds.userId();
    User support =
        UserFactory.createSupportUserWithPartner(
            supportId,
            new UserName("Support"),
            new UserEmail("support@test.com"),
            new UserPassword("Support1-123"),
            TestUUIDIds.partnerId());
    when(requestContext.role()).thenReturn(UserRole.ADMIN);
    when(requestContext.userId()).thenReturn(supportId);
    when(userRepository.findUserById(supportId)).thenReturn(Optional.of(support));

    assertThatThrownBy(() -> adminActionPolicy.adminValidator(requestContext))
        .isInstanceOf(SecurityException.class)
        .hasMessageContaining("Token role mismatch");

    verifyNoInteractions(partnerRepository);
  }

  @Test
  @DisplayName("Admin requester must have ACTIVATED UserStatus failure throws error")
  void adminRequesterRecordMustHaveActivatedUserStatusFailureThrowsError() {

    var deactivatedAdmin = admin.deactivate();
    var deactivatedAdminId = admin.id();
    when(requestContext.userId()).thenReturn(deactivatedAdminId);
    when(requestContext.role()).thenReturn(UserRole.ADMIN);
    when(userRepository.findUserById(deactivatedAdminId)).thenReturn(Optional.of(deactivatedAdmin));

    assertThatThrownBy(() -> adminActionPolicy.adminValidator(requestContext))
        .isInstanceOf(IllegalUserStatusException.class)
        .hasMessageContaining("Request handler status other than activated");

    verifyNoInteractions(partnerRepository);
  }

  @Test
  @DisplayName("Admin user without partnerId throws PartnerNotFoundException")
  void adminWithoutPartnerThrowsPartnerNotFoundException() {

    User corruptedAdmin = mock(User.class);

    when(requestContext.role()).thenReturn(UserRole.ADMIN);
    when(requestContext.userId()).thenReturn(ANY_USERID);

    when(corruptedAdmin.role()).thenReturn(UserRole.ADMIN);
    when(corruptedAdmin.status()).thenReturn(UserAccountStatus.ACTIVATED);
    when(corruptedAdmin.partnerId()).thenReturn(Optional.empty());

    when(userRepository.findUserById(ANY_USERID)).thenReturn(Optional.of(corruptedAdmin));

    assertThatThrownBy(() -> adminActionPolicy.adminValidator(requestContext))
        .isInstanceOf(PartnerNotFoundException.class)
        .hasMessageContaining("Request handler has no partner");

    verifyNoInteractions(partnerRepository);
  }

  @Test
  @DisplayName("Request handlers partner must be found in records failure throws error")
  void requestHandlersPartnerMustBeFoundInRecordsFailureThrowsError() {

    when(requestContext.userId()).thenReturn(ANY_USERID);
    when(requestContext.role()).thenReturn(UserRole.ADMIN);
    when(userRepository.findUserById(ANY_USERID)).thenReturn(Optional.of(admin));
    when(partnerRepository.findById(ANY_PARTNERID)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> adminActionPolicy.adminValidator(requestContext))
        .isInstanceOf(PartnerNotFoundException.class)
        .hasMessageContaining("Partner for request handler was not found");

    verify(partnerRepository, atLeastOnce()).findById(ANY_PARTNERID);
    verify(userRepository, atLeastOnce()).findUserById(ANY_USERID);
  }

  @Test
  @DisplayName("Request handler Partner must be in active state failure throws error")
  void requestHandlerPartnerMustBeInActiveStateFailureThrowsError() {
    var deactivatedPartner = partner.toDeactivate();
    when(requestContext.userId()).thenReturn(ANY_USERID);
    when(requestContext.role()).thenReturn(UserRole.ADMIN);
    when(userRepository.findUserById(ANY_USERID)).thenReturn(Optional.of(admin));
    when(partnerRepository.findById(ANY_PARTNERID)).thenReturn(Optional.of(deactivatedPartner));

    assertThatThrownBy(() -> adminActionPolicy.adminValidator(requestContext))
        .isInstanceOf(IllegalPartnerStatusException.class)
        .hasMessageContaining("Partner status is not active");

    verify(partnerRepository, atLeastOnce()).findById(ANY_PARTNERID);
    verify(userRepository, atLeastOnce()).findUserById(ANY_USERID);
  }
}
