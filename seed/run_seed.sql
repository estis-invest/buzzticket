BEGIN;

--  SAFETY GUARD
DO $$
    BEGIN
        IF current_database() <> 'buzzticket' THEN
            RAISE EXCEPTION
                'Seed aborted: connected to unexpected database: %',
                current_database();
        END IF;
    END $$;

--  RESET (only here, once)
DELETE FROM ticket_assignee;
DELETE FROM staff_invitation;
DELETE FROM ticket;
DELETE FROM app_user;
DELETE FROM partner;

--  EXECUTION ORDER
\i seed/v1_initial_setup.sql
\i seed/v2_adds_customers.sql
\i seed/v3_adds_staff_members.sql
\i seed/v4_adds_tickets_pending_status.sql

COMMIT;