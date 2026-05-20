
CREATE INDEX idx_ticket_owner_partner_status
    ON ticket (ticket_owner_partner_id, ticket_status);

CREATE INDEX idx_ticket_reported_by
    ON ticket (ticket_reported_by_id);

CREATE INDEX idx_ticket_assignee_user
    ON ticket_assignee (user_id);
