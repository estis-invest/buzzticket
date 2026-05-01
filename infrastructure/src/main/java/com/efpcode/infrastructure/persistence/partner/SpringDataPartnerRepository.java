package com.efpcode.infrastructure.persistence.partner;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SpringDataPartnerRepository extends JpaRepository<PartnerEntity, UUID> {

  boolean existsByIgnoringCasePartnerNameAndPartnerStatusNot(
      String partnerName, String partnerStatus);

  boolean existsByPartnerIdAndPartnerStatusNot(UUID partnerId, String partnerStatus);

  List<PartnerEntity> findAllByPartnerStatusNot(String partnerStatus);

  Optional<PartnerEntity> findByPartnerIdAndPartnerStatusNot(UUID partnerId, String partnerStatus);

  Optional<PartnerEntity> findByPartnerId(UUID partnerId);

  @Query("select (count(p) > 0) from PartnerEntity p")
  boolean existsAny();
}
