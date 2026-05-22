package com.efpcode.application.policy.staff.dto;

import com.efpcode.domain.partner.model.Partner;
import com.efpcode.domain.user.model.User;

public record StaffContext(User user, Partner partner) {}
