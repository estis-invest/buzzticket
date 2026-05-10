CREATE UNIQUE INDEX uq_staff_invitation_pending_email_partner
    ON staff_invitation (staff_invitation_email, staff_invitation_partner_id)
    WHERE staff_invitation_status = 'PENDING';

CREATE INDEX idx_staff_invitation_partner_status
    ON staff_invitation (staff_invitation_partner_id, staff_invitation_status);

CREATE INDEX idx_staff_invitation_invited_by_user
    ON staff_invitation (staff_invitation_invited_by_user_id);

CREATE INDEX idx_staff_invitation_accepted_user
    ON staff_invitation (staff_invitation_accepted_user_id);
