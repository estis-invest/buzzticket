package com.efpcode.infrastructure.web.advice;

import com.efpcode.application.usecase.partner.exceptions.InvalidPartnerCommandArgumentException;
import com.efpcode.application.usecase.partner.exceptions.PartnerAlreadyExistsException;
import com.efpcode.application.usecase.partner.exceptions.PartnerNotFoundException;
import com.efpcode.domain.partner.exceptions.PartnerDomainException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
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
  public ResponseEntity<Map<String, Object>> handleDomainValidation(
      InvalidPartnerCommandArgumentException ex) {
    Map<String, Object> body = new HashMap<>();
    body.put("timestamp", LocalDateTime.now());
    body.put("status", 422);
    body.put("error", "Unprocessable Content");
    body.put("message", ex.getMessage()); // This will say "Name is required..."

    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT).body(body);
  }
}
