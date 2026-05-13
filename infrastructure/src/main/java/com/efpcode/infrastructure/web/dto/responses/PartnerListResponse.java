package com.efpcode.infrastructure.web.dto.responses;

import java.util.List;

public record PartnerListResponse(List<PartnerResponse> partners, int total) {}
