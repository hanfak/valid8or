package com.github.hanfak.valid8or.implmentation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

class ValidationRules<T> {
  private final List<ValidationRule<Predicate<T>, Function<String, ? extends RuntimeException>>>
      rules = new ArrayList<>();

  void add(ValidationRule<Predicate<T>, Function<String, ? extends RuntimeException>> rule) {
    this.rules.add(rule);
  }

  Stream<ValidationRule<Predicate<T>, Function<String, ? extends RuntimeException>>> getRules() {
    return rules.stream();
  }

  int size() {
    return rules.size();
  }
}
