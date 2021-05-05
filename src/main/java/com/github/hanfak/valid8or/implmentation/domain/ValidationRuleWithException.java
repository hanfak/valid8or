package com.github.hanfak.valid8or.implmentation.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.function.Function;

@EqualsAndHashCode
@Getter
public final class ValidationRuleWithException<R,E> {

  private final R rule;
  private final E exception;
  private final Function<String, String> message;

  private ValidationRuleWithException(R rule, E exception, Function<String, String> message) {
    this.rule = rule;
    this.exception = exception;
    this.message = message;
  }

  public static <R, E> ValidationRuleWithException<R, E> rule(R rule, E exception, Function<String, String> message) {
    return new ValidationRuleWithException<R, E>(rule, exception, message);
  }

  public static <R, E> ValidationRuleWithExceptionBuilder<R, E> create() {
    return new ValidationRuleWithExceptionBuilder<R, E>();
  }

  public static <R, E> ValidationRuleWithExceptionBuilder<R, E> rule(R rule) {
    return new ValidationRuleWithExceptionBuilder<R, E>().rule(rule);
  }

  public static class ValidationRuleWithExceptionBuilder<R, E> {

    private R rule;
    private E exception;
    private Function<String, String> message;

    ValidationRuleWithExceptionBuilder() {
    }

    public ValidationRuleWithExceptionBuilder<R, E> rule(R rule) {
      this.rule = rule;
      return this;
    }

    public ValidationRuleWithExceptionBuilder<R, E> ifNotThrow(E exception) {
      this.exception = exception;
      return this;
    }

    public ValidationRuleWithExceptionBuilder<R, E> withMessage(Function<String, String> message) {
      this.message = message;
      return this;
    }

    public ValidationRuleWithException<R, E> build() {
      return new ValidationRuleWithException<R, E>(rule, exception, message);
    }
  }
}
