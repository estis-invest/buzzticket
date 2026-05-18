package com.efpcode.infrastructure.web.dto.responses;

import java.util.List;

public record UserListResponse(List<UserResponse> users, int total) {}
