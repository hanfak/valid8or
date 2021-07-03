package com.github.hanfak.valid8or.implmentation;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;

import java.util.function.UnaryOperator;

@Value
@AllArgsConstructor
class ValidationRule<R,E> {

  R rule;
  E exception;
  UnaryOperator<String> message;

  static <R, E> ValidationRule<R, E> validationRule(R rule, E exception, UnaryOperator<String> message) {
    return new ValidationRule<>(rule, exception, message);
  }
}
