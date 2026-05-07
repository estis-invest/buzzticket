package com.efpcode.application.port.in.user;

import com.efpcode.application.context.RequestContext;
import com.efpcode.application.usecase.user.dto.RegisterStaffCommand;
import com.efpcode.domain.user.model.User;

public interface StaffRegistrationCommands {
    User register(RequestContext requestContext, RegisterStaffCommand command);
}
