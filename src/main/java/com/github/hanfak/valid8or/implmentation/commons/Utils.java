package com.github.hanfak.valid8or.implmentation.commons;

public class Utils {
  public static void check(final boolean argIsNull, final String message) {
    if (argIsNull) {
      throw new IllegalArgumentException(message);
    }
  }
}
