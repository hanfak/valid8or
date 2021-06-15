package com.github.hanfak.valid8or.implmentation.mustsatisfy;

import com.github.hanfak.valid8or.implmentation.domain.ExceptionAndInput;
import com.github.hanfak.valid8or.implmentation.domain.ValidationException;
import com.github.hanfak.valid8or.implmentation.domain.ValidationRuleWithExceptionToThrow;
import com.github.hanfak.valid8or.implmentation.domain.ValidationRuleWithExceptionToThrow.ValidationRuleWithExceptionBuilder;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import static com.github.hanfak.valid8or.implmentation.domain.ValidationRuleWithExceptionToThrow.create;
import static java.util.Optional.empty;
import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

public final class Valid8OrMustSatisfyAllRulesBuilder<T> implements Valid8orMustSatisfyAllRulesBuilderFlow<T> {

  private final List<ValidationRuleWithExceptionToThrow<Predicate<T>, ? extends Function<String, ? extends RuntimeException>>>
      validationRuleWithExceptionToThrows = new ArrayList<>();

  private T input;
  private Predicate<T> predicate;
  private Function<String, ? extends RuntimeException> exceptionFunction;
  private Optional<Consumer<ExceptionAndInput<? extends RuntimeException, T>>> optionalConsumer = empty();

  @Override
  public Satisfy<T> forInput(final T input) {
    this.input = input;
    return this;
  }

  /** TODO change name:
   *  isValidFor(), holdsFor(), holdsForRule()
   *  validForRule()
   */
  @Override
  public ThrowException<T> mustSatisfy(final Predicate<T> predicate) {
    check(Objects.isNull(predicate), "Rule must be provided");
    this.predicate = predicate;
    return this;
  }

  @Override
  public ThrowException<T> andSatisfies(final Predicate<T> predicate) {
    mustSatisfy(predicate);
    return this;
  }

  @Override
  public Message<T> orThrow(final Function<String, ? extends RuntimeException> exceptionFunction) {
    check(Objects.isNull(exceptionFunction), "An exception function must not be provided");
    this.exceptionFunction = exceptionFunction;
    return this;
  }

  @Override // TODO: Do I need this and butWas()
  public ConnectorOrValidate<T> withMessage(final UnaryOperator<String> messageFunction) {
    check(Objects.isNull(messageFunction), "Message must not be provided");
    // TODO: if exceptionFunction is null, then set it here ?? thus avoid to methods with message
    buildRule(messageFunction);
    return this;
  }

  // TODO Do i need this??? Will need better name ie because?since?
  @Override
  public ConnectorOrValidate<T> butWas(final UnaryOperator<String> messageFunction) {
    this.exceptionFunction = ValidationException::new;
    withMessage(messageFunction);
    return this;
  }

  @Override
  public ConsumerTerminal<T> thenConsume(final Consumer<ExceptionAndInput<? extends RuntimeException, T>> consumer) {
    check(Objects.isNull(consumer), "A consumer must not be provided");
    this.optionalConsumer = Optional.of(consumer);
    return this;
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
    return createListOfAllExceptionMessages(findFailedRules());
  }

  @Override
  public boolean isValid() {
    return findFailedRules().isEmpty();
  }

  @Override
  public boolean isInvalid() {
    return !isValid();
  }

  private void check(final boolean argIsNull, final String message) {
    if (argIsNull) {
      throw new IllegalArgumentException(message);
    }
  }

  private void buildRule(final UnaryOperator<String> message) {
    ValidationRuleWithExceptionBuilder<Predicate<T>, Function<String, ? extends RuntimeException>>
        builder = create();
    this.validationRuleWithExceptionToThrows.add(builder.rule(this.predicate)
        .ifNotThrow(this.exceptionFunction)
        .withMessage(message));
  }

  private T validateWithCustomMessage() {
    consumeThenThrow(findFailedRules(), throwException());
    return this.input;
  }

  private List<ValidationRuleWithExceptionToThrow<Predicate<T>, ? extends Function<String, ? extends RuntimeException>>>
  findFailedRules() {
    return this.validationRuleWithExceptionToThrows.stream()
        .filter(not(x1 -> x1.getRule().test(this.input)))
        .collect(toList());
  }

  private void
  consumeThenThrow(final List<ValidationRuleWithExceptionToThrow<Predicate<T>, ? extends Function<String, ? extends RuntimeException>>> failedRules,
                   final Consumer<ValidationRuleWithExceptionToThrow<Predicate<T>, ? extends Function<String, ? extends RuntimeException>>> action) {
    try {
      failedRules.stream().findAny().ifPresent(action);
    } catch (RuntimeException e) {
      this.optionalConsumer.ifPresent(con -> con.accept(new ExceptionAndInput<>(e, e.getMessage(), this.input)));
      throw e;
    }
  }

  private Consumer<ValidationRuleWithExceptionToThrow<Predicate<T>, ? extends Function<String, ? extends RuntimeException>>>
  throwException() {
    return predicateValidationRule -> {
      throw predicateValidationRule.getException()
          .compose(predicateValidationRule.getMessage())
          .apply(nullSafeInput()); // ISSUES: if input does not have toString Overidden it will return method ref in exception message
    };
  }

  private Set<String>
  createListOfAllExceptionMessages(final List<ValidationRuleWithExceptionToThrow<Predicate<T>, ? extends Function<String, ? extends RuntimeException>>> failedRules) {
    return failedRules.stream()
        .map(ValidationRuleWithExceptionToThrow::getMessage)
        .map(messageFunction -> messageFunction.apply(nullSafeInput()))
        .collect(toSet());
  }

  private String nullSafeInput() {
    return Objects.isNull(this.input) ? null : this.input.toString();
  }
}
