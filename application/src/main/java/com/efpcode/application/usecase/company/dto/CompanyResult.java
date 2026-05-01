package com.efpcode.application.usecase.company.dto;

import com.efpcode.domain.partner.model.Partner;
import com.efpcode.domain.user.model.User;

public record CompanyResult(Partner partner, User user) {}
