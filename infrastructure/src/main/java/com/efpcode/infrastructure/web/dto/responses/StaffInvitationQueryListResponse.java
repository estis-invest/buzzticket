package com.efpcode.infrastructure.web.dto.responses;

import java.util.List;

public record StaffInvitationQueryListResponse(
    List<StaffInvitationQueryResponse> invitations, int total) {}
