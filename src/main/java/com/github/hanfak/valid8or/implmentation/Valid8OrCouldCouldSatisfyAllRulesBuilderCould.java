package com.github.hanfak.valid8or.implmentation;

import com.github.hanfak.valid8or.implmentation.ValidationRule.ValidationRuleBuilder;

import java.util.*;
import java.util.function.*;

import static com.github.hanfak.valid8or.implmentation.Utils.check;
import static com.github.hanfak.valid8or.implmentation.ValidationRule.create;
import static java.util.Collections.emptySet;
import static java.util.Optional.empty;
import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

final class Valid8OrCouldCouldSatisfyAllRulesBuilderCould<T> implements Valid8OrCouldCouldSatisfyAllRulesBuilderFlowCould<T> {

  private T input;
  private Predicate<T> predicate;
  private Function<String, ? extends RuntimeException> exceptionFunction;
  private Optional<Consumer<ExceptionAndInput<? extends RuntimeException, T>>> optionalConsumer = empty();

  private final ValidationRules<T> validationRules;

  Valid8OrCouldCouldSatisfyAllRulesBuilderCould(ValidationRules<T> validationRules) {
    this.validationRules = validationRules;
  }

  @Override
  public CouldSatisfy<T> forInput(T input) {
    this.input = input;
    return this;
  }

  @Override
  public CouldThrowException<T> couldSatisfy(Predicate<T> predicate) {
    check(Objects.isNull(predicate), "Rule must be provided");
    this.predicate = predicate;
    return this;
  }

  @Override
  public CouldThrowException<T> orSatisfies(Predicate<T> predicate) {
    couldSatisfy(predicate);
    return this;
  }

  @Override
  public CouldConnectorOrValidate<T> butWas(UnaryOperator<String> messageFunction) {
    exceptionFunction = ValidationException::new;
    withMessage(messageFunction);
    return this;
  }

  @Override
  public CouldMessage<T> orThrow(Function<String, ? extends RuntimeException> exceptionFunction) {
    check(Objects.isNull(exceptionFunction), "An exception function must not be provided");
    this.exceptionFunction = exceptionFunction;
    return this;
  }

  @Override
  public CouldConnectorOrValidate<T> withMessage(UnaryOperator<String> messageFunction) {
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
  public T validateOrThrowNotify() {
    return null;
  }

  @Override
  public T validateOrThrowNotify(Function<String, ? extends RuntimeException> exceptionFunction, BiFunction<T, String, String> message) {
    return null;
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
    if (failedRules.size() == validationRules.size()) {
      return createListOfAllExceptionMessages(failedRules);
    }
    return emptySet();
  }

  @Override
  public boolean isValid() {
    return findFailedRules().size() != validationRules.size();
  }

  @Override
  public boolean isInvalid() {
    return !isValid();
  }

  private void buildRule(UnaryOperator<String> message) {
    ValidationRuleBuilder<Predicate<T>, Function<String, ? extends RuntimeException>> builder = create();
    ValidationRule<Predicate<T>, Function<String, ? extends RuntimeException>> e = builder.rule(predicate)
        .ifNotThrow(exceptionFunction)
        .withMessage(message);
    validationRules.add(e);
  }

  private List<ValidationRule<Predicate<T>, ? extends Function<String, ? extends RuntimeException>>>
  findFailedRules() {
    return validationRules.getRules()
        .filter(not(x1 -> x1.getRule().test(input)))
        .collect(toList());
  }

  private Set<String> createListOfAllExceptionMessages(
      List<ValidationRule<Predicate<T>,
                  ? extends Function<String, ? extends RuntimeException>>> failedRules) {
    return failedRules.stream()
        .map(ValidationRule::getMessage)
        .map(messageFunction -> messageFunction.apply(nullSafeInput()))
        .collect(toSet());
  }

  private void consumeThenThrow(
      List<ValidationRule<Predicate<T>, ? extends Function<String, ? extends RuntimeException>>> failedRules,
      Consumer<ValidationRule<Predicate<T>, ? extends Function<String, ? extends RuntimeException>>> action) {
    try {
      failedRules.stream().findAny().ifPresent(action);
    } catch (RuntimeException e) {
      optionalConsumer.ifPresent(con -> con.accept(new ExceptionAndInput<>(e, e.getMessage(), input)));
      throw e;
    }
  }

  private Consumer<ValidationRule<Predicate<T>, ? extends Function<String, ? extends RuntimeException>>>
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
    if (failedRules.size() == validationRules.size()) {
      consumeThenThrow(failedRules, throwException());
    }
    return input;
  }
}
