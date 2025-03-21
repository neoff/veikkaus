package fi.veikkaus.demo.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class InsufficientException extends RuntimeException {
  public InsufficientException(String insufficientBalance) {
    super(insufficientBalance);
  }
}
