package com.github.hanfak.valid8or.api.helper;

import java.util.Objects;
import java.util.function.Predicate;

import static java.util.Objects.nonNull;

public interface PredicateStringRules {

  static Predicate<String> isNotEmpty() {
    return v -> !(nonNull(v) && v.isEmpty());
  }

  static Predicate<String> isNotTrimmedEmpty() {
    return v -> !(nonNull(v) && !v.trim().isEmpty());
  }

  static Predicate<String> isNotBlank() {
    return v -> !v.isBlank();
  }

  static Predicate<String> isBlank() {
    return String::isBlank;
  }

  static Predicate<String> isAllUppercase() {
    return v -> true;
  }

  // isAllLowercase
  // isCamelCase etc

  static Predicate<String> isWithinMax(int max) {
    return v -> Objects.isNull(v) || v.length() <= max;
  }

  static Predicate<String> isWithinMin(int min) {
    return v -> nonNull(v) && v.length() >= min;
  }

  //containsPattern
  //contains
}
