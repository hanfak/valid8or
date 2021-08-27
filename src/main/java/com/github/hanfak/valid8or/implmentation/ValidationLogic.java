package com.github.hanfak.valid8or.implmentation;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.*;

import static com.github.hanfak.valid8or.implmentation.Utils.check;
import static com.github.hanfak.valid8or.implmentation.ValidationRule.validationRule;
import static java.lang.String.format;
import static java.util.Collections.emptySet;
import static java.util.Objects.isNull;
import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

final class ValidationLogic<T> {

  private static final String MISSING_MESSAGE_FUNCTION_EXCEPTION_MESSAGE = "Message function must be provided";
  private static final String MISSING_EXCEPTION_FUNCTION_EXCEPTION_MESSAGE = "An exception function must be provided";
  private static final String MISSING_RULE_EXCEPTION_MESSAGE = "Predicate rule must be provided";

  void buildRule(UnaryOperator<String> messageFunction,
                 Function<String, ? extends RuntimeException> exceptionFunction,
                 Predicate<T> rulePredicate,
                 ValidationRules<T> validationRules) {
    check(isNull(rulePredicate), MISSING_RULE_EXCEPTION_MESSAGE);
    check(isNull(exceptionFunction), MISSING_EXCEPTION_FUNCTION_EXCEPTION_MESSAGE);
    check(isNull(messageFunction), MISSING_MESSAGE_FUNCTION_EXCEPTION_MESSAGE);

    validationRules.add(validationRule(rulePredicate, exceptionFunction, messageFunction));
  }

  T throwNotificationIfNotValid(T input, ValidationRules<T> validationRules, Optional<Consumer<ExceptionAndInput<? extends RuntimeException, T>>> optionalConsumer, Predicate<List<ValidationRule<Predicate<T>, ? extends Function<String, ? extends RuntimeException>>>> failedRulesPredicate) {
    var failedRules = findFailedRules(validationRules, input);
    if (failedRulesPredicate.test(failedRules)) {
      var runtimeException = getExceptionMessage(failedRules, input);
      var validationException = createExceptionAndInput(runtimeException, input);
      optionalConsumer.ifPresent(con -> con.accept(validationException));
      throw validationException.getException();
    }
    return input;
  }

  T throwNotificationIfNotValid(T input, ValidationRules<T> validationRules, Function<String, ? extends RuntimeException> exceptionFunction,
                                BiFunction<T, String, String> messageFunction,
                                Optional<Consumer<ExceptionAndInput<? extends RuntimeException, T>>> optionalConsumer, Predicate<List<ValidationRule<Predicate<T>, ? extends Function<String, ? extends RuntimeException>>>> failedRulesPredicate) {
    check(isNull(exceptionFunction), MISSING_EXCEPTION_FUNCTION_EXCEPTION_MESSAGE);
    check(isNull(messageFunction), MISSING_MESSAGE_FUNCTION_EXCEPTION_MESSAGE);

    var failedRules = findFailedRules(validationRules, input);
    if (failedRulesPredicate.test(failedRules)) {
      var runtimeException = createCustomNotificationException(exceptionFunction, messageFunction, failedRules, input);
      var customException = createExceptionAndInput(runtimeException, input);
      optionalConsumer.ifPresent(con -> con.accept(customException));
      throw customException.getException();
    }
    return input;
  }

  T throwIfNotValid(T input, ValidationRules<T> validationRules,
                    Optional<Consumer<ExceptionAndInput<? extends RuntimeException, T>>> optionalConsumer,
                    Predicate<List<ValidationRule<Predicate<T>, ? extends Function<String, ? extends RuntimeException>>>> failedRulesPredicate) {
    var failedRules = findFailedRules(validationRules, input);
    if (failedRulesPredicate.test(failedRules)) {
      consumeThenThrow(failedRules, throwException(input), optionalConsumer, input);
    }
    return input;
  }

  Set<String> allExceptionMessages(T input, ValidationRules<T> validationRules,
                                   Predicate<List<ValidationRule<Predicate<T>, ? extends Function<String, ? extends RuntimeException>>>> failedRulesPredicate) {
    var failedRules = findFailedRules(validationRules, input);
    if (failedRulesPredicate.test(failedRules)) {
      return createListOfAllExceptionMessages(failedRules, input);
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

  private void consumeThenThrow(
      List<ValidationRule<Predicate<T>, ? extends Function<String, ? extends RuntimeException>>> failedRules,
      Consumer<ValidationRule<Predicate<T>, ? extends Function<String, ? extends RuntimeException>>> action,
      Optional<Consumer<ExceptionAndInput<? extends RuntimeException, T>>> optionalConsumer,
      T input) {
    try {
      failedRules.stream().findFirst().ifPresent(action);
    } catch (RuntimeException e) {
      optionalConsumer.ifPresent(con -> con.accept(createExceptionAndInput(e, input)));
      throw e;
    }
  }

  private Consumer<ValidationRule<Predicate<T>, ? extends Function<String, ? extends RuntimeException>>>
  throwException(T input) {
    return predicateValidationRule -> {
      throw predicateValidationRule.getException()
          .compose(predicateValidationRule.getExceptionMessageFunction())
          .apply(nullSafeInput(input)); // ISSUES: if input does not have toString() Overridden it will return method ref in exception message
    };
  }

  private Set<String> createListOfAllExceptionMessages(
      List<ValidationRule<Predicate<T>, ? extends Function<String, ? extends RuntimeException>>> failedRules, T input) {
    return failedRules.stream()
        .map(ValidationRule::getExceptionMessageFunction)
        .map(messageFunction -> messageFunction.apply(nullSafeInput(input)))
        .collect(toSet());
  }

  String nullSafeInput(T input) {
    return isNull(input) ? null : input.toString();
  }
}
