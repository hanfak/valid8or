package com.github.hanfak.valid8or.implmentation;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.*;

import static java.lang.String.format;
import static java.util.Collections.emptySet;
import static java.util.Objects.isNull;
import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

final class ValidationLogic<T> {

  T isValidOrThrow(T input, ValidationRules<T> validationRules,
                   Optional<Consumer<ExceptionAndInput<? extends RuntimeException, T>>> optionalConsumer,
                   Predicate<List<ValidationRule<Predicate<T>, ? extends Function<String, ? extends RuntimeException>>>> failedRulesPredicate) {
    var failedRules = findFailedRules(validationRules, input);
    if (failedRulesPredicate.test(failedRules)) {
      var runtimeException = throwException(input).apply(failedRules.iterator().next());
      optionalConsumer.ifPresent(consumer -> consumer.accept(createExceptionAndInput(runtimeException, input)));
      throw runtimeException;
    }
    return input;
  }

  T isValidOrThrowCombined(T input, ValidationRules<T> validationRules,
                           Optional<Consumer<ExceptionAndInput<? extends RuntimeException, T>>> optionalConsumer,
                           Predicate<List<ValidationRule<Predicate<T>, ? extends Function<String, ? extends RuntimeException>>>> failedRulesPredicate) {
    var failedRules = findFailedRules(validationRules, input);
    if (failedRulesPredicate.test(failedRules)) {
      var runtimeException = getExceptionMessage(failedRules, input);
      var validationException = createExceptionAndInput(runtimeException, input);
      optionalConsumer.ifPresent(consumer -> consumer.accept(validationException));
      throw validationException.getException();
    }
    return input;
  }

  T isValidOrThrowCombined(T input, ValidationRules<T> validationRules,
                           Optional<Consumer<ExceptionAndInput<? extends RuntimeException, T>>> optionalConsumer,
                           Predicate<List<ValidationRule<Predicate<T>, ? extends Function<String, ? extends RuntimeException>>>> failedRulesPredicate,
                           Function<String, ? extends RuntimeException> customExceptionFunction,
                           BiFunction<T, String, String> customExceptionMessageFunction) {
    var failedRules = findFailedRules(validationRules, input);
    if (failedRulesPredicate.test(failedRules)) {
      var runtimeException = createCustomNotificationException(customExceptionFunction, customExceptionMessageFunction, failedRules, input);
      var customException = createExceptionAndInput(runtimeException, input);
      optionalConsumer.ifPresent(consumer -> consumer.accept(customException));
      throw customException.getException();
    }
    return input;
  }

  Set<String> allExceptionMessages(
      T input, ValidationRules<T> validationRules,
      Predicate<List<ValidationRule<Predicate<T>, ? extends Function<String, ? extends RuntimeException>>>> failedRulesPredicate) {
    var failedRules = findFailedRules(validationRules, input);
    if (failedRulesPredicate.test(failedRules)) {
      return failedRules.stream()
          .map(ValidationRule::getExceptionMessageFunction)
          .map(exceptionMessageFunction -> exceptionMessageFunction.apply(nullSafeInput(input)))
          .collect(toSet());
    }
    return emptySet();
  }

  boolean isValid(T input,
                  ValidationRules<T> validationRules,
                  Predicate<List<ValidationRule<Predicate<T>, ? extends Function<String, ? extends RuntimeException>>>> failedRulesPredicate) {
    return failedRulesPredicate.test(findFailedRules(validationRules, input));
  }

  private List<ValidationRule<Predicate<T>, ? extends Function<String, ? extends RuntimeException>>>
  findFailedRules(ValidationRules<T> validationRules, T input) {
    return validationRules.getRules()
        .filter(not(validationRule -> validationRule.getRule().test(input)))
        .collect(toList());
  }

  private ValidationException
  getExceptionMessage(List<ValidationRule<Predicate<T>, ? extends Function<String, ? extends RuntimeException>>> failedRules,
                      T input) {
    var allExceptionMessages = createAllExceptionMessages(failedRules, input);
    return new ValidationException(format("For input: '%s', the following problems occurred: '%s'", input, allExceptionMessages));
  }

  private String
  createAllExceptionMessages(List<ValidationRule<Predicate<T>, ? extends Function<String, ? extends RuntimeException>>> failedRules,
                             T input) {
    return failedRules.stream()
        .map(ValidationRule::getExceptionMessageFunction)
        .map(exceptionMessageFunction -> exceptionMessageFunction.apply(input.toString()))// ISSUES: if input does not have toString() Overridden it will return method ref in exception message
        .distinct()
        .collect(joining("; "));
  }

  private ExceptionAndInput<RuntimeException, T> createExceptionAndInput(RuntimeException runtimeException, T input) {
    return new ExceptionAndInput<>(runtimeException, runtimeException.getMessage(), input);
  }

  private RuntimeException createCustomNotificationException(
      Function<String, ? extends RuntimeException> exceptionFunction,
      BiFunction<T, String, String> message,
      List<ValidationRule<Predicate<T>, ? extends Function<String, ? extends RuntimeException>>> failedRules, T input) {
    var allExceptionMessages = createAllExceptionMessages(failedRules, input);
    return message.andThen(exceptionFunction).apply(input, allExceptionMessages);
  }

  private Function<ValidationRule<Predicate<T>, ? extends Function<String, ? extends RuntimeException>>, ? extends RuntimeException>
  throwException(T input) {
    return predicateValidationRule -> {
      return predicateValidationRule.getException()
          .compose(predicateValidationRule.getExceptionMessageFunction())
          .apply(nullSafeInput(input)); // ISSUES: if input does not have toString() Overridden it will return method ref in exception message
    };
  }

  private String nullSafeInput(T input) {
    return isNull(input) ? null : input.toString();
  }
}
