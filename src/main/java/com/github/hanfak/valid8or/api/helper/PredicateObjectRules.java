package com.github.hanfak.valid8or.api.helper;

import java.util.Objects;
import java.util.function.Predicate;

public interface PredicateObjectRules {

  static <T> Predicate<T> isEqualsTo(T other) {
    return input -> Objects.equals(input, other);
  }

  static <T> Predicate<T> isNotNull() {
    return Objects::nonNull;
  }

  static <T> Predicate<T> isNull() {
    return Objects::isNull;
  }

//instanceOf
  //subtypeOf
}
