package com.github.hanfak.valid8or.implmentation.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static com.github.hanfak.valid8or.implmentation.domain.ValidationRuleWithExceptionToThrow.create;

public class Rules<T> {
  private final List<ValidationRuleWithExceptionToThrow<Predicate<T>, ? extends Function<String, ? extends RuntimeException>>>
      validationRuleWithExceptionToThrows = new ArrayList<>();

  public void add(ValidationRuleWithExceptionToThrow<Predicate<T>, ? extends Function<String, ? extends RuntimeException>> rule) {
    ValidationRuleWithExceptionToThrow.ValidationRuleWithExceptionBuilder<Predicate<T>, Function<String, ? extends RuntimeException>> builder = create();
    this.validationRuleWithExceptionToThrows.add(rule);
  }

  public Stream<ValidationRuleWithExceptionToThrow<Predicate<T>, ? extends Function<String, ? extends RuntimeException>>> getRules() {
    return validationRuleWithExceptionToThrows.stream();
  }

  public int size() {
    return validationRuleWithExceptionToThrows.size();
  }
}
