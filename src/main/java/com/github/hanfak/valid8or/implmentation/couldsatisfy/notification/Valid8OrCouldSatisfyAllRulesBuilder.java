package com.github.hanfak.valid8or.implmentation.couldsatisfy.notification;

import com.github.hanfak.valid8or.implmentation.domain.ExceptionAndInput;
import com.github.hanfak.valid8or.implmentation.domain.ValidationException;
import com.github.hanfak.valid8or.implmentation.domain.ValidationRuleWithExceptionToThrow;
import com.github.hanfak.valid8or.implmentation.domain.ValidationRuleWithExceptionToThrow.ValidationRuleWithExceptionBuilder;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.github.hanfak.valid8or.implmentation.domain.ValidationRuleWithExceptionToThrow.create;
import static java.util.Collections.emptySet;
import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

public final class Valid8OrCouldSatisfyAllRulesBuilder<T> implements Valid8orCouldSatisfyAllRulesBuilderFlow<T> {

  private final List<ValidationRuleWithExceptionToThrow<Predicate<T>, ? extends Function<String, ? extends RuntimeException>>>
      validationRuleWithExceptionToThrows = new ArrayList<>();

  private T input;
  private Predicate<T> predicate;
  private Function<String, ? extends RuntimeException> exceptionFunction;
  private Optional<Consumer<ExceptionAndInput<? extends RuntimeException, T>>> optionalConsumer = Optional.empty();

  @Override
  public Satisfy<T> forInput(T input) {
    this.input = input;
    return this;
  }

  @Override
  public ThrowException<T> couldSatisfy(Predicate<T> predicate) {
    check(Objects.isNull(predicate), "Rule must be provided");
    this.predicate = predicate;
    return this;
  }

  @Override
  public ThrowException<T> orSatisfies(Predicate<T> predicate) {
    couldSatisfy(predicate);
    return this;
  }

  @Override
  public ConnectorOrValidate<T> butWas(Function<String, String> messageFunction) {
    this.exceptionFunction = ValidationException::new;
    withMessage(messageFunction);
    return this;
  }

  @Override
  public Message<T> orThrow(Function<String, ? extends RuntimeException> exceptionFunction) {
    check(Objects.isNull(exceptionFunction), "An exception function must not be provided");
    this.exceptionFunction = exceptionFunction;
    return this;
  }

  @Override
  public ConnectorOrValidate<T> withMessage(Function<String, String> messageFunction) {
    check(Objects.isNull(messageFunction), "Message must not be provided");
    // TODO: if exceptionFunction is null, then set it here ?? thus avoid to methods with message
    buildRule(messageFunction);
    return this;
  }

  @Override
  public ConsumerTerminal<T> thenConsume(Consumer<ExceptionAndInput<? extends RuntimeException, T>> consumer) {
    check(Objects.isNull(consumer), "A consumer must not be provided");
    this.optionalConsumer = Optional.of(consumer);
    return this;
  }

  @Override
  public T validate() { // TODO : change to toBeValid(), validateInput(),
    return validateWithCustomMessage();
  }

  @Override
  public Optional<T> validateThenReturnOptional() { // TODO : change to toBeValidOptional()
    T input = validate();
    return Objects.isNull(input) ? Optional.empty() : Optional.of(input);
  }

  @Override
  public Set<String> allExceptionMessages() {
    var failedRules = findFailedRules();
    if (failedRules.size() == validationRuleWithExceptionToThrows.size()) {
      return createListOfAllExceptionMessages(failedRules);
    }
    return emptySet();
  }

  @Override
  public boolean isValid() {
    return findFailedRules().size() != validationRuleWithExceptionToThrows.size();
  }

  @Override
  public boolean isInvalid() {
    return !isValid();
  }

  private void check(final boolean argIsNull, final String message) {
    if (argIsNull) {
      throw new IllegalArgumentException(message);
    }
  }

  private void buildRule(Function<String, String> message) {
    ValidationRuleWithExceptionBuilder<Predicate<T>, Function<String, ? extends RuntimeException>> builder = create();
    this.validationRuleWithExceptionToThrows.add(builder.rule(predicate)
        .ifNotThrow(exceptionFunction)
        .withMessage(message));
  }

  private List<ValidationRuleWithExceptionToThrow<Predicate<T>, ? extends Function<String, ? extends RuntimeException>>>
  findFailedRules() {
    return validationRuleWithExceptionToThrows.stream()
        .filter(not(x1 -> x1.getRule().test(this.input)))
        .collect(toList());
  }

  private Set<String> createListOfAllExceptionMessages(
      List<ValidationRuleWithExceptionToThrow<Predicate<T>,
            ? extends Function<String, ? extends RuntimeException>>> failedRules) {
    return failedRules.stream()
        .map(ValidationRuleWithExceptionToThrow::getMessage)
        .map(messageFunction -> messageFunction.apply(nullSafeInput()))
        .collect(toSet());
  }

  private void consumeThenThrow(
      List<ValidationRuleWithExceptionToThrow<Predicate<T>, ? extends Function<String, ? extends RuntimeException>>> failedRules,
      Consumer<ValidationRuleWithExceptionToThrow<Predicate<T>, ? extends Function<String, ? extends RuntimeException>>> action) {
    try {
      failedRules.stream().findAny().ifPresent(action);
    } catch (RuntimeException e) {
      this.optionalConsumer.ifPresent(con -> con.accept(new ExceptionAndInput<>(e, e.getMessage(), this.input)));
      throw e;
    }
  }

  private Consumer<ValidationRuleWithExceptionToThrow<Predicate<T>, ? extends Function<String, ? extends RuntimeException>>>
  throwException() {
    return predicateValidationRule -> {
      throw predicateValidationRule.getException()
          .compose(predicateValidationRule.getMessage())
          .apply(nullSafeInput()); // ISSUES: if input does not have toString Overidden it will return method ref in exception message
    };
  }

  private String nullSafeInput() {
    return Objects.isNull(this.input) ? null : this.input.toString();
  }

  private T validateWithCustomMessage() {
    var failedRules = findFailedRules();
    if (failedRules.size() == validationRuleWithExceptionToThrows.size()) {
      consumeThenThrow(failedRules, throwException());
    }
    return this.input;
  }
}
