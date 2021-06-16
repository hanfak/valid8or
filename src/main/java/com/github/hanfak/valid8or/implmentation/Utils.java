package com.github.hanfak.valid8or.implmentation;

class Utils {

  private Utils() {
    throw new IllegalStateException("Utility class");
  }

  static void check(final boolean argIsNull, final String message) {
    if (argIsNull) {
      throw new IllegalArgumentException(message);
    }
  }
}
