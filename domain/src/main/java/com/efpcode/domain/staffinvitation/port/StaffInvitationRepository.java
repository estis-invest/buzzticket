package com.efpcode.domain.staffinvitation.port;

import com.efpcode.domain.partner.model.PartnerId;
import com.efpcode.domain.staffinvitation.StaffInvitation;
import com.efpcode.domain.staffinvitation.StaffInvitationId;
import com.efpcode.domain.staffinvitation.StaffInvitationStatus;
import com.efpcode.domain.staffinvitation.StaffInvitationTokenHash;
import com.efpcode.domain.user.model.UserEmail;
import java.util.List;
import java.util.Optional;

public interface StaffInvitationRepository {

  Optional<StaffInvitation> findById(StaffInvitationId invitationId);

  Optional<StaffInvitation> findPendingByTokenHash(StaffInvitationTokenHash tokenHash);

  boolean existsPendingByEmailAndPartnerId(UserEmail inviteeEmail, PartnerId partnerId);

  List<StaffInvitation> findByPartnerIdAndStatus(PartnerId partnerId, StaffInvitationStatus status);

  void save(StaffInvitation staffInvitation);
}
