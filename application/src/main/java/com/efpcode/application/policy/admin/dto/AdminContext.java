package com.efpcode.application.policy.admin.dto;

import com.efpcode.domain.partner.model.Partner;
import com.efpcode.domain.user.model.User;

public record AdminContext(User admin, Partner partner) {}
