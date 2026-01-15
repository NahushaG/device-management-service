package org.assessment.devicemanagement.exception;

import java.net.URI;
import java.time.Instant;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(NotFoundException.class)
  public ProblemDetail handleNotFound(NotFoundException ex) {
    ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
    pd.setTitle("Resource not found");
    pd.setDetail(ex.getMessage());
    pd.setType(URI.create("about:blank"));
    pd.setProperty("timestamp", Instant.now());
    pd.setProperty("errorCode", "NOT_FOUND");
    return pd;
  }

  @ExceptionHandler(DomainValidationException.class)
  public ProblemDetail handleDomain(DomainValidationException ex) {
    ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);
    pd.setTitle("Domain validation failed");
    pd.setDetail(ex.getMessage());
    pd.setType(URI.create("about:blank"));
    pd.setProperty("timestamp", Instant.now());
    pd.setProperty("errorCode", "DOMAIN_VALIDATION");
    return pd;
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ProblemDetail handleBeanValidation(MethodArgumentNotValidException ex) {
    ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
    pd.setTitle("Request validation failed");
    pd.setDetail("One or more fields are invalid.");
    pd.setType(URI.create("about:blank"));
    pd.setProperty("timestamp", Instant.now());
    pd.setProperty("errorCode", "REQUEST_VALIDATION");

    List<ValidationError> errors = ex.getBindingResult().getFieldErrors().stream()
        .map(this::toValidationError)
        .toList();

    pd.setProperty("errors", errors);
    return pd;
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ProblemDetail handleNotReadable(HttpMessageNotReadableException ex) {
    // covers invalid JSON, invalid enum values, malformed bodies
    ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
    pd.setTitle("Malformed request");
    pd.setDetail("Request body is invalid or could not be parsed.");
    pd.setType(URI.create("about:blank"));
    pd.setProperty("timestamp", Instant.now());
    pd.setProperty("errorCode", "MALFORMED_REQUEST");
    return pd;
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ProblemDetail handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
    // covers invalid UUID in path variables etc.
    ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
    pd.setTitle("Invalid parameter");
    pd.setDetail("Parameter '%s' has an invalid value.".formatted(ex.getName()));
    pd.setType(URI.create("about:blank"));
    pd.setProperty("timestamp", Instant.now());
    pd.setProperty("errorCode", "INVALID_PARAMETER");
    return pd;
  }

  @ExceptionHandler(ErrorResponseException.class)
  public ProblemDetail handleSpringErrorResponse(ErrorResponseException ex) {
    // fallback for other Spring-generated ProblemDetail exceptions
    return ex.getBody();
  }

  private ValidationError toValidationError(FieldError fe) {
    return new ValidationError(fe.getField(), fe.getDefaultMessage());
  }

  public record ValidationError(String field, String message) {}

}
