package com.github.hanfak.valid8or.implmentation;

import java.util.Objects;
import java.util.function.UnaryOperator;

final class ValidationRule<R,E> {

  private final R rule;
  private final E exception;
  private final UnaryOperator<String> exceptionMessageFunction;

  private ValidationRule(R rule, E exception, UnaryOperator<String> exceptionMessageFunction) {
    this.rule = rule;
    this.exception = exception;
    this.exceptionMessageFunction = exceptionMessageFunction;
  }

  static <R, E> ValidationRule<R, E> validationRule(R rule, E exception, UnaryOperator<String> exceptionMessageFunction) {
    return new ValidationRule<>(rule, exception, exceptionMessageFunction);
  }

  public R getRule() {
    return this.rule;
  }

  public E getException() {
    return this.exception;
  }

  public UnaryOperator<String> getExceptionMessageFunction() {
    return this.exceptionMessageFunction;
  }

  public boolean equals(final Object o) {
    if (o == this) return true;
    if (!(o instanceof ValidationRule)) return false;
    final ValidationRule<?, ?> other = (ValidationRule<?, ?>) o;
    if (!Objects.equals(this.getRule(), other.getRule())) return false;
    if (!Objects.equals(this.getException(), other.getException())) return false;
    return Objects.equals(this.getExceptionMessageFunction(), other.getExceptionMessageFunction());
  }

  public int hashCode() {
    final int PRIME = 59;
    int result = 1;
    result = result * PRIME + (this.getRule() == null ? 43 : this.getRule().hashCode());
    result = result * PRIME + (this.getException() == null ? 43 : this.getException().hashCode());
    result = result * PRIME + (this.getExceptionMessageFunction() == null ? 43 : this.getExceptionMessageFunction().hashCode());
    return result;
  }

  public String toString() {
    return "ValidationRule(rule=" + this.getRule() + ", exception=" + this.getException() + ", exceptionMessageFunction=" + this.getExceptionMessageFunction() + ")";
  }
}
