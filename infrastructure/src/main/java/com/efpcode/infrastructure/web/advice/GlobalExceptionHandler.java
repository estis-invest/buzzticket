package com.efpcode.infrastructure.web.advice;

import com.efpcode.application.usecase.auth.exceptions.LoginFailException;
import com.efpcode.application.usecase.partner.exceptions.InvalidPartnerCommandArgumentException;
import com.efpcode.application.usecase.partner.exceptions.PartnerAlreadyExistsException;
import com.efpcode.application.usecase.partner.exceptions.PartnerNotFoundException;
import com.efpcode.domain.common.exceptions.CommonDomainException;
import com.efpcode.domain.partner.exceptions.PartnerDomainException;
import com.efpcode.domain.user.exceptions.UserDomainException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(PartnerAlreadyExistsException.class)
  public ProblemDetail handleAlreadyExists(PartnerAlreadyExistsException ex) {
    return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
  }

  @ExceptionHandler(PartnerNotFoundException.class)
  public ProblemDetail handleApplicationError(PartnerNotFoundException ex) {
    return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
  }

  @ExceptionHandler(PartnerDomainException.class)
  public ProblemDetail handleDomainError(PartnerDomainException ex) {
    return ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_CONTENT, ex.getMessage());
  }

  @ExceptionHandler(InvalidPartnerCommandArgumentException.class)
  public ProblemDetail handleDomainValidation(InvalidPartnerCommandArgumentException ex) {
    return ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_CONTENT, ex.getMessage());
  }

  @ExceptionHandler(UserDomainException.class)
  public ProblemDetail handleUserDomain(UserDomainException ex) {
    return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
  }

  @ExceptionHandler(CommonDomainException.class)
  public ProblemDetail handleCommonDomain(CommonDomainException ex) {
    return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
  }

  @ExceptionHandler(LoginFailException.class)
  public ProblemDetail handleLoginFailure(LoginFailException ex) {
    return ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, ex.getMessage());
  }
}
