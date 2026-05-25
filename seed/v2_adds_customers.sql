-- =====================
-- CUSTOMERS
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
          '019e5f4f-fc21-7f72-a2c9-18bc231dbd84',
          'customer',
          'customer@acme.com',
          '$2a$12$ptTXbEo7pO56e2aqtrLoCOeEHmBsh9dbZ2yZAbHfp28jenHVjElZa',
          'CUSTOMER',
          'ACTIVATED',
          '2026-05-25 13:25:37.954662',
          '2026-05-25 13:25:37.954761',
          NULL
      ),
      (
          '019e5f50-8665-7fea-93c9-f9799edfe92f',
          'customer',
          'customer2@acme.com',
          '$2a$12$LJbv3SFHQhMBs.aHr/mDdObzl7EQoOqui/AGaERHEAb3jyLadzWmy',
          'CUSTOMER',
          'ACTIVATED',
          '2026-05-25 13:26:13.349196',
          '2026-05-25 13:26:13.349201',
          NULL
      ),
      (
          '019e5f50-dfd2-7782-82c5-7314d00428c2',
          'customer',
          'customer3@acme.com',
          '$2a$12$rUuszFLVb.k6tMDuqHl6iOXdqoO9bxyzYDmfcneE/v/okBts.kKdu',
          'CUSTOMER',
          'ACTIVATED',
          '2026-05-25 13:26:36.242285',
          '2026-05-25 13:26:36.242289',
          NULL
      ),
      (
          '019e5f51-ba0e-7502-8b41-90041817a7a8',
          'customer',
          'customer1@beta.com',
          '$2a$12$klGcIVVb1SXior1xTDlzp.h4dCxYUIq5t3EY8g3daVLfQaMa4qSsu',
          'CUSTOMER',
          'ACTIVATED',
          '2026-05-25 13:27:32.110407',
          '2026-05-25 13:27:32.110411',
          NULL
      ),
      (
          '019e5f51-f3c4-7a44-bc05-a3ade4a15c77',
          'customer',
          'customer2@beta.com',
          '$2a$12$j2msgmVKhLXBtxa/YmC52.2nwd9QIWG2H0DPY6kRFZJo2IJoleKUO',
          'CUSTOMER',
          'ACTIVATED',
          '2026-05-25 13:27:46.885002',
          '2026-05-25 13:27:46.885006',
          NULL
      ),
      (
          '019e5f53-17e7-7945-989a-2a7a6e573efc',
          'customer',
          'customer@zeta.com',
          '$2a$12$SEXKFUiiiCsVmHz3QK6tc.4hzsSbuZ0VkL4.iIBrWPE99BBQnrSSS',
          'CUSTOMER',
          'ACTIVATED',
          '2026-05-25 13:29:01.672013',
          '2026-05-25 13:29:01.672017',
          NULL
      );