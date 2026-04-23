package com.efpcode.infrastructure.persistence.user;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataUserRepository extends JpaRepository<UserEntity, UUID> {

  List<UserEntity> findByPartner_PartnerIdAndUserCreatedAtBetween(
      UUID partnerPartnerId,
      Instant userCreatedAtAfter,
      Instant userCreatedAtBefore,
      Pageable pageable);

  List<UserEntity> findByPartner_PartnerId(UUID partnerPartnerId, Pageable pageable);

  Optional<UserEntity> findByUserId(UUID userId);

  Optional<UserEntity> findByUserEmail(String userEmail);

  boolean existsByUserEmailIgnoreCase(String userEmail);
}
