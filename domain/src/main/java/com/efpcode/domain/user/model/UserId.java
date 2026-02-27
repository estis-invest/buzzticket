package com.efpcode.domain.user.model;

import java.util.UUID;

public record UserId(UUID id, String name, String email) {}
