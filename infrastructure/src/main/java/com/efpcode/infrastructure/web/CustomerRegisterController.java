package com.efpcode.infrastructure.web;

import com.efpcode.application.port.in.user.CustomerRegistrationCommands;
import com.efpcode.application.usecase.user.dto.RegisterCustomerCommand;
import com.efpcode.domain.user.model.User;
import com.efpcode.infrastructure.web.dto.requests.CustomerRegistrationRequest;
import com.efpcode.infrastructure.web.dto.responses.UserResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public/signup")
class CustomerRegisterController {

  private final CustomerRegistrationCommands customerRegistrationCommands;

  public CustomerRegisterController(CustomerRegistrationCommands customerRegistrationCommands) {
    this.customerRegistrationCommands = customerRegistrationCommands;
  }

  @PostMapping
  public ResponseEntity<UserResponse> customerSignUp(
      @Valid @RequestBody CustomerRegistrationRequest request) {
    var command = new RegisterCustomerCommand(request.name(), request.email(), request.password());

    User customer = customerRegistrationCommands.register(command);

    return ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.fromDomain(customer));
  }
}
