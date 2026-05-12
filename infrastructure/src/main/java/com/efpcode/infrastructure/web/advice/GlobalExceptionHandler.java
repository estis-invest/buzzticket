package com.efpcode.infrastructure.web.advice;

import com.efpcode.application.usecase.auth.exceptions.LoginFailException;
import com.efpcode.application.usecase.partner.exceptions.InvalidPartnerCommandArgumentException;
import com.efpcode.application.usecase.partner.exceptions.PartnerAlreadyExistsException;
import com.efpcode.application.usecase.partner.exceptions.PartnerNotFoundException;
import com.efpcode.application.usecase.user.exceptions.IllegalStaffInvitationExpirationDateArgumentException;
import com.efpcode.application.usecase.user.exceptions.IllegalUserEmailDuplicatedException;
import com.efpcode.application.usecase.user.exceptions.UserApplicationException;
import com.efpcode.domain.common.exceptions.CommonDomainException;
import com.efpcode.domain.partner.exceptions.PartnerDomainException;
import com.efpcode.domain.staffinvitation.exceptions.StaffInvitationDomainException;
import com.efpcode.domain.user.exceptions.UserDomainException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ProblemDetail handleValidationErrors(MethodArgumentNotValidException ex) {
    ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

    problem.setTitle("Invalid request");
    problem.setDetail(
        ex.getBindingResult().getFieldErrors().stream()
            .map(err -> err.getField() + ": " + err.getDefaultMessage())
            .findFirst()
            .orElse("Request validation failed"));

    return problem;
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

  @ExceptionHandler(IllegalUserEmailDuplicatedException.class)
  public ProblemDetail handleDuplicateUserEmail(IllegalUserEmailDuplicatedException ex) {
    return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
  }

  @ExceptionHandler(IllegalStaffInvitationExpirationDateArgumentException.class)
  public ProblemDetail handleInvitationTtlViolation(
      IllegalStaffInvitationExpirationDateArgumentException ex) {
    return ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_CONTENT, ex.getMessage());
  }

  @ExceptionHandler(UserApplicationException.class)
  public ProblemDetail handleUserApplicationException(UserApplicationException ex) {
    return ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_CONTENT, ex.getMessage());
  }

  @ExceptionHandler(StaffInvitationDomainException.class)
  public ProblemDetail handleStaffInvitationDomain(StaffInvitationDomainException ex) {
    return ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_CONTENT, ex.getMessage());
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ProblemDetail handleDataIntegrityViolation(DataIntegrityViolationException ex) {
    return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, "Resource already exists");
  }
}
