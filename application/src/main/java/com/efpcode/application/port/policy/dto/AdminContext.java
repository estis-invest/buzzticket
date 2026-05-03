package com.efpcode.application.port.policy.dto;

import com.efpcode.domain.partner.model.Partner;
import com.efpcode.domain.user.model.User;

public record AdminContext(User user, Partner partner) {}
