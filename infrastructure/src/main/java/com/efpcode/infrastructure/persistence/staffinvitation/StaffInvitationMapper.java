package com.efpcode.infrastructure.persistence.staffinvitation;

import com.efpcode.domain.partner.model.PartnerId;
import com.efpcode.domain.staffinvitation.model.*;
import com.efpcode.domain.user.model.UserEmail;
import com.efpcode.domain.user.model.UserId;
import com.efpcode.domain.user.model.UserRole;
import java.util.Optional;

public class StaffInvitationMapper {

  private StaffInvitationMapper() {}

  public static StaffInvitationEntity toEntity(StaffInvitation domain) {
    StaffInvitationEntity entity = new StaffInvitationEntity();

    entity.setInvitationId(domain.invitationId().invitationId());
    entity.setInvitedByUserId(domain.invitedByUserId().id());
    entity.setInviteeEmail(domain.inviteeEmail().email());
    entity.setRole(domain.role().name());
    entity.setPartnerId(domain.partnerId().partnerId());
    entity.setInvitationTokenHash(domain.invitationTokenHash().value());
    entity.setStatus(domain.invitationStatus().name());
    entity.setInvitationCreatedAt(domain.invitationCreatedAt().time());
    entity.setInvitationExpiresAt(domain.invitationExpiresAt().time());
    entity.setInvitationUpdatedAt(domain.invitationUpdatedAt().time());
    entity.setAcceptedAt(domain.acceptedAt().map(StaffInvitationAcceptedAt::time).orElse(null));
    entity.setAcceptedUserId(domain.acceptedUserId().map(UserId::id).orElse(null));

    return entity;
  }

  public static StaffInvitation toDomain(StaffInvitationEntity entity) {
    Optional<StaffInvitationAcceptedAt> acceptedAt =
        entity.getAcceptedAt() == null
            ? Optional.empty()
            : Optional.of(new StaffInvitationAcceptedAt(entity.getAcceptedAt()));
    Optional<UserId> acceptedUserId =
        entity.getAcceptedUserId() == null
            ? Optional.empty()
            : Optional.of(new UserId(entity.getAcceptedUserId()));

    return new StaffInvitation(
        new StaffInvitationId(entity.getInvitationId()),
        new UserId(entity.getInvitedByUserId()),
        new UserEmail(entity.getInviteeEmail()),
        UserRole.valueOf(entity.getRole()),
        new PartnerId(entity.getPartnerId()),
        new StaffInvitationTokenHash(entity.getInvitationTokenHash()),
        StaffInvitationStatus.valueOf(entity.getStatus()),
        new StaffInvitationCreatedAt(entity.getInvitationCreatedAt()),
        new StaffInvitationExpiresAt(entity.getInvitationExpiresAt()),
        new StaffInvitationUpdatedAt(entity.getInvitationUpdatedAt()),
        acceptedAt,
        acceptedUserId);
  }
}
