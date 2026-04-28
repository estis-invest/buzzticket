package com.efpcode.application.usecase.bootstrap.dto;

import com.efpcode.domain.partner.model.Partner;
import com.efpcode.domain.user.model.User;

public record BootstrapResult(Partner partner, User user) {}
