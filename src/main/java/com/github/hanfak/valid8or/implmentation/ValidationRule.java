package com.github.hanfak.valid8or.implmentation;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.function.UnaryOperator;

// tODO use effective java builder pattern
@EqualsAndHashCode
@Getter
final class ValidationRule<R,E> {

  private final R rule;
  private final E exception;
  private final UnaryOperator<String> message;

  private ValidationRule(R rule, E exception, UnaryOperator<String> message) {
    this.rule = rule;
    this.exception = exception;
    this.message = message;
  }

  static <R, E> ValidationRule<R, E> rule(R rule, E exception, UnaryOperator<String> message) {
    return new ValidationRule<>(rule, exception, message);
  }

  static <R, E> ValidationRuleBuilder<R, E> create() {
    return new ValidationRuleBuilder<>();
  }

  static class ValidationRuleBuilder<R, E> {

    private R rule;
    private E exception;

    ValidationRuleBuilder() {
    }

    ValidationRuleBuilder<R, E> rule(R rule) {
      this.rule = rule;
      return this;
    }

    ValidationRuleBuilder<R, E> ifNotThrow(E exception) {
      this.exception = exception;
      return this;
    }

    ValidationRule<R, E> withMessage(UnaryOperator<String> message) {
      return new ValidationRule<>(rule, exception, message);
    }
  }
}
