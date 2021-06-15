package com.github.hanfak.valid8or.implmentation.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static com.github.hanfak.valid8or.implmentation.domain.ValidationRule.create;

public class ValidationRules<T> {
  private final List<ValidationRule<Predicate<T>, ? extends Function<String, ? extends RuntimeException>>>
      validationRules = new ArrayList<>();

  public void add(ValidationRule<Predicate<T>, ? extends Function<String, ? extends RuntimeException>> rule) {
    ValidationRule.ValidationRuleBuilder<Predicate<T>, Function<String, ? extends RuntimeException>> builder = create();
    this.validationRules.add(rule);
  }

  public Stream<ValidationRule<Predicate<T>, ? extends Function<String, ? extends RuntimeException>>> getRules() {
    return validationRules.stream();
  }

  public int size() {
    return validationRules.size();
  }
}
