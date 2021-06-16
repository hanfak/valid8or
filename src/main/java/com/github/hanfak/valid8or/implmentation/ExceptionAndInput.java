package com.github.hanfak.valid8or.implmentation;

import lombok.Value;

@Value
public class ExceptionAndInput<E, T> {

  E exception;
  String message;
  T input;
}
