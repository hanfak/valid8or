package com.github.hanfak.valid8or.implmentation;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.*;

import static com.github.hanfak.valid8or.implmentation.Utils.*;
import static java.util.Objects.isNull;
import static java.util.Optional.empty;
import static java.util.function.Predicate.not;

final class Valid8OrCouldSatisfyAllRulesBuilder<T> implements Valid8OrCouldSatisfyAllRulesBuilderFlow<T> {

  private T input;
  private Predicate<T> rulePredicate;
  private Function<String, ? extends RuntimeException> exceptionFunction;
  private Optional<Consumer<ExceptionAndInput<? extends RuntimeException, T>>> optionalConsumer = empty();

  private final ValidationRules<T> validationRules;
  private final ValidationLogic<T> validationLogic;

  Valid8OrCouldSatisfyAllRulesBuilder(ValidationRules<T> validationRules, ValidationLogic<T> validationLogic) {
    this.validationRules = validationRules;
    this.validationLogic = validationLogic;
  }

  @Override
  public CouldSatisfy<T> forInput(T input) {
    this.input = input;
    return this;
  }

  @Override
  public CouldThrowException<T> couldSatisfy(Predicate<T> rulePredicate) {
    this.rulePredicate = rulePredicate;
    return this;
  }

  @Override
  public CouldThrowException<T> or(Predicate<T> rulePredicate) {
    couldSatisfy(rulePredicate);
    return this;
  }

  @Override
  public CouldMessage<T> orElseThrow(Function<String, ? extends RuntimeException> exceptionFunction) {
    this.exceptionFunction = exceptionFunction;
    return this;
  }

  @Override
  public CouldConnectorOrValidate<T> withExceptionMessage(UnaryOperator<String> exceptionMessageFunction) {
    check(isNull(this.rulePredicate), MISSING_RULE_EXCEPTION_MESSAGE);
    check(isNull(this.exceptionFunction), MISSING_EXCEPTION_FUNCTION);
    check(isNull(exceptionMessageFunction), MISSING_EXCEPTION_MESSAGE_FUNCTION);

    this.validationRules.add(exceptionMessageFunction, this.exceptionFunction, this.rulePredicate);
    return this;
  }

  @Override
  public CouldConnectorOrValidate<T> orThrowExceptionWith(UnaryOperator<String> exceptionMessageFunction) {
    this.exceptionFunction = ValidationException::new;
    withExceptionMessage(exceptionMessageFunction);
    return this;
  }

  @Override
  public ConsumerTerminal<T> useConsumer(Consumer<ExceptionAndInput<? extends RuntimeException, T>> consumer) {
    check(isNull(consumer), MISSING_CONSUMER);
    this.optionalConsumer = Optional.of(consumer);
    return this;
  }

  // TODO: naming
  @Override
  public T throwNotificationIfNotValid() {
    return this.validationLogic.throwNotificationIfNotValid(
        this.input, this.validationRules, this.optionalConsumer,
        allValidationRulesFailed());
  }

  @Override
  public T throwNotificationIfNotValid(Function<String, ? extends RuntimeException> customExceptionFunction,
                                       BiFunction<T, String, String> customExceptionMessageFunction) {
    check(isNull(customExceptionFunction), MISSING_EXCEPTION_FUNCTION);
    check(isNull(customExceptionMessageFunction), MISSING_EXCEPTION_MESSAGE_FUNCTION);

    return this.validationLogic.throwNotificationIfNotValid(
        this.input, this.validationRules, this.optionalConsumer,
        allValidationRulesFailed(),
        customExceptionFunction,
        customExceptionMessageFunction);
  }

  @Override
  public T throwIfNotValid() {
    return this.validationLogic.throwIfNotValid(
        this.input, this.validationRules, this.optionalConsumer,
        allValidationRulesFailed());
  }

  @Override
  public Optional<T> throwIfNotValidOrReturnOptional() {
    var validatedInput = throwIfNotValid();
    return isNull(validatedInput) ? empty() : Optional.of(validatedInput);
  }

  @Override
  public Set<String> allExceptionMessages() {
    return this.validationLogic.allExceptionMessages(this.input, this.validationRules, allValidationRulesFailed());
  }

  @Override
  public boolean isValid() {
    return this.validationLogic.isValid(this.input, this.validationRules, not(allValidationRulesFailed()));
  }

  @Override
  public boolean isNotValid() {
    return !isValid();
  }

  private Predicate<List<ValidationRule<Predicate<T>, ? extends Function<String, ? extends RuntimeException>>>>
  allValidationRulesFailed() {
    return failedRules -> failedRules.size() == this.validationRules.size();
  }
}
