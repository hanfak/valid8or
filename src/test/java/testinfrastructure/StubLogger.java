package testinfrastructure;

import com.github.hanfak.valid8or.implmentation.ExceptionAndInput;

import java.util.LinkedList;
import java.util.Queue;

import static java.lang.String.format;

public class StubLogger {

  private static final String LOG_TEMPLATE = "For input '%s', was not valid because: '%s'";
  private final Queue<LogEvent> logEvents = new LinkedList<>();

  public void log(ExceptionAndInput<? extends RuntimeException, Integer> exceptionAndInput) {
    logEvents.clear();
    String message = format(LOG_TEMPLATE, exceptionAndInput.getInput(), exceptionAndInput.getMessage());

    logEvents.add(new LogEvent(message, exceptionAndInput.getException()));
  }

  public void logWarn(ExceptionAndInput<? extends RuntimeException, Integer> exceptionAndInput) {
    logEvents.clear();
    String message = format(LOG_TEMPLATE, exceptionAndInput.getInput(), exceptionAndInput.getMessage());

    logEvents.add(new LogEvent(message, exceptionAndInput.getException()));
  }

  public boolean isEmpty() {
    return logEvents.isEmpty();
  }

  public String lastLogEventMessage() {
    return logEvents.element().getMessage();
  }

  public Throwable lastLogEventException() {
    return logEvents.element().getException();
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
