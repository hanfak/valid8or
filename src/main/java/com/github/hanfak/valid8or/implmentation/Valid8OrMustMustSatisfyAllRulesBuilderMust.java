package com.github.hanfak.valid8or.implmentation;

import com.github.hanfak.valid8or.implmentation.ValidationRule.ValidationRuleBuilder;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
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
  public MustThrowException<T> mustSatisfy(Predicate<T> predicate) {
    check(Objects.isNull(predicate), "Rule must be provided");
    this.predicate = predicate;
    return this;
  }

  @Override
  public MustThrowException<T> andSatisfies(Predicate<T> predicate) {
    mustSatisfy(predicate);
    return this;
  }

  @Override
  public MustMessage<T> orThrow(Function<String, ? extends RuntimeException> exceptionFunction) {
    check(Objects.isNull(exceptionFunction), "An exception function must be provided");
    this.exceptionFunction = exceptionFunction;
    return this;
  }

  @Override // TODO: Do I need this and butWas()
  public MustConnectorOrValidate<T> withMessage(UnaryOperator<String> messageFunction) {
    check(Objects.isNull(messageFunction), "Message function must be provided");
    // TODO: if exceptionFunction is null, then set it here ?? thus avoid to methods with message
    buildRule(messageFunction);
    return this;
  }

  // TODO Do i need this??? Will need better name ie because?since?
  @Override
  public MustConnectorOrValidate<T> butWas(UnaryOperator<String> messageFunction) {
    check(Objects.isNull(messageFunction), "Message function must be provided");
    this.exceptionFunction = ValidationException::new;
    withMessage(messageFunction);
    return this;
  }

  @Override
  public ConsumerTerminal<T> thenConsume(Consumer<ExceptionAndInput<? extends RuntimeException, T>> consumer) {
    check(Objects.isNull(consumer), "A consumer must be provided");
    this.optionalConsumer = Optional.of(consumer);
    return this;
  }


  @Override
  public T validateOrThrowNotify() {
    var failedRules = findFailedRules();
    if (!failedRules.isEmpty()) {
      String exceptionMessage = getExceptionMessage(failedRules);
      var validationException = createExceptionAndInput(new ValidationException(exceptionMessage));
      this.optionalConsumer.ifPresent(con -> con.accept(validationException));
      throw validationException.getException();
    }
    return this.input;
  }

  @Override
  public T validateOrThrowNotify(Function<String, ? extends RuntimeException> exceptionFunction,
                                 BiFunction<T, String, String> message) {
    check(Objects.isNull(exceptionFunction), "An exception function must be provided");
    check(Objects.isNull(message), "Message function must be provided");

    var failedRules = findFailedRules();
    if (!failedRules.isEmpty()) {
      RuntimeException runtimeException = createCustomNotificationException(exceptionFunction, message, failedRules);
      this.optionalConsumer.ifPresent(con -> con.accept(createExceptionAndInput(runtimeException)));
      throw runtimeException;
    }
    return this.input;
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

  private void buildRule(UnaryOperator<String> message) {
    ValidationRuleBuilder<Predicate<T>, Function<String, ? extends RuntimeException>> builder = create();
    this.validationRules.add(builder.rule(this.predicate)
        .ifNotThrow(this.exceptionFunction)
        .withMessage(message));
  }

  private T validateWithCustomMessage() {
    consumeThenThrow(findFailedRules(), throwException());
    return this.input;
  }

  private List<ValidationRule<Predicate<T>, ? extends Function<String, ? extends RuntimeException>>>
  findFailedRules() {
    return this.validationRules.getRules()
        .filter(not(validationRule -> validationRule.getRule().test(this.input)))
        .collect(toList());
  }

  private void
  consumeThenThrow(List<ValidationRule<Predicate<T>, ? extends Function<String, ? extends RuntimeException>>> failedRules,
                   Consumer<ValidationRule<Predicate<T>, ? extends Function<String, ? extends RuntimeException>>> action) {
    try {
      failedRules.stream().findAny().ifPresent(action);
    } catch (RuntimeException e) {
      this.optionalConsumer.ifPresent(con -> con.accept(createExceptionAndInput(e)));
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
  createListOfAllExceptionMessages(List<ValidationRule<Predicate<T>, ? extends Function<String, ? extends RuntimeException>>> failedRules) {
    return failedRules.stream()
        .map(ValidationRule::getMessage)
        .map(messageFunction -> messageFunction.apply(nullSafeInput()))
        .collect(toSet());
  }

  private String nullSafeInput() {
    return Objects.isNull(this.input) ? null : this.input.toString();
  }

  private ExceptionAndInput<RuntimeException, T> createExceptionAndInput(RuntimeException runtimeException) {
    return new ExceptionAndInput<>(runtimeException, runtimeException.getMessage(), this.input);
  }

  private String getExceptionMessage(List<ValidationRule<Predicate<T>, ? extends Function<String, ? extends RuntimeException>>> failedRules) {
    var allExceptionMessages = createAllExceptionMessages(failedRules);
    return format("For input: '%s', the following problems occurred: '%s'", this.input, allExceptionMessages);
  }

  private RuntimeException createCustomNotificationException(
      Function<String, ? extends RuntimeException> exceptionFunction,
      BiFunction<T, String, String> message,
      List<ValidationRule<Predicate<T>, ? extends Function<String, ? extends RuntimeException>>> failedRules) {
    var allExceptionMessages = createAllExceptionMessages(failedRules);
    return message.andThen(exceptionFunction).apply(this.input, allExceptionMessages);
  }

  private String createAllExceptionMessages(List<ValidationRule<Predicate<T>, ? extends Function<String, ? extends RuntimeException>>> failedRules) {
    return failedRules.stream()
        .map(ValidationRule::getMessage)
        .map(messageFunction -> messageFunction.apply(this.input.toString()))
        .distinct()
        .collect(joining(", "));
  }
}
