package com.github.hanfak.valid8or.implmentation;

final class Utils {

  private Utils() {
    throw new IllegalStateException("No instances!");
  }

  static void check(final boolean inputIsNull, final String exceptionMessage) {
    if (inputIsNull) {
      throw new IllegalArgumentException(exceptionMessage);
    }
  }
}
