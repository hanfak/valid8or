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

public class ValidationLogic<T> {

  private static final String MISSING_MESSAGE_FUNCTION_EXCEPTION_MESSAGE = "Message function must be provided";
  private static final String MISSING_EXCEPTION_FUNCTION_EXCEPTION_MESSAGE = "An exception function must be provided";
  private static final String MISSING_RULE_EXCEPTION_MESSAGE = "Rule must be provided";

  private final Utils<T> utils;

  public ValidationLogic(Utils<T> utils) {
    this.utils = utils;
  }

  void buildRule(UnaryOperator<String> messageFunction,
                 Function<String, ? extends RuntimeException> exceptionFunction,
                 Predicate<T> rulePredicate,
                 ValidationRules<T> validationRules) {
    check(isNull(rulePredicate), MISSING_RULE_EXCEPTION_MESSAGE);
    check(isNull(exceptionFunction), MISSING_EXCEPTION_FUNCTION_EXCEPTION_MESSAGE);
    check(isNull(messageFunction), MISSING_MESSAGE_FUNCTION_EXCEPTION_MESSAGE);

    validationRules.add(validationRule(rulePredicate, exceptionFunction, messageFunction));
  }

  T validateOrThrowNotify(Predicate<List<ValidationRule<Predicate<T>, ? extends Function<String, ? extends RuntimeException>>>> failedRulesPredicate,
                          T input,
                          Optional<Consumer<ExceptionAndInput<? extends RuntimeException, T>>> optionalConsumer,
                          ValidationRules<T> validationRules) {
    var failedRules = findFailedRules(validationRules, input);
    if (failedRulesPredicate.test(failedRules)) {
      var exceptionMessage = getExceptionMessage(failedRules, input);
      var validationException = createExceptionAndInput(new ValidationException(exceptionMessage), input);
      optionalConsumer.ifPresent(con -> con.accept(validationException));
      throw validationException.getException();
    }
    return input;
  }

  T validateOrThrowNotify(Function<String, ? extends RuntimeException> exceptionFunction,
                          BiFunction<T, String, String> messageFunction,
                          Predicate<List<ValidationRule<Predicate<T>, ? extends Function<String, ? extends RuntimeException>>>> failedRulesPredicate,
                          Optional<Consumer<ExceptionAndInput<? extends RuntimeException, T>>> optionalConsumer,
                          T input,
                          ValidationRules<T> validationRules) {
    check(isNull(exceptionFunction), MISSING_EXCEPTION_FUNCTION_EXCEPTION_MESSAGE);
    check(isNull(messageFunction), MISSING_MESSAGE_FUNCTION_EXCEPTION_MESSAGE);

    var failedRules = findFailedRules(validationRules, input);
    if (failedRulesPredicate.test(failedRules)) {
      var runtimeException = createCustomNotificationException(exceptionFunction, messageFunction, failedRules, input);
      optionalConsumer.ifPresent(con -> con.accept(createExceptionAndInput(runtimeException, input)));
      throw runtimeException;
    }
    return input;
  }

  T validate(ValidationRules<T> validationRules,
             T input,
             Optional<Consumer<ExceptionAndInput<? extends RuntimeException, T>>> optionalConsumer,
             Predicate<List<ValidationRule<Predicate<T>, ? extends Function<String, ? extends RuntimeException>>>> failedRulesPredicate) {
    var failedRules = findFailedRules(validationRules, input);
    if (failedRulesPredicate.test(failedRules)) {
      consumeThenThrow(failedRules, throwException(input), optionalConsumer, input);
    }
    return input;
  }

  Set<String> allExceptionMessages(ValidationRules<T> validationRules,
                                   T input,
                                   Predicate<List<ValidationRule<Predicate<T>, ? extends Function<String, ? extends RuntimeException>>>> failedRulesPredicate) {
    var failedRules = findFailedRules(validationRules, input);
    if (failedRulesPredicate.test(failedRules)) {
      return createListOfAllExceptionMessages(failedRules, input);
    }
    return emptySet();
  }

  boolean isValid(Predicate<List<ValidationRule<Predicate<T>, ? extends Function<String, ? extends RuntimeException>>>> failedRulesPredicate,
                  ValidationRules<T> validationRules,
                  T input) {
    return failedRulesPredicate.test(findFailedRules(validationRules, input));
  }

  private List<ValidationRule<Predicate<T>, ? extends Function<String, ? extends RuntimeException>>>
  findFailedRules(ValidationRules<T> validationRules, T input) {
    return validationRules.getRules()
        .filter(not(validationRule -> validationRule.getRule().test(input)))
        .collect(toList());
  }

  private String
  getExceptionMessage(List<ValidationRule<Predicate<T>, ? extends Function<String, ? extends RuntimeException>>> failedRules,
                      T input) {
    var allExceptionMessages = createAllExceptionMessages(failedRules, input);
    return format("For input: '%s', the following problems occurred: '%s'", input, allExceptionMessages);
  }

  private String
  createAllExceptionMessages(List<ValidationRule<Predicate<T>, ? extends Function<String, ? extends RuntimeException>>> failedRules,
                             T input) {
    return failedRules.stream()
        .map(ValidationRule::getMessage)
        .map(messageFunction -> messageFunction.apply(input.toString()))
        .distinct()
        .collect(joining(", ")); // TODO use different separator ie ;, or add as arg
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
          .compose(predicateValidationRule.getMessage())
          .apply(utils.nullSafeInput(input)); // ISSUES: if input does not have toString Overridden it will return method ref in exception message
    };
  }

  private Set<String> createListOfAllExceptionMessages(
      List<ValidationRule<Predicate<T>, ? extends Function<String, ? extends RuntimeException>>> failedRules, T input) {
    return failedRules.stream()
        .map(ValidationRule::getMessage)
        .map(messageFunction -> messageFunction.apply(utils.nullSafeInput(input)))
        .collect(toSet());
  }
}
