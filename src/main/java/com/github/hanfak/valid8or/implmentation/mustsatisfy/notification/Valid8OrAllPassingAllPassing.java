package com.github.hanfak.valid8or.implmentation.mustsatisfy.notification;

import com.github.hanfak.valid8or.implmentation.domain.ExceptionAndInput;
import com.github.hanfak.valid8or.implmentation.domain.ValidationException;
import com.github.hanfak.valid8or.implmentation.domain.ValidationRuleWithException;
import com.github.hanfak.valid8or.implmentation.domain.ValidationRuleWithException.ValidationRuleWithExceptionBuilder;

import java.util.*;
import java.util.function.*;

import static com.github.hanfak.valid8or.implmentation.domain.ValidationRuleWithException.create;
import static java.lang.String.format;
import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

public final class Valid8OrAllPassingAllPassing<T> implements Valid8orAllPassingBuilder<T> {

  private final List<ValidationRuleWithException<Predicate<T>, ? extends Function<String, ? extends RuntimeException>>> pairLst = new ArrayList<>();

  private T input;
  private Predicate<T> predicate;
  private Function<String, ? extends RuntimeException> exceptionFunction;
  private Optional<Consumer<ExceptionAndInput<? extends RuntimeException, T>>> optionalConsumer = Optional.empty();

  @Override
  public Satisfy<T> forInput(T input) {
    this.input = input;
    return this;
  }

  @Override
  public ThrowExceptionForMustRule<T> mustSatisfy(Predicate<T> predicate) {
    if (Objects.isNull(predicate)) {// TODO: test
      throw new IllegalArgumentException("Rule cannot be null");
    }
    this.predicate = predicate;
    return this;
  }

  @Override
  public ThrowExceptionForMustRule<T> andSatisfies(Predicate<T> predicate) {
    mustSatisfy(predicate);
    return this;
  }

  // TODO Do i need this???
  @Override
  public ConnectorOrValidateForMustSatisfy<T> butWas(Function<String, String> message) {
    this.exceptionFunction = ValidationException::new;
    withMessage(message);
    return this;
  }

  @Override
  public MessageForMustRule<T> orThrow(Function<String, ? extends RuntimeException> exceptionFunction) {
    if (Objects.isNull(exceptionFunction)) {// TODO: test
      throw new IllegalArgumentException("exception function cannot be null");
    }
    this.exceptionFunction = exceptionFunction;
    return this;
  }

  @Override
  public ConnectorOrValidateForMustSatisfy<T> withMessage(Function<String, String> message) {
    if (Objects.isNull(message)) {// TODO: test
      throw new IllegalArgumentException("message function cannot be null");
    }
    // TODO: if exceptionFunction is null, then set it here ?? thus avoid to methods with message
    buildRule(message);
    return this;
  }

  @Override
  public ConsumerTerminal<T> thenConsume(Consumer<ExceptionAndInput<? extends RuntimeException, T>> consumer) {
    if (Objects.isNull(consumer)) {// TODO: test
      throw new IllegalArgumentException("consumer cannot be null");
    }
    this.optionalConsumer = Optional.of(consumer);
    return this;
  }

  @Override
  public T validate() {
    return validateWithCustomMessage();
  }

  @Override
  public Optional<T> validateThenReturnOptional() {
    // TODO: Need to test
    T input = validate();
    return Objects.isNull(input) ? Optional.empty() /*or null??*/ : Optional.of(input);
  }

  @Override
  public T orElseThrowValidationException() {
    return validateWithValidationExceptionCustomMessage();
  }

  @Override
  public T orElseThrowValidationException(Function<String, ? extends RuntimeException> exceptionFunction, BiFunction<T, String, String> message) {
    return validateWithOverallCustomExceptionCustomMessage(exceptionFunction, message);
  }

  @Override
  public Set<String> allExceptionMessages() {
    var failedRules = findFailedRules();
    return createListOfAllExceptionMessages(failedRules);
  }

  @Override
  public boolean isValid() {
    return (findFailedRules().isEmpty());
  }

  @Override
  public boolean isInvalid() {
    return !isValid();
  }

