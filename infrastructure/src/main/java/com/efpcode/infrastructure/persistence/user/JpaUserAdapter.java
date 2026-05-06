package com.efpcode.infrastructure.persistence.user;

import com.efpcode.domain.partner.model.PartnerId;
import com.efpcode.domain.partner.model.PartnerStatus;
import com.efpcode.domain.user.model.*;
import com.efpcode.domain.user.port.UserRepository;
import com.efpcode.infrastructure.persistence.exceptions.MissingReferenceEntityException;
import com.efpcode.infrastructure.persistence.partner.PartnerEntity;
import com.efpcode.infrastructure.persistence.partner.SpringDataPartnerRepository;
import java.util.List;
import java.util.Optional;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class JpaUserAdapter implements UserRepository {
  private final SpringDataUserRepository userRepository;
  private final SpringDataPartnerRepository partnerRepository;

  public JpaUserAdapter(
      SpringDataUserRepository userRepository, SpringDataPartnerRepository partnerRepository) {
    this.userRepository = userRepository;
    this.partnerRepository = partnerRepository;
  }

  @Override
  public Optional<User> findUserById(UserId id) {
    return userRepository.findByUserId(id.id()).map(UserMapper::toDomain);
  }

  @Override
  public Optional<User> findUserByEmail(UserEmail email) {
    return userRepository.findByUserEmailIgnoreCase(email.email()).map(UserMapper::toDomain);
  }

  @Override
  public List<User> findByUserCreatedAtRange(
      PartnerId partnerId, UserCreatedAt startDate, UserCreatedAt endDate) {
    Sort sort = getSort();

    return userRepository
        .findByPartner_PartnerIdAndUserCreatedAtBetween(
            partnerId.partnerId(), startDate.time(), endDate.time(), sort)
        .stream()
        .map(UserMapper::toDomain)
        .toList();
  }

  @Override
  public List<User> findAll() {
    Sort sort = getSort();
    return userRepository.findAll(sort).stream().map(UserMapper::toDomain).toList();
  }

  @Override
  public List<User> findByPartnerId(PartnerId id) {
    Sort sort = getSort();
    return userRepository.findByPartner_PartnerId(id.partnerId(), sort).stream()
        .map(UserMapper::toDomain)
        .toList();
  }

  @Override
  public boolean existsByEmail(UserEmail email) {
    return userRepository.existsByUserEmailIgnoreCase(email.email());
  }

  @Override
  @Transactional
  public void save(User user) {
    PartnerEntity partnerEntity =
        user.partnerId()
            .map(PartnerId::partnerId)
            .map(
                id ->
                    partnerRepository
                        .findByPartnerIdAndPartnerStatusNot(id, PartnerStatus.DELETED.name())
                        .orElseThrow(
                            () -> new MissingReferenceEntityException("Partner", id, null)))
            .orElse(null);

    UserEntity entity = UserMapper.toEntity(user, partnerEntity);
    userRepository.save(entity);
  }

  @Override
  public void deleteByUserId(UserId id) {
    userRepository.deleteById(id.id());
  }

  @Override
  public boolean existsAdminForPartner(PartnerId partnerId) {
    return userRepository.existsByPartnerPartnerIdAndUserRole(
        partnerId.partnerId(), UserRole.ADMIN.name());
  }

  // Helper methods
  private static @NonNull Sort getSort() {
    return Sort.by("userCreatedAt").descending();
  }
}
