package com.github.hanfak.valid8or.implmentation;

final class Utils {

  static final String MISSING_EXCEPTION_MESSAGE_FUNCTION = "Message function must be provided";
  static final String MISSING_EXCEPTION_FUNCTION = "An exception function must be provided";
  static final String MISSING_RULE_EXCEPTION_MESSAGE = "Predicate rule must be provided";
  static final String MISSING_CONSUMER = "A consumer must be provided";

  private Utils() {
    throw new IllegalStateException("No instances!");
  }

  static void check(final boolean inputIsNull, final String exceptionMessage) {
    if (inputIsNull) {
      throw new IllegalArgumentException(exceptionMessage);
    }
  }
}
