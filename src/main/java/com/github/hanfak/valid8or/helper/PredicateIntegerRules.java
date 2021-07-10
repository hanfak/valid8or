package com.github.hanfak.valid8or.helper;

import java.util.function.Predicate;
import java.util.stream.IntStream;

public interface PredicateIntegerRules {

  static Predicate<Integer> isEqualsTo(Integer value) {
    return input -> input.equals(value);
  }

  static Predicate<Integer> isNotEqualsTo(Integer value) {
    return input -> !input.equals(value);
  }

  static Predicate<Integer> isZero() {
    return input -> input == 0;
  }

  static Predicate<Integer> isNonZero() {
    return input -> input != 0;
  }

  static Predicate<Integer> isPositive() {
    return input -> input > 0;
  }

  static Predicate<Integer> isNegative() {
    return input -> input < 0;
  }

  static Predicate<Integer> isEven() {
    return input -> input % 2 == 0;
  }

  static Predicate<Integer> isOdd() {
    return input -> Math.abs(input) % 2 == 1;
  }

  // Newtons-Raphson Method, for positive input
  static Predicate<Integer> isSquare() {
    return input -> {
      long x1 = input;
      long x2 = 1L;
      while (x1 > x2) {
        x1 = (x1 + x2) / 2L;
        x2 = input / x1;
      }
      return x1 == x2 && input % x1 == 0L;
    };
  }

  // This is not an efficient method, use standard library
  static Predicate<Integer> isPrime() {
    return input -> input > 1 &&
        IntStream.rangeClosed(2, (int) Math.sqrt(input))
        .noneMatch(n -> (input % n == 0));
  }

  static Predicate<Integer> isGreaterThan(Integer min) {
    return x -> x > min;
  }

  static Predicate<Integer> isGreaterThanOrEqual(Integer min) {
    return x -> x >= min;
  }

  static Predicate<Integer> isLessThan(Integer max) {
    return x -> x < max;
  }

  static Predicate<Integer> isLessThanOrEqual(Integer max) {
    return x -> x <= max;
  }
}
