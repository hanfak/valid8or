package com.github.hanfak.valid8or.implmentation;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.*;

import static com.github.hanfak.valid8or.implmentation.Utils.check;
import static java.util.Objects.isNull;
import static java.util.Optional.empty;
import static java.util.function.Predicate.not;

final class Valid8OrMustMustSatisfyAllRulesBuilderMust<T> implements Valid8OrMustMustSatisfyAllRulesBuilderFlowMust<T> {

  private T input;
  private Predicate<T> predicate;
  private Function<String, ? extends RuntimeException> exceptionFunction;
  private Optional<Consumer<ExceptionAndInput<? extends RuntimeException, T>>> optionalConsumer = empty();

  private final ValidationRules<T> validationRules;
  private final ValidationLogic<T> validationLogic;
  private final Predicate<List<ValidationRule<Predicate<T>, ? extends Function<String, ? extends RuntimeException>>>>
      thereExistsOneFailedRule = not(List::isEmpty);;

  Valid8OrMustMustSatisfyAllRulesBuilderMust(ValidationRules<T> validationRules, ValidationLogic<T> validationLogic) {
    this.validationRules = validationRules;
    this.validationLogic = validationLogic;
  }

  @Override
  public MustSatisfy<T> forInput(T input) {
    this.input = input;
    return this;
  }

  /**
   * TODO change name:
   * isValidFor(), holdsFor(), holdsForRule()
   * validForRule()
   */
  @Override
  public MustThrowException<T> mustSatisfy(Predicate<T> predicateRule) {
    this.predicate = predicateRule;
    return this;
  }

  @Override
  public MustThrowException<T> andSatisfies(Predicate<T> predicateRule) {
    mustSatisfy(predicateRule);
    return this;
  }

  @Override
  public MustMessage<T> orThrow(Function<String, ? extends RuntimeException> exceptionFunction) {
    this.exceptionFunction = exceptionFunction;
    return this;
  }

  @Override // TODO: Do I need this and butWas()
  public MustConnectorOrValidate<T> withMessage(UnaryOperator<String> messageFunction) {
    // TODO: if exceptionFunction is null, then set it here ?? thus avoid to methods with message
    this.validationLogic.buildRule(messageFunction, this.exceptionFunction, this.predicate, this.validationRules);
    return this;
  }

  // TODO Do i need this??? Will need better name ie because?since?
  @Override
  public MustConnectorOrValidate<T> butWas(UnaryOperator<String> messageFunction) {
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

  @SuppressWarnings("Convert2MethodRef")// For readability
  @Override
  public T validateOrThrowNotify() {
    return this.validationLogic.validateOrThrowNotify(
        this.input,
        this.validationRules,
        this.optionalConsumer,
        thereExistsOneFailedRule
    );
  }

  @SuppressWarnings("Convert2MethodRef")// For readability
  @Override
  public T validateOrThrowNotify(Function<String, ? extends RuntimeException> exceptionFunction,
                                 BiFunction<T, String, String> messageFunction) {
    return this.validationLogic.validateOrThrowNotify(
        this.input,
        this.validationRules,
        exceptionFunction,
        messageFunction,
        this.optionalConsumer,
        thereExistsOneFailedRule
    );
  }

  @Override
  public T validate() { // TODO : change to toBeValid(), validateInput(),
    return this.validationLogic.validate(this.input, this.validationRules, this.optionalConsumer, failedRules -> true);
  }

  @Override
  public Optional<T> validateThenReturnOptional() { // TODO : change to toBeValidOptional()
    var validatedInput = validate();
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
  public boolean isInvalid() {
    return !isValid();
  }
}
