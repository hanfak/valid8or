package com.github.hanfak.valid8or.implmentation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import static com.github.hanfak.valid8or.implmentation.ValidationRule.validationRule;

final class ValidationRules<T> {

  private final List<ValidationRule<Predicate<T>, Function<String, ? extends RuntimeException>>>
      rules = new ArrayList<>();

  void add(UnaryOperator<String> messageFunction,
           Function<String, ? extends RuntimeException> exceptionFunction,
           Predicate<T> rulePredicate) {
    add(validationRule(rulePredicate, exceptionFunction, messageFunction));
  }

  Stream<ValidationRule<Predicate<T>, Function<String, ? extends RuntimeException>>> getRules() {
    return rules.stream();
  }

  int size() {
    return rules.size();
  }

  private void add(ValidationRule<Predicate<T>, Function<String, ? extends RuntimeException>> rule) {
    this.rules.add(rule);
  }
}
