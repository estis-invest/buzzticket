CREATE TABLE app_user
(
    user_id             UUID                        NOT NULL,
    user_name           VARCHAR(255)                NOT NULL,
    user_email          VARCHAR(255)                NOT NULL,
    user_password       VARCHAR(255)                NOT NULL,
    user_role           VARCHAR(255)                NOT NULL,
    user_account_status VARCHAR(255)                NOT NULL,
    user_created_at     TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    user_updated_at     TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    partner_id          UUID,
    CONSTRAINT pk_app_user PRIMARY KEY (user_id)
);

ALTER TABLE app_user
    ADD CONSTRAINT uc_app_user_user_email UNIQUE (user_email);

ALTER TABLE app_user
    ADD CONSTRAINT FK_APP_USER_ON_PARTNER FOREIGN KEY (partner_id) REFERENCES partner (partner_id);