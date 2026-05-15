package com.efpcode.application.usecase.user;

import com.efpcode.application.port.out.security.PasswordHasher;
import com.efpcode.application.usecase.user.dto.RegisterCustomerCommand;
import com.efpcode.application.usecase.user.exceptions.IllegalUserEmailDuplicatedException;
import com.efpcode.domain.common.model.PlainPassword;
import com.efpcode.domain.common.port.IdGenerator;
import com.efpcode.domain.user.model.*;
import com.efpcode.domain.user.port.UserRepository;

public class RegisterCustomerUseCase {

  private final IdGenerator<UserId> idGenerator;
  private final UserRepository userRepository;
  private final PasswordHasher passwordHasher;

  public RegisterCustomerUseCase(
      IdGenerator<UserId> idGenerator,
      UserRepository userRepository,
      PasswordHasher passwordHasher) {
    this.idGenerator = idGenerator;
    this.userRepository = userRepository;
    this.passwordHasher = passwordHasher;
  }

  public User execute(RegisterCustomerCommand command) {

    UserEmail customerEmail = new UserEmail(command.email());
    User customer;

    if (userRepository.existsByEmail(customerEmail)) {
      throw new IllegalUserEmailDuplicatedException("Duplicated email is not allowed");
    }

    PlainPassword plainPassword = new PlainPassword(command.password());
    UserPassword hashedPassword = passwordHasher.hash(plainPassword);
    UserName customerName = new UserName(command.name());
    UserId customerId = idGenerator.generate();

    customer =
        UserFactory.createCustomerUserWithoutPartner(
            customerId, customerName, customerEmail, hashedPassword);

    userRepository.save(customer);

    return customer;
  }
}
