package com.efpcode.infrastructure.web.advice;

import com.efpcode.application.usecase.partner.exceptions.PartnerAlreadyExistsException;
import com.efpcode.application.usecase.partner.exceptions.PartnerNotFoundException;
import com.efpcode.domain.partner.exceptions.PartnerDomainException;
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
}
