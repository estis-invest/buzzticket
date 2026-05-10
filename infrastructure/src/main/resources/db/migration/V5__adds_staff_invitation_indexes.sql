
-- ============================================================
-- Uniqueness & lookup constraints
-- ============================================================

-- Invitation token must be globally unique and fast to resolve
CREATE UNIQUE INDEX uq_staff_invitation_token_hash
    ON staff_invitation (staff_invitation_token_hash);

-- Only one PENDING invitation per (email, partner)
CREATE UNIQUE INDEX uq_staff_invitation_pending_email_partner
    ON staff_invitation (staff_invitation_email, staff_invitation_partner_id)
    WHERE staff_invitation_status = 'PENDING';

-- ============================================================
-- Foreign key indexes (PERFORMANCE CRITICAL)
-- ============================================================

CREATE INDEX idx_staff_invitation_partner
    ON staff_invitation (staff_invitation_partner_id);

CREATE INDEX idx_staff_invitation_invited_by_user
    ON staff_invitation (staff_invitation_invited_by_user_id);

CREATE INDEX idx_staff_invitation_accepted_user
    ON staff_invitation (staff_invitation_accepted_user_id);
