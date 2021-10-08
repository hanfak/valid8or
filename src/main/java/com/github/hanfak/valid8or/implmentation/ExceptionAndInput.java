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
    if (!Objects.equals(this.getException(), other.getException())) return false;
    if (!Objects.equals(this.getMessage(), other.getMessage())) return false;
    return Objects.equals(this.getInput(), other.getInput());
  }

  public int hashCode() {
    final int PRIME = 59;
    int result = 1;
    result = result * PRIME + (this.getException() == null ? 43 : this.getException().hashCode());
    result = result * PRIME + (this.getMessage() == null ? 43 : ((Object) this.getMessage()).hashCode());
    result = result * PRIME + (this.getInput() == null ? 43 : this.getInput().hashCode());
    return result;
  }

  public String toString() {
    return "ExceptionAndInput(exception=" + this.getException() + ", message=" + this.getMessage() + ", input=" + this.getInput() + ")";
  }
}
