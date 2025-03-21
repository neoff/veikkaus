package fi.veikkaus.demo.controller;

import fi.veikkaus.demo.exception.BadRequestException;
import fi.veikkaus.demo.exception.InsufficientException;
import fi.veikkaus.demo.exception.NotFoundException;
import fi.veikkaus.demo.exception.UserExistException;
import fi.veikkaus.demo.pojo.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import reactor.core.publisher.Mono;

@Slf4j
@ControllerAdvice
public class AppExceptionHandler {
  
  @ExceptionHandler(NotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public Mono<ResponseEntity<ErrorResponse>> handleNotFoundException(NotFoundException e) {
    log.error("❌ Not found error: {}", e.getMessage());
    return buildResponse(HttpStatus.NOT_FOUND, e.getMessage());
  }
  
  @ExceptionHandler(BadRequestException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Mono<ResponseEntity<ErrorResponse>> handleBadRequestException(BadRequestException e) {
    log.error("❌ Bad request error: {}", e.getMessage());
    return buildResponse(HttpStatus.BAD_REQUEST, e.getMessage());
  }
  
  @ExceptionHandler(UserExistException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  public Mono<ResponseEntity<ErrorResponse>> handleConflictException(UserExistException e) {
    log.error("❌ Conflict error: {}", e.getMessage());
    return buildResponse(HttpStatus.CONFLICT, e.getMessage());
  }
  
  @ExceptionHandler(InsufficientException.class)
  @ResponseStatus(HttpStatus.PAYMENT_REQUIRED)
  public Mono<ResponseEntity<ErrorResponse>> handleInsufficientException(InsufficientException e) {
    log.error("❌ Insufficient amount error: {}", e.getMessage());
    return buildResponse(HttpStatus.PAYMENT_REQUIRED, e.getMessage());
  }
  
  @ExceptionHandler(Exception.class)
  public Mono<ResponseEntity<ErrorResponse>> handleInternalServerErrorException(Exception e) {
    log.error("🔥 Internal server error", e);
    return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Server error");
  }
  
  private Mono<ResponseEntity<ErrorResponse>> buildResponse(HttpStatus code,
                                                            String message) {
    ErrorResponse errorResponse = new ErrorResponse()
                                      .code(code.value())
                                      .error(message);
    return Mono.just(ResponseEntity
                         .status(code)
                         .body(errorResponse));
  }
}
