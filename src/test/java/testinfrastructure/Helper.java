package testinfrastructure;

import com.github.hanfak.valid8or.implmentation.domain.ExceptionAndInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

import static java.lang.String.format;

public class Helper {
  private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  public static void log(ExceptionAndInput<? extends RuntimeException, Integer> exceptionAndInput) {
    String message = format("For input '%s', was not valid because: '%s'", exceptionAndInput.getInput(), exceptionAndInput.getMessage());
    logger.error(message, exceptionAndInput.getException());
  }
}
