package com.efpcode.domain.user.model;

import com.efpcode.domain.partner.model.PartnerId;
import java.util.Optional;

public class UserFactory {
  private UserFactory() {}

  public static User createAdminUserWithPartner(
      UserName name, UserEmail email, UserPassword password, PartnerId partnerId) {
    return createUser(name, email, password, UserRole.ADMIN, Optional.of(partnerId));
  }

  public static User createSupportUserWithPartner(
      UserName name, UserEmail email, UserPassword password, PartnerId partnerId) {
    return createUser(name, email, password, UserRole.SUPPORT, Optional.of(partnerId));
  }

  public static User createCustomerUserWithPartner(
      UserName name, UserEmail email, UserPassword password, PartnerId partnerId) {
    return createUser(name, email, password, UserRole.CUSTOMER, Optional.of(partnerId));
  }

  public static User createCustomerUserWithoutPartner(
      UserName name, UserEmail email, UserPassword password) {
    return createUser(name, email, password, UserRole.CUSTOMER, Optional.empty());
  }

  private static User createUser(
      UserName name,
      UserEmail email,
      UserPassword password,
      UserRole role,
      Optional<PartnerId> partnerId) {
    return new User(
        UserId.generate(),
        name,
        email,
        password,
        role,
        UserAccountStatus.ACTIVATED,
        UserCreatedAt.createNow(),
        UserUpdateAt.createNow(),
        partnerId);
  }
}
