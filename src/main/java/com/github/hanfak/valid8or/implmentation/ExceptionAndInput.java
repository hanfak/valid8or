package com.github.hanfak.valid8or.implmentation;

import java.util.Objects;

public final class ExceptionAndInput<E, T> {

  private final E exception;
  private final String message;
  private final T input;

  private ExceptionAndInput(E exception, String message, T input) {
    this.exception = exception;
    this.message = message;
    this.input = input;
  }

  public static <E, T>ExceptionAndInput<E, T> exceptionAndInput(E exception, String message, T input){
    return new ExceptionAndInput<>(exception, message, input);
  }

  public E getException() {
    return this.exception;
  }

  public String getMessage() {
    return this.message;
  }

  public T getInput() {
    return this.input;
  }

  public boolean equals(final Object o) {
    if (o == this) return true;
    if (!(o instanceof ExceptionAndInput)) return false;
    final ExceptionAndInput<?, ?> other = (ExceptionAndInput<?, ?>) o;
    final Object this$exception = this.getException();
    final Object other$exception = other.getException();
    if (!Objects.equals(this$exception, other$exception)) return false;
    final Object this$message = this.getMessage();
    final Object other$message = other.getMessage();
    if (!Objects.equals(this$message, other$message)) return false;
    final Object this$input = this.getInput();
    final Object other$input = other.getInput();
    return Objects.equals(this$input, other$input);
  }

  public int hashCode() {
    final int PRIME = 59;
    int result = 1;
    final Object $exception = this.getException();
    result = result * PRIME + ($exception == null ? 43 : $exception.hashCode());
    final Object $message = this.getMessage();
    result = result * PRIME + ($message == null ? 43 : $message.hashCode());
    final Object $input = this.getInput();
    result = result * PRIME + ($input == null ? 43 : $input.hashCode());
    return result;
  }

  public String toString() {
    return "ExceptionAndInput(exception=" + this.getException() + ", message=" + this.getMessage() + ", input=" + this.getInput() + ")";
  }
}
