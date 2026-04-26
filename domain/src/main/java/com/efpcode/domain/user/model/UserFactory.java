package com.efpcode.domain.user.model;

import com.efpcode.domain.partner.model.PartnerId;
import java.util.Optional;

public class UserFactory {
  private UserFactory() {}

  public static User createAdminUserWithPartner(
      UserId id, UserName name, UserEmail email, UserPassword password, PartnerId partnerId) {
    return createUser(id, name, email, password, UserRole.ADMIN, Optional.of(partnerId));
  }

  public static User createSupportUserWithPartner(
      UserId id, UserName name, UserEmail email, UserPassword password, PartnerId partnerId) {
    return createUser(id, name, email, password, UserRole.SUPPORT, Optional.of(partnerId));
  }

  public static User createCustomerUserWithPartner(
      UserId id, UserName name, UserEmail email, UserPassword password, PartnerId partnerId) {
    return createUser(id, name, email, password, UserRole.CUSTOMER, Optional.of(partnerId));
  }

  public static User createCustomerUserWithoutPartner(
      UserId id, UserName name, UserEmail email, UserPassword password) {
    return createUser(id, name, email, password, UserRole.CUSTOMER, Optional.empty());
  }

  private static User createUser(
      UserId id,
      UserName name,
      UserEmail email,
      UserPassword password,
      UserRole role,
      Optional<PartnerId> partnerId) {
    return new User(
        id,
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
