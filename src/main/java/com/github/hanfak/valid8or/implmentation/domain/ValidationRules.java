package com.github.hanfak.valid8or.implmentation.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class ValidationRules<T> {
  private final List<ValidationRule<Predicate<T>, Function<String, ? extends RuntimeException>>>
      rules = new ArrayList<>();

  public void add(ValidationRule<Predicate<T>, Function<String, ? extends RuntimeException>> rule) {
    this.rules.add(rule);
  }

  public Stream<ValidationRule<Predicate<T>, Function<String, ? extends RuntimeException>>> getRules() {
    return rules.stream();
  }

  public int size() {
    return rules.size();
  }
}
