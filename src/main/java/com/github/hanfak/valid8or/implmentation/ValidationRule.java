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
    final Object this$rule = this.getRule();
    final Object other$rule = other.getRule();
    if (!Objects.equals(this$rule, other$rule)) return false;
    final Object this$exception = this.getException();
    final Object other$exception = other.getException();
    if (!Objects.equals(this$exception, other$exception)) return false;
    final Object this$exceptionMessageFunction = this.getExceptionMessageFunction();
    final Object other$exceptionMessageFunction = other.getExceptionMessageFunction();
    return Objects.equals(this$exceptionMessageFunction, other$exceptionMessageFunction);
  }

  public int hashCode() {
    final int PRIME = 59;
    int result = 1;
    final Object $rule = this.getRule();
    result = result * PRIME + ($rule == null ? 43 : $rule.hashCode());
    final Object $exception = this.getException();
    result = result * PRIME + ($exception == null ? 43 : $exception.hashCode());
    final Object $exceptionMessageFunction = this.getExceptionMessageFunction();
    result = result * PRIME + ($exceptionMessageFunction == null ? 43 : $exceptionMessageFunction.hashCode());
    return result;
  }

  public String toString() {
    return "ValidationRule(rule=" + this.getRule() + ", exception=" + this.getException() + ", exceptionMessageFunction=" + this.getExceptionMessageFunction() + ")";
  }
}
