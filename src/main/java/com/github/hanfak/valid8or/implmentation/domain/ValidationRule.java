package com.github.hanfak.valid8or.implmentation.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.function.Function;
import java.util.function.UnaryOperator;

// tODO use effective java builder pattern
@EqualsAndHashCode
@Getter
public final class ValidationRule<R,E> {

  private final R rule;
  private final E exception;
  private final Function<String, String> message;

  private ValidationRule(R rule, E exception, UnaryOperator<String> message) {
    this.rule = rule;
    this.exception = exception;
    this.message = message;
  }

  public static <R, E> ValidationRule<R, E> rule(R rule, E exception, UnaryOperator<String> message) {
    return new ValidationRule<>(rule, exception, message);
  }

  public static <R, E> ValidationRuleBuilder<R, E> create() {
    return new ValidationRuleBuilder<>();
  }

  public static <R, E> ValidationRuleBuilder<R, E> rule(R rule) {
    return new ValidationRuleBuilder<R, E>().rule(rule);
  }

  public static class ValidationRuleBuilder<R, E> {

    private R rule;
    private E exception;

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

    public ValidationRule<R, E> withMessage(UnaryOperator<String> message) {
      return new ValidationRule<>(rule, exception, message);
    }
  }
}
