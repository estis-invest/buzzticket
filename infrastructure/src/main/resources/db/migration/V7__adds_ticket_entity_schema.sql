CREATE TABLE ticket
(
    ticket_id               UUID                        NOT NULL,
    ticket_slug             VARCHAR(64)                 NOT NULL,
    ticket_title            VARCHAR(50)                 NOT NULL,
    ticket_description      VARCHAR(1800)               NOT NULL,
    ticket_status           VARCHAR(255)                NOT NULL,
    ticket_priority         VARCHAR(255)                NOT NULL,
    ticket_created_at       TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    ticket_updated_at       TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    ticket_reported_by_id   UUID                        NOT NULL,
    ticket_owner_partner_id UUID                        NOT NULL,
    CONSTRAINT pk_ticket PRIMARY KEY (ticket_id)
);

CREATE TABLE ticket_assignee
(
    ticket_id UUID NOT NULL,
    user_id   UUID NOT NULL
);

ALTER TABLE ticket
    ADD CONSTRAINT uc_ticket_ticket_slug UNIQUE (ticket_slug);


ALTER TABLE ticket
    ADD CONSTRAINT fk_ticket_reported_by
        FOREIGN KEY (ticket_reported_by_id)
            REFERENCES app_user (user_id);

ALTER TABLE ticket
    ADD CONSTRAINT fk_ticket_owner_partner
        FOREIGN KEY (ticket_owner_partner_id)
            REFERENCES partner (partner_id);


ALTER TABLE ticket_assignee
    ADD CONSTRAINT pk_ticket_assignee
        PRIMARY KEY (ticket_id, user_id);

ALTER TABLE ticket_assignee
    ADD CONSTRAINT fk_ticket_assignee_on_ticket_entity FOREIGN KEY (ticket_id) REFERENCES ticket (ticket_id);

ALTER TABLE ticket_assignee
    ADD CONSTRAINT fk_ticket_assignee_user
        FOREIGN KEY (user_id) REFERENCES app_user (user_id);
