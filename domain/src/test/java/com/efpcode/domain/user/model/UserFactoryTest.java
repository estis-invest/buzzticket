package com.efpcode.domain.user.model;

import static org.assertj.core.api.Assertions.*;

import com.efpcode.domain.partner.model.PartnerId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserFactoryTest {

  private static final UserName ANY_NAME = new UserName("John Doe");
  private static final UserEmail ANY_EMAIL = new UserEmail("test@example.com");
  private static final UserPassword ANY_PASS = new UserPassword("secure123");
  private static final PartnerId ANY_PARTNER = PartnerId.generate();

  @Test
  @DisplayName("createAdminUserWithPartner creates a user with an Admin with a Partner")
  void createAdminUserWithPartnerCreatesAUserWithAnAdminWithAPartner() {
    var user = UserFactory.createAdminUserWithPartner(ANY_NAME, ANY_EMAIL, ANY_PASS, ANY_PARTNER);
    assertThat(user).isNotNull().isInstanceOf(User.class);
    assertThat(user.role()).isEqualTo(UserRole.ADMIN);
    assertThat(user.partnerId()).contains(ANY_PARTNER);
  }

  @Test
  @DisplayName("createSupportUserWithPartner creates a user with a Support with a Partner")
  void createSupportUserWithPartnerCreatesAUserWithASupportWithAPartner() {
    var user = UserFactory.createSupportUserWithPartner(ANY_NAME, ANY_EMAIL, ANY_PASS, ANY_PARTNER);
    assertThat(user).isNotNull().isInstanceOf(User.class);
    assertThat(user.role()).isEqualTo(UserRole.SUPPORT);
    assertThat(user.partnerId()).contains(ANY_PARTNER);
  }

  @Test
  @DisplayName("createCustomerWithPartner creates a user with Customer with a Partner")
  void createCustomerWithPartnerCreatesAUserWithCustomerWithAPartner() {

    var user =
        UserFactory.createCustomerUserWithPartner(ANY_NAME, ANY_EMAIL, ANY_PASS, ANY_PARTNER);
    assertThat(user).isNotNull().isInstanceOf(User.class);
    assertThat(user.role()).isEqualTo(UserRole.CUSTOMER);
    assertThat(user.partnerId()).contains(ANY_PARTNER);
  }

  @Test
  @DisplayName("createCustomerWithoutPartner creates a user with Customer wiht no Partner")
  void createCustomerWithoutPartnerCreatesAUserWithCustomerWihtNoPartner() {

    var user = UserFactory.createCustomerUserWithoutPartner(ANY_NAME, ANY_EMAIL, ANY_PASS);
    assertThat(user).isNotNull().isInstanceOf(User.class);
    assertThat(user.role()).isEqualTo(UserRole.CUSTOMER);
    assertThat(user.partnerId()).isEmpty();
  }
}
