package com.github.hanfak.valid8or.implmentation.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
@Getter
public final class ValidationRule<R,E> {
  private final R rule;
  private final E exception;
  private final String message;

  private ValidationRule(R rule, E exception, String message) {
    this.rule = rule;
    this.exception = exception;
    this.message = message;
  }

  public static <R, E> ValidationRule<R, E> rule(R rule, E exception, String message) {
    return new ValidationRule<R, E>(rule, exception, message);
  }

  public static <R, E> ValidationRuleBuilder<R, E> rule() {
    return new ValidationRuleBuilder<R, E>();
  }

  public static <R, E> ValidationRuleBuilder<R, E> rule(R rule) {
    return new ValidationRuleBuilder<R, E>().rule(rule);
  }

  public static class ValidationRuleBuilder<R, E> {
    private R rule;
    private E exception;
    private String message;

    ValidationRuleBuilder() {
    }

    public ValidationRuleBuilder<R, E> rule(R rule) {
      this.rule = rule;
      return this;
    }

    public ValidationRuleBuilder<R, E> ifNotThrow(E exception) {
      this.exception = exception;
      return this;
    }

    public ValidationRuleBuilder<R, E> withMessage(String message) {
      this.message = message;
      return this;
    }

    public ValidationRule<R, E> build() {
      return new ValidationRule<R, E>(rule, exception, message);
    }
  }
}
