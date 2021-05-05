package com.github.hanfak.valid8or.implmentation.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
public final class ExceptionAndInput<E, T> {

  private final E exception;
  private final String message;
  private final T input;

  public ExceptionAndInput(E exception, String message, T input) {
    this.exception = exception;
    this.message = message;
    this.input = input;
  }
}
