package com.github.hanfak.valid8or.implmentation;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.function.UnaryOperator;

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

  static <R, E> ValidationRule<R, E> validationRule(R rule, E exception, UnaryOperator<String> message) {
    return new ValidationRule<>(rule, exception, message);
  }
}
