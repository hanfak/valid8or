package com.github.hanfak.valid8or.implmentation.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.function.Function;

// tODO use effective java builder pattern
@EqualsAndHashCode
@Getter
public final class ValidationRuleWithExceptionToThrow<R,E> {

  private final R rule;
  private final E exception;
  private final Function<String, String> message;

  private ValidationRuleWithExceptionToThrow(R rule, E exception, Function<String, String> message) {
    this.rule = rule;
    this.exception = exception;
    this.message = message;
  }

  public static <R, E> ValidationRuleWithExceptionToThrow<R, E> rule(R rule, E exception, Function<String, String> message) {
    return new ValidationRuleWithExceptionToThrow<>(rule, exception, message);
  }

  public static <R, E> ValidationRuleWithExceptionBuilder<R, E> create() {
    return new ValidationRuleWithExceptionBuilder<>();
  }

  public static <R, E> ValidationRuleWithExceptionBuilder<R, E> rule(R rule) {
    return new ValidationRuleWithExceptionBuilder<R, E>().rule(rule);
  }

  public static class ValidationRuleWithExceptionBuilder<R, E> {

    private R rule;
    private E exception;

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

    public ValidationRuleWithExceptionToThrow<R, E> withMessage(Function<String, String> message) {
      return new ValidationRuleWithExceptionToThrow<>(rule, exception, message);
    }
  }
}
