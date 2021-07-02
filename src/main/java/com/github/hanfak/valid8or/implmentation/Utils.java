package com.github.hanfak.valid8or.implmentation;

import java.util.Objects;

// TODO better name
class Utils<T> {

  String nullSafeInput(T input) {
    return Objects.isNull(input) ? null : input.toString();
  }

  // TODO remove static method
  static void check(final boolean argIsNull, final String message) {
    if (argIsNull) {
      throw new IllegalArgumentException(message);
    }
  }
}
