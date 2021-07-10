package com.github.hanfak.valid8or.helper;

import java.util.Objects;
import java.util.function.Predicate;

public interface PredicateBooleanRules {

  static Predicate<Boolean> isTrue() {
    return input -> Objects.equals(input,true);
  }

  static Predicate<Boolean> isFalse() {
    return input -> Objects.equals(input,false);
  }

}
