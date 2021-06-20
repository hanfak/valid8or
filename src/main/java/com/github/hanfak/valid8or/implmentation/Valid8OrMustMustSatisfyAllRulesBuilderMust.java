package com.github.hanfak.valid8or.implmentation;

import com.github.hanfak.valid8or.implmentation.ValidationRule.ValidationRuleBuilder;

import java.util.*;
import java.util.function.*;

import static com.github.hanfak.valid8or.implmentation.Utils.check;
import static com.github.hanfak.valid8or.implmentation.ValidationRule.create;
import static java.lang.String.format;
import static java.util.Optional.empty;
import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

final class Valid8OrMustMustSatisfyAllRulesBuilderMust<T> implements Valid8OrMustMustSatisfyAllRulesBuilderFlowMust<T> {

  private T input;
  private Predicate<T> predicate;
  private Function<String, ? extends RuntimeException> exceptionFunction;
  private Optional<Consumer<ExceptionAndInput<? extends RuntimeException, T>>> optionalConsumer = empty();

  private final ValidationRules<T> validationRules;

  Valid8OrMustMustSatisfyAllRulesBuilderMust(ValidationRules<T> validationRules) {
    this.validationRules = validationRules;
  }

  @Override
  public MustSatisfy<T> forInput(final T input) {
    this.input = input;
    return this;
  }

  /** TODO change name:
   *  isValidFor(), holdsFor(), holdsForRule()
   *  validForRule()
   */
  @Override
  public MustThrowException<T> mustSatisfy(final Predicate<T> predicate) {
    check(Objects.isNull(predicate), "Rule must be provided");
    this.predicate = predicate;
    return this;
  }

  @Override
  public MustThrowException<T> andSatisfies(final Predicate<T> predicate) {
    mustSatisfy(predicate);
    return this;
  }

  @Override
  public MustMessage<T> orThrow(final Function<String, ? extends RuntimeException> exceptionFunction) {
    check(Objects.isNull(exceptionFunction), "An exception function must not be provided");
    this.exceptionFunction = exceptionFunction;
    return this;
  }

  @Override // TODO: Do I need this and butWas()
  public MustConnectorOrValidate<T> withMessage(final UnaryOperator<String> messageFunction) {
    check(Objects.isNull(messageFunction), "Message must not be provided");
    // TODO: if exceptionFunction is null, then set it here ?? thus avoid to methods with message
    buildRule(messageFunction);
    return this;
  }

  // TODO Do i need this??? Will need better name ie because?since?
  @Override
  public MustConnectorOrValidate<T> butWas(final UnaryOperator<String> messageFunction) {
    exceptionFunction = ValidationException::new;
    withMessage(messageFunction);
    return this;
  }

  @Override
  public ConsumerTerminal<T> thenConsume(final Consumer<ExceptionAndInput<? extends RuntimeException, T>> consumer) {
    check(Objects.isNull(consumer), "A consumer must not be provided");
    optionalConsumer = Optional.of(consumer);
    return this;
  }

  @Override
  public T validateOrThrowNotify() {
    var failedRules = findFailedRules();
    if (!failedRules.isEmpty()) {
      var allExceptionMessages = createAllExceptionMessages(failedRules);
      var exceptionMessage = format("For input: '%s', the following problems occurred: '%s'", input, allExceptionMessages);
      var validationException = new ExceptionAndInput<>(new ValidationException(exceptionMessage), exceptionMessage, input);
      optionalConsumer.ifPresent(con -> con.accept(validationException));
      throw validationException.getException();
    }
    return input;
  }

  @Override
  public T validateOrThrowNotify(Function<String, ? extends RuntimeException> exceptionFunction, BiFunction<T, String, String> message) {
    var failedRules = findFailedRules();
    if (!failedRules.isEmpty()) {
      var allExceptionMessages = createAllExceptionMessages(failedRules);
      var runtimeException = message.andThen(exceptionFunction).apply(input, allExceptionMessages);
      var customValidationException = new ExceptionAndInput<>(runtimeException, runtimeException.getMessage(), input);
      optionalConsumer.ifPresent(con -> con.accept(customValidationException));
      throw runtimeException;
    }
    return input;
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

  private void buildRule(final UnaryOperator<String> message) {
    ValidationRuleBuilder<Predicate<T>, Function<String, ? extends RuntimeException>>
        builder = create();
    validationRules.add(builder.rule(this.predicate)
        .ifNotThrow(this.exceptionFunction)
        .withMessage(message));
  }

  private T validateWithCustomMessage() {
    consumeThenThrow(findFailedRules(), throwException());
    return this.input;
  }

  private List<ValidationRule<Predicate<T>, ? extends Function<String, ? extends RuntimeException>>>
  findFailedRules() {
    return validationRules.getRules()
        .filter(not(x1 -> x1.getRule().test(this.input)))
        .collect(toList());
  }

  private void
  consumeThenThrow(final List<ValidationRule<Predicate<T>, ? extends Function<String, ? extends RuntimeException>>> failedRules,
                   final Consumer<ValidationRule<Predicate<T>, ? extends Function<String, ? extends RuntimeException>>> action) {
    try {
      failedRules.stream().findAny().ifPresent(action);
    } catch (RuntimeException e) {
      this.optionalConsumer.ifPresent(con -> con.accept(new ExceptionAndInput<>(e, e.getMessage(), this.input)));
      throw e;
    }
  }

  private Consumer<ValidationRule<Predicate<T>, ? extends Function<String, ? extends RuntimeException>>>
  throwException() {
    return predicateValidationRule -> {
      throw predicateValidationRule.getException()
          .compose(predicateValidationRule.getMessage())
          .apply(nullSafeInput()); // ISSUES: if input does not have toString Overidden it will return method ref in exception message
    };
  }

  private Set<String>
  createListOfAllExceptionMessages(final List<ValidationRule<Predicate<T>, ? extends Function<String, ? extends RuntimeException>>> failedRules) {
    return failedRules.stream()
        .map(ValidationRule::getMessage)
        .map(messageFunction -> messageFunction.apply(nullSafeInput()))
        .collect(toSet());
  }

  private String nullSafeInput() {
    return Objects.isNull(this.input) ? null : this.input.toString();
  }

  private Consumer<String>
  customValidationExceptionMessage(
      Function<String, ? extends RuntimeException> exceptionFunction,
      BiFunction<T, String, String> message) {
    return validationMessages -> {
      throw message.andThen(exceptionFunction).apply(input, validationMessages);
    };
  }

  private String createAllExceptionMessages(List<ValidationRule<Predicate<T>, ? extends Function<String, ? extends RuntimeException>>> failedRules) {
    return failedRules.stream()
        .map(ValidationRule::getMessage)
        .map(x -> x.apply(input.toString()))
        .distinct()
        .collect(joining(", "));
  }
}
