package com.github.hanfak.valid8or.implmentation;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.*;

import static com.github.hanfak.valid8or.implmentation.Utils.check;
import static java.util.Objects.isNull;
import static java.util.Optional.empty;
import static java.util.function.Predicate.not;

final class Valid8OrMustSatisfyAllRulesBuilder<T> implements Valid8OrMustSatisfyAllRulesBuilderFlow<T> {

  private T input;
  private Predicate<T> predicate;
  private Function<String, ? extends RuntimeException> exceptionFunction;
  private Optional<Consumer<ExceptionAndInput<? extends RuntimeException, T>>> optionalConsumer = empty();

  private final ValidationRules<T> validationRules;
  private final ValidationLogic<T> validationLogic;
  private final Predicate<List<ValidationRule<Predicate<T>, ? extends Function<String, ? extends RuntimeException>>>>
      thereExistsOneFailedRule = not(List::isEmpty);

  Valid8OrMustSatisfyAllRulesBuilder(ValidationRules<T> validationRules, ValidationLogic<T> validationLogic) {
    this.validationRules = validationRules;
    this.validationLogic = validationLogic;
  }

  @Override
  public MustSatisfy<T> forInput(T input) {
    this.input = input;
    return this;
  }

  @Override
  public MustThrowException<T> mustSatisfy(Predicate<T> predicateRule) {
    this.predicate = predicateRule;
    return this;
  }

  @Override
  public MustThrowException<T> and(Predicate<T> predicateRule) {
    mustSatisfy(predicateRule);
    return this;
  }

  @Override
  public MustMessage<T> orElseThrow(Function<String, ? extends RuntimeException> exceptionFunction) {
    this.exceptionFunction = exceptionFunction;
    return this;
  }

  @Override // TODO: Do I need this and butWas()
  public MustConnectorOrValidate<T> withExceptionMessage(UnaryOperator<String> exceptionMessageFunction) {
    // TODO: if exceptionFunction is null, then set it here ?? thus avoid to methods with message
    this.validationLogic.buildRule(exceptionMessageFunction, this.exceptionFunction, this.predicate, this.validationRules);
    return this;
  }

  // TODO Do i need this??? Will need better name ie because?since?
  @Override
  public MustConnectorOrValidate<T> butWas(UnaryOperator<String> messageFunction) {
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
        thereExistsOneFailedRule
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
        thereExistsOneFailedRule
    );
  }

  @Override
  public T throwIfNotValid() {
    return this.validationLogic.throwIfNotValid(this.input, this.validationRules, this.optionalConsumer, failedRules -> true);
  }

  @Override
  public Optional<T> throwIfNotValidReturnOptional() {
    var validatedInput = throwIfNotValid();
    return isNull(validatedInput) ? empty() : Optional.of(validatedInput);
  }

  @Override
  public Set<String> allExceptionMessages() {
    return this.validationLogic.allExceptionMessages(this.input, this.validationRules, failedRules -> true);
  }

  @SuppressWarnings("Convert2MethodRef")// For readability
  @Override
  public boolean isValid() {
    return this.validationLogic.isValid(this.input, this.validationRules, failedRules -> failedRules.isEmpty());
  }

  @Override
  public boolean isNotValid() {
    return !isValid();
  }
}
