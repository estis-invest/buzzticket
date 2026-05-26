-- =====================
-- SAFE RESET
-- =====================

DELETE FROM ticket_assignee;
DELETE FROM staff_invitation;
DELETE FROM ticket;
DELETE FROM app_user;
DELETE FROM partner;

-- =====================
-- PARTNERS
-- =====================

INSERT INTO partner (
    partner_id,
    partner_name,
    partner_city,
    partner_country,
    partner_iso_code,
    partner_status,
    partner_created_at,
    partner_updated_at
) VALUES
      (
          '019e5f15-1021-783b-b123-1901331999a7',
          'Acme Corp',
          'Stockholm',
          'SWEDEN',
          'SWE',
          'ACTIVE',
          '2026-05-25 12:21:16.450192',
          '2026-05-25 12:21:16.450302'
      ),
      (
          '019e5f16-b180-70fe-8d2d-ed7fc292b031',
          'Beta Corp',
          'Stockholm',
          'SWEDEN',
          'SWE',
          'ACTIVE',
          '2026-05-25 12:23:03.296705',
          '2026-05-25 12:23:03.296711'
      ),
      (
          '019e5f18-16a1-7e90-8b9d-171de06b31af',
          'Zeta Corp',
          'Stockholm',
          'SWEDEN',
          'SWE',
          'ACTIVE',
          '2026-05-25 12:24:34.721901',
          '2026-05-25 12:24:34.721906'
      );

-- =====================
-- ADMIN USERS
-- =====================

INSERT INTO app_user (
    user_id,
    user_name,
    user_email,
    user_password,
    user_role,
    user_account_status,
    user_created_at,
    user_updated_at,
    partner_id
) VALUES
      (
          '019e5f15-1021-74a2-b806-8c491da223b6',
          'Admin User',
          'admin@acme.com',
          '$2a$12$ybtImSJ.2l3C4kv4045EBOYHSfmj5MmGEdGbQVt.eEpBTGIZIgRF6',
          'ADMIN',
          'ACTIVATED',
          '2026-05-25 12:21:16.694511',
          '2026-05-25 12:21:16.694607',
          '019e5f15-1021-783b-b123-1901331999a7'
      ),
      (
          '019e5f16-b180-7820-bec7-ca6fa82d1917',
          'Admin User2',
          'admin2@acme.com',
          '$2a$12$8i/EZ7z/Tneeig0BgjqiBugIdw0q0mjkaVNOvj1SG1zpi3qFsUMe6',
          'ADMIN',
          'ACTIVATED',
          '2026-05-25 12:23:03.553485',
          '2026-05-25 12:23:03.553489',
          '019e5f16-b180-70fe-8d2d-ed7fc292b031'
      ),
      (
          '019e5f18-16a1-7e5d-be1a-dcccd073fe84',
          'Admin User3',
          'admin3@acme.com',
          '$2a$12$hGBUF58nAwcm5gEx8ddVvOy.pTvGmVeAzKhuZimpUOsXOGNJLQCxm',
          'ADMIN',
          'ACTIVATED',
          '2026-05-25 12:24:34.978462',
          '2026-05-25 12:24:34.978469',
          '019e5f18-16a1-7e90-8b9d-171de06b31af'
      );