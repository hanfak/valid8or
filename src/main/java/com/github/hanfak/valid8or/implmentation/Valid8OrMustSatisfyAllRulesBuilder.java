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

  @Override
  public MustConnectorOrValidate<T> withExceptionMessage(UnaryOperator<String> exceptionMessageFunction) {
    this.validationLogic.buildRule(exceptionMessageFunction, this.exceptionFunction, this.predicate, this.validationRules);
    return this;
  }

  @Override
  public MustConnectorOrValidate<T> orThrowExceptionWith(UnaryOperator<String> exceptionMessageFunction) {
    this.exceptionFunction = ValidationException::new;
    withExceptionMessage(exceptionMessageFunction);
    return this;
  }

  @Override
  public ConsumerTerminal<T> useConsumer(Consumer<ExceptionAndInput<? extends RuntimeException, T>> consumer) {
    check(isNull(consumer), "A consumer must be provided");
    this.optionalConsumer = Optional.of(consumer);
    return this;
  }
  // TODO: naming
  // IfNotValidThrowValidationException
  // IfNotValidThrowCombinedValidationException
  @Override
  public T throwNotificationIfNotValid() {
    return this.validationLogic.throwNotificationIfNotValid(
        this.input,
        this.validationRules,
        this.optionalConsumer,
        thereExistsOneFailedRule
    );
  }

  // IfNotValidThrowException
  // IfNotValidThrowCombinedException
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

  // IfNotValidThrowCustomException
  @Override
  public T throwIfNotValid() {
    return this.validationLogic.throwIfNotValid(this.input, this.validationRules, this.optionalConsumer, failedRules -> true);
  }

  // returnOptionalOrThrowCustomException
  @Override
  public Optional<T> throwIfNotValidOrReturnOptional() {
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
