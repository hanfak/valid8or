package testinfrastructure;

import com.github.hanfak.valid8or.implmentation.ExceptionAndInput;

import java.util.LinkedList;
import java.util.Queue;

import static java.lang.String.format;

public class StubLogger {

  private static final String LOG_TEMPLATE = "For input '%s', was not valid because: '%s'";
  private final Queue<LogEvent> logEvent = new LinkedList<>();

  public void log(ExceptionAndInput<? extends RuntimeException, Integer> exceptionAndInput) {
    logEvent.clear();
    String message = format(LOG_TEMPLATE, exceptionAndInput.getInput(), exceptionAndInput.getMessage());

    logEvent.add(new LogEvent(message, exceptionAndInput.getException()));
  }

  public String lastLogEventMessage() {
    return logEvent.element().getMessage();
  }

  public Throwable lastLogEventException() {
    return logEvent.element().getException();
  }

  private static class LogEvent {

    private final String message;
    private final RuntimeException exception;

    private LogEvent(String message, RuntimeException exception) {
      this.message = message;
      this.exception = exception;
    }

    String getMessage() {
      return message;
    }

    Throwable getException() {
      return exception;
    }
  }
}
