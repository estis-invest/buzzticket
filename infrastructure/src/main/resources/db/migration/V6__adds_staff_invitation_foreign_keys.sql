ALTER TABLE staff_invitation
    ADD CONSTRAINT fk_staff_invitation_partner
        FOREIGN KEY (staff_invitation_partner_id)
            REFERENCES partner (partner_id)
            ON DELETE RESTRICT;

ALTER TABLE staff_invitation
    ADD CONSTRAINT fk_staff_invitation_invited_by_user
        FOREIGN KEY (staff_invitation_invited_by_user_id)
            REFERENCES app_user (user_id)
            ON DELETE RESTRICT;

ALTER TABLE staff_invitation
    ADD CONSTRAINT fk_staff_invitation_accepted_user
        FOREIGN KEY (staff_invitation_accepted_user_id)
            REFERENCES app_user (user_id)
            ON DELETE RESTRICT;
