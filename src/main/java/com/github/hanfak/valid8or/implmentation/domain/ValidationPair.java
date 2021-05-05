package com.github.hanfak.valid8or.implmentation.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
@Getter
public final class ValidationPair<R,E> {

  private final R rule;
  private final E exception;

  private ValidationPair(R rule, E exception) {
    this.rule = rule;
    this.exception = exception;
  }

  public static <R, E> ValidationPair<R, E> rule(R rule, E exception) {
    return new ValidationPair<R, E>(rule, exception);
  }

  public static <E> E ifNotThrow(E exception) {
    return exception;
  }

  public static <R, E> ValidationPairBuilder<R, E> builder() {
    return new ValidationPairBuilder<R, E>();
  }

  public static class ValidationPairBuilder<R, E> {
    private R rule;
    private E exception;

    ValidationPairBuilder() {
    }

    public ValidationPairBuilder<R, E> rule(R rule) {
      this.rule = rule;
      return this;
    }

    public ValidationPairBuilder<R, E> ifNotThrow(E exception) {
      this.exception = exception;
      return this;
    }

    public ValidationPair<R, E> build() {
      return new ValidationPair<R, E>(rule, exception);
    }

    public String toString() {
      return "ValidationPair.ValidationPairBuilder(rule=" + this.rule + ", exception=" + this.exception + ")";
    }
  }
}
