package com.efpcode.infrastructure.persistence.staffinvitation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataStaffInvitationRepository
    extends JpaRepository<StaffInvitationEntity, UUID> {

  Optional<StaffInvitationEntity> findByInvitationId(UUID invitationId);

  Optional<StaffInvitationEntity> findByStatusAndInvitationTokenHash(
      String status, String invitationTokenHash);

  boolean existsByInviteeEmailAndPartnerIdAndStatus(String inviteeEmail, UUID partnerId, String status);

  List<StaffInvitationEntity> findByPartnerIdAndStatus(UUID partnerId, String status);
}
