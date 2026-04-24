package com.efpcode.infrastructure.persistence.user;

import com.efpcode.domain.partner.model.PartnerId;
import com.efpcode.domain.user.model.*;
import com.efpcode.infrastructure.persistence.partner.PartnerEntity;
import java.util.Optional;

public class UserMapper {
  private UserMapper() {}

  public static UserEntity toEntity(User domain, PartnerEntity partnerEntity) {
    UserEntity entity = new UserEntity();

    entity.setUserId(domain.id().id());
    entity.setUserName(domain.name().name());
    entity.setUserEmail(domain.email().email());
    entity.setUserPassword(domain.password().hashedPassword());
    entity.setUserRole(domain.role().name());
    entity.setUserAccountStatus(domain.status().name());
    entity.setUserCreatedAt(domain.userCreatedAt().time());
    entity.setUserUpdatedAt(domain.userUpdateAt().updatedAt());
    entity.setPartner(partnerEntity);

    return entity;
  }

  public static User toDomain(UserEntity entity) {
    return new User(
        new UserId(entity.getUserId()),
        new UserName(entity.getUserName()),
        new UserEmail(entity.getUserEmail()),
        new UserPassword(entity.getUserPassword()),
        UserRole.valueOf(entity.getUserRole()),
        UserAccountStatus.valueOf(entity.getUserAccountStatus()),
        new UserCreatedAt(entity.getUserCreatedAt()),
        new UserUpdateAt(entity.getUserUpdatedAt()),
        entity.getPartner() == null
            ? Optional.empty()
            : Optional.of(new PartnerId(entity.getPartner().getPartnerId())));
  }
}
