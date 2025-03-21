package fi.veikkaus.demo.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class NotFoundException extends RuntimeException {
  public NotFoundException(String s) {
    super(s);
  }
}
