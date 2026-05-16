package com.efpcode.application.policy.user.dto;

import com.efpcode.domain.partner.model.Partner;
import com.efpcode.domain.user.model.User;
import java.util.Optional;

public record UserContext(User user, Optional<Partner> partner) {}
