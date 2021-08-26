package com.github.hanfak.valid8or.implmentation;

import java.util.Optional;
import java.util.Set;
import java.util.function.*;

import static com.github.hanfak.valid8or.implmentation.Utils.check;
import static java.util.Objects.isNull;
import static java.util.Optional.empty;

final class Valid8OrCouldSatisfyAllRulesBuilder<T> implements Valid8OrCouldSatisfyAllRulesBuilderFlow<T> {

  private T input;
  private Predicate<T> predicate;
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
  public CouldThrowException<T> couldSatisfy(Predicate<T> predicateRule) {
    this.predicate = predicateRule;
    return this;
  }

  @Override
  public CouldThrowException<T> or(Predicate<T> predicateRule) {
    couldSatisfy(predicateRule);
    return this;
  }

  @Override
  public CouldMessage<T> orElseThrow(Function<String, ? extends RuntimeException> exceptionFunction) {
    this.exceptionFunction = exceptionFunction;
    return this;
  }

  @Override // TODO: Do I need this and butWas()
  public CouldConnectorOrValidate<T> withExceptionMessage(UnaryOperator<String> exceptionMessageFunction) {
    // TODO: if exceptionFunction is null, then set it here ?? thus avoid to methods with message
    this.validationLogic.buildRule(exceptionMessageFunction, this.exceptionFunction, this.predicate, this.validationRules);
    return this;
  }

  @Override // TODO Do i need this??? Will need better name ie because?since?
  public CouldConnectorOrValidate<T> butWas(UnaryOperator<String> messageFunction) {
    this.exceptionFunction = ValidationException::new;
    withExceptionMessage(messageFunction);
    return this;
  }

  @Override
  public ConsumerTerminal<T> useConsumer(Consumer<ExceptionAndInput<? extends RuntimeException, T>> consumer) {
    check(isNull(consumer), "A consumer must be provided");
    this.optionalConsumer = Optional.of(consumer);
    return this;
  }

  @Override
  public T throwNotificationIfNotValid() {
    return this.validationLogic.throwNotificationIfNotValid(
        this.input,
        this.validationRules,
        this.optionalConsumer,
        failedRules -> failedRules.size() == this.validationRules.size()
    );
  }

  @Override
  public T throwNotificationIfNotValid(Function<String, ? extends RuntimeException> exceptionFunction,
                                       BiFunction<T, String, String> exceptionMessageFunction) {
    return this.validationLogic.throwNotificationIfNotValid(
        this.input,
        this.validationRules,
        exceptionFunction,
        exceptionMessageFunction,
        this.optionalConsumer,
        failedRules -> failedRules.size() == this.validationRules.size()
    );
  }

  @Override
  public T throwIfNotValid() {
    return this.validationLogic.throwIfNotValid(
        this.input,
        this.validationRules,
        this.optionalConsumer,
        failedRules -> failedRules.size() == validationRules.size());
  }

  @Override
  public Optional<T> throwIfNotValidReturnOptional() {
    var validatedInput = throwIfNotValid();
    return isNull(validatedInput) ? empty() : Optional.of(validatedInput);
  }

  @Override
  public Set<String> allExceptionMessages() {
    return this.validationLogic.allExceptionMessages(
        this.input,
        this.validationRules,
        failedRules -> failedRules.size() == this.validationRules.size());
  }

  @Override
  public boolean isValid() {
    return this.validationLogic.isValid(
        this.input, this.validationRules, failedRules -> failedRules.size() != this.validationRules.size()
    );
  }

  @Override
  public boolean isNotValid() {
    return !isValid();
  }
}
