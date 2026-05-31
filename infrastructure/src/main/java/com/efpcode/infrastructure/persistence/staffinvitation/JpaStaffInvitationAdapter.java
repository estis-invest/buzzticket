package com.efpcode.infrastructure.persistence.staffinvitation;

import com.efpcode.domain.partner.model.PartnerId;
import com.efpcode.domain.staffinvitation.model.StaffInvitation;
import com.efpcode.domain.staffinvitation.model.StaffInvitationId;
import com.efpcode.domain.staffinvitation.model.StaffInvitationStatus;
import com.efpcode.domain.staffinvitation.model.StaffInvitationTokenHash;
import com.efpcode.domain.staffinvitation.port.StaffInvitationRepository;
import com.efpcode.domain.user.model.UserEmail;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class JpaStaffInvitationAdapter implements StaffInvitationRepository {
  private final SpringDataStaffInvitationRepository staffInvitationRepository;

  public JpaStaffInvitationAdapter(SpringDataStaffInvitationRepository staffInvitationRepository) {
    this.staffInvitationRepository = staffInvitationRepository;
  }

  @Override
  public Optional<StaffInvitation> findById(StaffInvitationId invitationId) {
    return staffInvitationRepository
        .findByInvitationId(invitationId.invitationId())
        .map(StaffInvitationMapper::toDomain);
  }

  @Override
  public Optional<StaffInvitation> findPendingByTokenHash(StaffInvitationTokenHash tokenHash) {
    return staffInvitationRepository
        .findByStatusAndInvitationTokenHash(StaffInvitationStatus.PENDING.name(), tokenHash.value())
        .map(StaffInvitationMapper::toDomain);
  }

  @Override
  public boolean existsPendingByEmailAndPartnerId(UserEmail inviteeEmail, PartnerId partnerId) {
    return staffInvitationRepository.existsByInviteeEmailAndPartnerIdAndStatus(
        inviteeEmail.email(), partnerId.partnerId(), StaffInvitationStatus.PENDING.name());
  }

  @Override
  public List<StaffInvitation> findByPartnerIdAndStatus(
      PartnerId partnerId, StaffInvitationStatus status) {
    return staffInvitationRepository
        .findByPartnerIdAndStatus(partnerId.partnerId(), status.name())
        .stream()
        .map(StaffInvitationMapper::toDomain)
        .toList();
  }

  @Override
  public void save(StaffInvitation staffInvitation) {
    StaffInvitationEntity entity = StaffInvitationMapper.toEntity(staffInvitation);
    staffInvitationRepository.save(entity);
  }
}
