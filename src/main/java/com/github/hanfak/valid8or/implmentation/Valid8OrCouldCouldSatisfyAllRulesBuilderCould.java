package com.github.hanfak.valid8or.implmentation;

import java.util.Optional;
import java.util.Set;
import java.util.function.*;

import static com.github.hanfak.valid8or.implmentation.Utils.check;
import static java.util.Objects.isNull;
import static java.util.Optional.empty;

final class Valid8OrCouldCouldSatisfyAllRulesBuilderCould<T> implements Valid8OrCouldCouldSatisfyAllRulesBuilderFlowCould<T> {

  private T input;
  private Predicate<T> predicate;
  private Function<String, ? extends RuntimeException> exceptionFunction;
  private Optional<Consumer<ExceptionAndInput<? extends RuntimeException, T>>> optionalConsumer = empty();

  private final ValidationRules<T> validationRules;
  private final ValidationLogic<T> validationLogic;

  Valid8OrCouldCouldSatisfyAllRulesBuilderCould(ValidationRules<T> validationRules, ValidationLogic<T> validationLogic) {
    this.validationRules = validationRules;
    this.validationLogic = validationLogic;
  }

  /**
   * TODO change name:
   * isValidFor(), holdsFor(), holdsForRule()
   * validForRule()
   */
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
  public CouldThrowException<T> orSatisfies(Predicate<T> predicateRule) {
    couldSatisfy(predicateRule);
    return this;
  }

  @Override
  public CouldMessage<T> orThrow(Function<String, ? extends RuntimeException> exceptionFunction) {
    this.exceptionFunction = exceptionFunction;
    return this;
  }

  @Override // TODO: Do I need this and butWas()
  public CouldConnectorOrValidate<T> withMessage(UnaryOperator<String> messageFunction) {
    // TODO: if exceptionFunction is null, then set it here ?? thus avoid to methods with message
    this.validationLogic.buildRule(messageFunction, this.exceptionFunction, this.predicate, this.validationRules);
    return this;
  }

  @Override // TODO Do i need this??? Will need better name ie because?since?
  public CouldConnectorOrValidate<T> butWas(UnaryOperator<String> messageFunction) {
    this.exceptionFunction = ValidationException::new;
    withMessage(messageFunction);
    return this;
  }

  @Override
  public ConsumerTerminal<T> thenConsume(Consumer<ExceptionAndInput<? extends RuntimeException, T>> consumer) {
    check(isNull(consumer), "A consumer must be provided");
    this.optionalConsumer = Optional.of(consumer);
    return this;
  }

  @Override
  public T validateOrThrowNotify() {
    return this.validationLogic.validateOrThrowNotify(failedRules -> failedRules.size() == this.validationRules.size(),
        this.input,
        this.optionalConsumer,
        this.validationRules);
  }

  @Override
  public T validateOrThrowNotify(Function<String, ? extends RuntimeException> exceptionFunction,
                                 BiFunction<T, String, String> messageFunction) {
    return this.validationLogic.validateOrThrowNotify(exceptionFunction,
        messageFunction,
        failedRules -> failedRules.size() == this.validationRules.size(),
        this.optionalConsumer,
        this.input,
        this.validationRules);
  }

  @Override
  public T validate() { // TODO : change to toBeValid(), validateInput(),
    return this.validationLogic.validate(this.validationRules, this.input, this.optionalConsumer, failedRules -> failedRules.size() == validationRules.size());
  }

  @Override
  public Optional<T> validateThenReturnOptional() { // TODO : change to toBeValidOptional()
    var validatedInput = validate();
    return isNull(validatedInput) ? empty() : Optional.of(validatedInput);
  }

  @Override
  public Set<String> allExceptionMessages() {
    return this.validationLogic.allExceptionMessages(this.validationRules, this.input, failedRules -> failedRules.size() == this.validationRules.size());
  }

  @Override
  public boolean isValid() {
    return this.validationLogic.isValid(failedRules -> failedRules.size() != this.validationRules.size(), this.validationRules, this.input);
  }

  @Override
  public boolean isInvalid() {
    return !isValid();
  }
}
