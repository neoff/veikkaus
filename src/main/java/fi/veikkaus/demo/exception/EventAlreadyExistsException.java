package fi.veikkaus.demo.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class EventAlreadyExistsException extends RuntimeException {
  public EventAlreadyExistsException(String s) {
    super(s);
  }
}
