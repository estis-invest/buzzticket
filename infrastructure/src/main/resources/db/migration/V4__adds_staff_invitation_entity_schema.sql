CREATE TABLE staff_invitation
(
    staff_invitation_id                 UUID                        NOT NULL,
    staff_invitation_invited_by_user_id UUID                        NOT NULL,
    staff_invitation_email              VARCHAR(255)                NOT NULL,
    staff_invitation_role               VARCHAR(255)                NOT NULL,
    staff_invitation_partner_id         UUID                        NOT NULL,
    staff_invitation_token_hash         VARCHAR(255)                NOT NULL,
    staff_invitation_status             VARCHAR(255)                NOT NULL,
    staff_invitation_created_at         TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    staff_invitation_expires_at         TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    staff_invitation_updated_at         TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    staff_invitation_accepted_at        TIMESTAMP WITHOUT TIME ZONE,
    staff_invitation_accepted_user_id   UUID,
    CONSTRAINT pk_staff_invitation PRIMARY KEY (staff_invitation_id)
);

ALTER TABLE staff_invitation
    ADD CONSTRAINT uc_staff_invitation_staff_invitation_token_hash UNIQUE (staff_invitation_token_hash);