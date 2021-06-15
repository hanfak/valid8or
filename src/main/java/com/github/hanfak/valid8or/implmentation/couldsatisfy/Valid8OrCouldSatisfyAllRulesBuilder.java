package com.github.hanfak.valid8or.implmentation.couldsatisfy;

import com.github.hanfak.valid8or.implmentation.domain.ExceptionAndInput;
import com.github.hanfak.valid8or.implmentation.domain.Rules;
import com.github.hanfak.valid8or.implmentation.domain.ValidationException;
import com.github.hanfak.valid8or.implmentation.domain.ValidationRuleWithExceptionToThrow;
import com.github.hanfak.valid8or.implmentation.domain.ValidationRuleWithExceptionToThrow.ValidationRuleWithExceptionBuilder;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import static com.github.hanfak.valid8or.implmentation.commons.Utils.check;
import static com.github.hanfak.valid8or.implmentation.domain.ValidationRuleWithExceptionToThrow.create;
import static java.util.Collections.emptySet;
import static java.util.Optional.empty;
import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

public final class Valid8OrCouldSatisfyAllRulesBuilder<T> implements Valid8orCouldSatisfyAllRulesBuilderFlow<T> {

  private T input;
  private Predicate<T> predicate;
  private Function<String, ? extends RuntimeException> exceptionFunction;
  private Optional<Consumer<ExceptionAndInput<? extends RuntimeException, T>>> optionalConsumer = empty();

  private final Rules<T> rules;

  public Valid8OrCouldSatisfyAllRulesBuilder(Rules<T> rules) {
    this.rules = rules;
  }

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
  public ConnectorOrValidate<T> butWas(UnaryOperator<String> messageFunction) {
    exceptionFunction = ValidationException::new;
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
  public ConnectorOrValidate<T> withMessage(UnaryOperator<String> messageFunction) {
    check(Objects.isNull(messageFunction), "Message must not be provided");
    // TODO: if exceptionFunction is null, then set it here ?? thus avoid to methods with message
    buildRule(messageFunction);
    return this;
  }

  @Override
  public ConsumerTerminal<T> thenConsume(Consumer<ExceptionAndInput<? extends RuntimeException, T>> consumer) {
    check(Objects.isNull(consumer), "A consumer must not be provided");
    optionalConsumer = Optional.of(consumer);
    return this;
  }

  @Override
  public T validate() { // TODO : change to toBeValid(), validateInput(),
    return validateWithCustomMessage();
  }

  @Override
  public Optional<T> validateThenReturnOptional() { // TODO : change to toBeValidOptional()
    var validatedInput = validate();
    return Objects.isNull(validatedInput) ? empty() : Optional.of(validatedInput);
  }

  @Override
  public Set<String> allExceptionMessages() {
    var failedRules = findFailedRules();
    if (failedRules.size() == rules.size()) {
      return createListOfAllExceptionMessages(failedRules);
    }
    return emptySet();
  }

  @Override
  public boolean isValid() {
    return findFailedRules().size() != rules.size();
  }

  @Override
  public boolean isInvalid() {
    return !isValid();
  }

  private void buildRule(UnaryOperator<String> message) {
    ValidationRuleWithExceptionBuilder<Predicate<T>, Function<String, ? extends RuntimeException>> builder = create();
    ValidationRuleWithExceptionToThrow<Predicate<T>, Function<String, ? extends RuntimeException>> e = builder.rule(predicate)
        .ifNotThrow(exceptionFunction)
        .withMessage(message);
    rules.add(e);
  }

  private List<ValidationRuleWithExceptionToThrow<Predicate<T>, ? extends Function<String, ? extends RuntimeException>>>
  findFailedRules() {
    return rules.getRules()
        .filter(not(x1 -> x1.getRule().test(input)))
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
      optionalConsumer.ifPresent(con -> con.accept(new ExceptionAndInput<>(e, e.getMessage(), input)));
      throw e;
    }
  }

  private Consumer<ValidationRuleWithExceptionToThrow<Predicate<T>, ? extends Function<String, ? extends RuntimeException>>>
  throwException() {
    return predicateValidationRule -> {
      throw predicateValidationRule.getException()
          .compose(predicateValidationRule.getMessage())
          .apply(nullSafeInput()); // ISSUES: if input does not have toString Overridden it will return method ref in exception message
    };
  }

  private String nullSafeInput() {
    return Objects.isNull(input) ? null : input.toString();
  }

  private T validateWithCustomMessage() {
    var failedRules = findFailedRules();
    if (failedRules.size() == rules.size()) {
      consumeThenThrow(failedRules, throwException());
    }
    return input;
  }
}
