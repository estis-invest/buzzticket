CREATE TABLE partner
(
    partner_id         UUID                        NOT NULL,
    partner_name       VARCHAR(255)                NOT NULL,
    partner_city       VARCHAR(255)                NOT NULL,
    partner_country    VARCHAR(255)                NOT NULL,
    partner_iso_code   VARCHAR(3)                  NOT NULL,
    partner_status     VARCHAR(255)                NOT NULL,
    partner_created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    partner_updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_partner PRIMARY KEY (partner_id)
);