  private void buildRule(Function<String, String> message) {
    ValidationRuleWithExceptionBuilder<Predicate<T>, Function<String, ? extends RuntimeException>> builder = create();
    this.pairLst.add(builder.rule(predicate)
        .ifNotThrow(exceptionFunction)
        .withMessage(message));
  }

  private List<ValidationRuleWithException<Predicate<T>, ? extends Function<String, ? extends RuntimeException>>> findFailedRules() {
    return pairLst.stream()
        .filter(not(x1 -> x1.getRule().test(this.input)))
        .collect(toList());
  }

  private Consumer<String> customValidationExceptionMessage(Function<String, ? extends RuntimeException> exceptionFunction, BiFunction<T, String, String> message) {
    return validationMessages -> {
      throw message.andThen(exceptionFunction).apply(input, validationMessages);
    };
  }

  private String createAllExceptionMessage(List<ValidationRuleWithException<Predicate<T>, ? extends Function<String, ? extends RuntimeException>>> failedRules) {
    return failedRules.stream()
        .map(ValidationRuleWithException::getMessage)
        .map(x -> x.apply(input.toString()))
        .distinct()
        .collect(joining(", "));
  }

  private Set<String> createListOfAllExceptionMessages(List<ValidationRuleWithException<Predicate<T>, ? extends Function<String, ? extends RuntimeException>>> failedRules) {
    return failedRules.stream()
        .map(ValidationRuleWithException::getMessage)
//        .map(x -> x == null ?  "null" : x)
        .map(x -> x.apply(input.toString()))
        .collect(toSet());
  }

  private void consumeThenThrow(List<ValidationRuleWithException<Predicate<T>, ? extends Function<String, ? extends RuntimeException>>> failedRules, Consumer<ValidationRuleWithException<Predicate<T>, ? extends Function<String, ? extends RuntimeException>>> action) {
    try {
      failedRules.stream().findAny().ifPresent(action);
    } catch (RuntimeException e) {
      var runtimeExceptionExceptionAndInput = new ExceptionAndInput<>(e, e.getMessage(), input);
      optionalConsumer.ifPresent(con -> con.accept(runtimeExceptionExceptionAndInput));
      throw e;
    }
  }

  private Consumer<ValidationRuleWithException<Predicate<T>, ? extends Function<String, ? extends RuntimeException>>> throwException() {
    return predicateValidationRule -> {
      throw predicateValidationRule.getException()
          .compose(predicateValidationRule.getMessage())
          .apply(this.input.toString()); // ISSUES: if input does not have toString Overidden it will return method ref in exception message
    };
  }

  private void throwException(Supplier<? extends RuntimeException> x) {
    throw x.get();
  }

  private T validateWithCustomMessage() {
    var failedRules = findFailedRules();
    consumeThenThrow(failedRules, throwException());
    return this.input;
  }

  private T validateWithValidationExceptionCustomMessage() {
    var failedRules = findFailedRules();
    var allExceptionMessages = createAllExceptionMessage(failedRules);
    String exceptionMessage = format("For input: '%s', the following problems occurred: '%s'", input, allExceptionMessages);
    var validationException = new ExceptionAndInput<>(new ValidationException(exceptionMessage), exceptionMessage, input);
    if (!failedRules.isEmpty()) {
      optionalConsumer.ifPresent(con -> con.accept(validationException));
      throw validationException.getException();
    }
    return this.input;
  }

  private T validateWithOverallCustomExceptionCustomMessage(Function<String, ? extends RuntimeException> exceptionFunction, BiFunction<T, String, String> message) {
    var failedRules = findFailedRules();
    var allExceptionMessages = createAllExceptionMessage(failedRules);
    var validationException = new ExceptionAndInput<>(exceptionFunction.apply(allExceptionMessages), allExceptionMessages, input);

    if (!failedRules.isEmpty()) {
      optionalConsumer.ifPresent(con -> con.accept(validationException));
      customValidationExceptionMessage(exceptionFunction, message).accept(validationException.getMessage());
    }
    return this.input;
  }
}
