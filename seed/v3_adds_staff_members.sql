-- =====================
-- STAFF + ADDITIONAL ADMINS
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
          '019e5f97-6add-7e93-b19e-46fe35732bfb',
          'Support User',
          'support@acme.com',
          '$2a$12$h1rjXFJgCcidl0v8SydyUu6DIKUKyRlS/Cvhm9l5XokvB8gQ6ZqFS',
          'SUPPORT',
          'ACTIVATED',
          '2026-05-25 14:43:39.359474',
          '2026-05-25 14:43:39.359479',
          '019e5f15-1021-783b-b123-1901331999a7'
      ),
      (
          '019e5f98-0bfe-7ff7-a06f-f130579a1e8d',
          'Support User',
          'support2@acme.com',
          '$2a$12$NQsxQsnwxK5KNRKz29DRcuFbknCyTg4Gel.XPhoUlDBSHJu6LMd2u',
          'SUPPORT',
          'ACTIVATED',
          '2026-05-25 14:44:20.606729',
          '2026-05-25 14:44:20.606733',
          '019e5f15-1021-783b-b123-1901331999a7'
      ),
      (
          '019e5f99-8df1-7603-8153-f3b779230a37',
          'Support User',
          'register.admin@acme.com',
          '$2a$12$CYSXzHn0eLAa5IJPIyv6S.0KA98U9obcCPvt7pLVy7A98.d2g97yG',
          'ADMIN',
          'ACTIVATED',
          '2026-05-25 14:45:59.409606',
          '2026-05-25 14:45:59.409611',
          '019e5f15-1021-783b-b123-1901331999a7'
      ),
      (
          '019e5f9b-8d37-7c21-b617-8af824cd5889',
          'Support User',
          'register.admin@beta.com',
          '$2a$12$.BIxD.I5tnhOsLN1eTeqo.DP3L6xJrQ4Aijs6I/kxAkLfd72aSk3i',
          'ADMIN',
          'ACTIVATED',
          '2026-05-25 14:48:10.296019',
          '2026-05-25 14:48:10.296023',
          '019e5f16-b180-70fe-8d2d-ed7fc292b031'
      ),
      (
          '019e5f9c-564e-7526-afe5-9c3bff75bd19',
          'Support User',
          'support@beta.com',
          '$2a$12$nTI3X8S9o/UZImFh8Tu2AurqNs9pQrAujtZnFHywtABoDYap8A.ES',
          'SUPPORT',
          'ACTIVATED',
          '2026-05-25 14:49:01.774289',
          '2026-05-25 14:49:01.774293',
          '019e5f16-b180-70fe-8d2d-ed7fc292b031'
      ),
      (
          '019e5f9e-2d5b-7ff0-8170-91ac4c74dd2e',
          'Support User',
          'support@zeta.com',
          '$2a$12$E6qsbag3sxF/D4RyWNZGbOupDjy3yUz8Jnn8.0UCrQ2h9TnardZp.',
          'SUPPORT',
          'ACTIVATED',
          '2026-05-25 14:51:02.363709',
          '2026-05-25 14:51:02.363713',
          '019e5f18-16a1-7e90-8b9d-171de06b31af'
      );