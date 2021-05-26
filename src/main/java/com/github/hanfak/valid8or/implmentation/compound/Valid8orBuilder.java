package com.github.hanfak.valid8or.implmentation.compound;

import com.github.hanfak.valid8or.implmentation.domain.ExceptionAndInput;
import com.github.hanfak.valid8or.implmentation.domain.ValidationException;
import com.github.hanfak.valid8or.implmentation.domain.ValidationPair;
import com.github.hanfak.valid8or.implmentation.domain.ValidationPair.ValidationPairBuilder;
import com.github.hanfak.valid8or.implmentation.domain.ValidationRuleWithException;
import com.github.hanfak.valid8or.implmentation.domain.ValidationRuleWithException.ValidationRuleWithExceptionBuilder;

import java.util.*;
import java.util.function.*;

import static com.github.hanfak.valid8or.implmentation.domain.ValidationPair.builder;
import static com.github.hanfak.valid8or.implmentation.domain.ValidationRuleWithException.create;
import static java.lang.String.format;
import static java.util.Collections.emptySet;
import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

public final class Valid8orBuilder<T> implements Valid8orBuilderInterface<T> {

  private final List<ValidationRuleWithException<Predicate<T>, ? extends Function<String, ? extends RuntimeException>>> pairLst = new ArrayList<>();
  private final List<ValidationPair<Predicate<T>, ? extends Supplier<? extends RuntimeException>>> otherPairList = new ArrayList<>();

  private T input;
  private Predicate<T> predicate;
  private Function<String, ? extends RuntimeException> exceptionFunction;
  private boolean couldSatisfyInUse;
  private boolean mustSatisfyInUse;
  private Optional<Consumer<ExceptionAndInput<? extends RuntimeException, T>>> optionalConsumer = Optional.empty();

  @Override
  public Satisfy<T> forInput(T input) {
    this.input = input;
    return this;
  }

  @Override
  public ThrowExceptionForCouldRule<T> couldSatisfy(Predicate<T> predicate) {
    if (Objects.isNull(predicate)) {// TODO: test
      throw new IllegalArgumentException("Rule cannot be null");
    }
    couldSatisfyInUse = true;
    this.predicate = predicate;
    return this;
  }

  @Override
  public ThrowExceptionForCouldRule<T> orSatisfies(Predicate<T> predicate) {
    couldSatisfy(predicate);
    return this;
  }

  @Override
  public ConnectorOrValidateForCouldSatisfy<T> ifNotThrowAn(Supplier<? extends RuntimeException> exception) {
    if (Objects.isNull(exception)) {// TODO: test
      throw new IllegalArgumentException("exception supplier cannot be null");
    }
    ValidationPairBuilder<Predicate<T>, Supplier<? extends RuntimeException>> builder = builder();
    this.otherPairList.add(builder.rule(predicate).ifNotThrow(exception).build());
    return this;
  }

  @Override
  public ConnectorOrValidateForCouldSatisfy<T> butWas(Function<String, String> message) {
    buildRule(message);
    return this;
  }

  @Override
  public MessageForCouldRule<T> ifNotThrow(Function<String, ? extends RuntimeException> exceptionFunction) {
    if (Objects.isNull(exceptionFunction)) { // TODO: test
      throw new IllegalArgumentException("exception function cannot be null");
    }
    this.exceptionFunction = exceptionFunction;
    return this;
  }

  @Override
  public ConnectorOrValidateForCouldSatisfy<T> withMessage(Function<String, String> message) {
    if (Objects.isNull(message)) {// TODO: test
      throw new IllegalArgumentException("message function cannot be null");
    }
    buildRule(message);
    return this;
  }

  @Override
  public ThrowExceptionForMustRule<T> mustSatisfy(Predicate<T> predicate) {
    if (Objects.isNull(predicate)) {// TODO: test
      throw new IllegalArgumentException("Rule cannot be null");
    }
    mustSatisfyInUse = true;
    this.predicate = predicate;
    return this;
  }

  @Override
  public ThrowExceptionForMustRule<T> andSatisfies(Predicate<T> predicate) {
    mustSatisfy(predicate);
    return this;
  }

  @Override
  public ConnectorOrValidateForMustSatisfy<T> ifNotWillThrowAn(Supplier<? extends RuntimeException> exceptionSupplier) {
    ValidationPairBuilder<Predicate<T>, Supplier<? extends RuntimeException>> builder = builder();
    this.otherPairList.add(builder.rule(predicate).ifNotThrow(exceptionSupplier).build());
    return this;
  }

  @Override
  public ConnectorOrValidateForMustSatisfy<T> butIs(Function<String, String> message) {
    buildRule(message);
    return this;
  }

  @Override
  public MessageForMustRule<T> ifNotWillThrow(Function<String, ? extends RuntimeException> exceptionFunction) {
    if (Objects.isNull(exceptionFunction)) {// TODO: test
      throw new IllegalArgumentException("exception function cannot be null");
    }
    this.exceptionFunction = exceptionFunction;
    return this;
  }

  @Override
  public ConnectorOrValidateForMustSatisfy<T> hasMessage(Function<String, String> message) {
    if (Objects.isNull(message)) {// TODO: test
      throw new IllegalArgumentException("message function cannot be null");
    }
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
    if (otherPairList.isEmpty()) {
      return validateWithCustomMessage();
    }
    return validateWithHardcodeMessage();
  }

  @Override
  public Optional<T> validateThenReturnOptional() {
    // TODO: Need to test
    T input = validate();
    return Objects.isNull(input) ? Optional.empty() /*or null??*/ : Optional.of(input);
  }

  @Override
  public T orElseThrowValidationException() {
    if (otherPairList.isEmpty()) {
      return validateWithValidationExceptionCustomMessage();
    }
    return validateWithValidationExceptionHardcodedMessage();
  }

  @Override
  public T orElseThrowValidationException(Function<String, ? extends RuntimeException> exceptionFunction, BiFunction<T, String, String> message) {
    if (otherPairList.isEmpty()) {
      return validateWithOverallCustomExceptionCustomMessage(exceptionFunction, message);
    }
    return validateWithOverallCustomExceptionHardcodedMessage(exceptionFunction, message);
  }

  @Override
  public Set<String> allExceptionMessages() {
    if (otherPairList.isEmpty()) {
      var failedRules = findFailedRules();
      if(mustSatisfyInUse) {
        return createListOfAllExceptionMessages(failedRules);
      }
      if (couldSatisfyInUse && failedRules.size() == pairLst.size()) {
        return createListOfAllExceptionMessages(failedRules);
      }
      return emptySet();
    }
    var failedRules1 = findFailedRules1();
    if(mustSatisfyInUse) {
      return createListOfAllExceptionMessages1(failedRules1);
    }
    if (couldSatisfyInUse && failedRules1.size() == otherPairList.size()) {
      return createListOfAllExceptionMessages1(failedRules1);
    }
    return emptySet();
  }

  @Override
  public boolean isValid() {
    if (otherPairList.isEmpty()) {
      return (couldSatisfyInUse && findFailedRules().size() != pairLst.size())
          || (mustSatisfyInUse && findFailedRules().isEmpty());
    }
    return (couldSatisfyInUse && findFailedRules1().size() != otherPairList.size())
        || (mustSatisfyInUse && findFailedRules1().isEmpty());
  }

  @Override
  public boolean isInvalid() {
    return !isValid();
  }

  private void buildRule(Function<String, String> message) {
    ValidationRuleWithExceptionBuilder<Predicate<T>, Function<String, ? extends RuntimeException>> builder = create();
    this.pairLst.add(builder.rule(predicate)
        .ifNotThrow(exceptionFunction)
        .withMessage(message)
        .build());
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

  private List<ValidationPair<Predicate<T>, ? extends Supplier<? extends RuntimeException>>> findFailedRules1() {
    return otherPairList.stream()
        .filter(not(x1 -> x1.getRule().test(this.input)))
        .collect(toList());
  }

  private String createAllExceptionMessage(List<ValidationRuleWithException<Predicate<T>, ? extends Function<String, ? extends RuntimeException>>> failedRules) {
    return failedRules.stream()
        .map(ValidationRuleWithException::getMessage)
        .map(x -> x.apply(input.toString()))
        .distinct()
        .collect(joining(", "));
  }

  private String createAllExceptionMessage1(List<ValidationPair<Predicate<T>, ? extends Supplier<? extends RuntimeException>>> failedRules) {
    return failedRules.stream()
        .map(x -> x.getException().get().getMessage())
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

  private Set<String> createListOfAllExceptionMessages1(List<ValidationPair<Predicate<T>, ? extends Supplier<? extends RuntimeException>>> failedRules) {
    return failedRules.stream()
        .map(x -> {
          String message = x.getException().get().getMessage();
          if (message == null) return "null";
          return message;
        })
        .collect(toSet());
  }

  private void consumeThenThrow(List<ValidationRuleWithException<Predicate<T>, ? extends Function<String, ? extends RuntimeException>>> failedRules, Consumer<ValidationRuleWithException<Predicate<T>, ? extends Function<String, ? extends RuntimeException>>> action) {
    try {
      var failedRulesWithDefinedExceptions = failedRules.stream().findAny();
      if (failedRulesWithDefinedExceptions.filter(x -> Objects.isNull(x.getException())).isPresent()) {
        //alternate
//        var message = failedRulesWithDefinedExceptions
//            .map(x -> x.getMessage().apply(this.input.toString()))
//            .orElse("Rule failed");
//        throw new ValidationException(message);
        throw failedRulesWithDefinedExceptions
            .map(x -> x.getMessage().apply(this.input.toString()))
            .map(ValidationException::new)
            .orElseThrow();
      }
      failedRulesWithDefinedExceptions.ifPresent(action);
    } catch (RuntimeException e) {
      var runtimeExceptionExceptionAndInput = new ExceptionAndInput<>(e, e.getMessage(), input);
      optionalConsumer.ifPresent(con -> con.accept(runtimeExceptionExceptionAndInput));
      throw e;
    }
  }

  private void consumeThenThrow(List<ValidationPair<Predicate<T>, ? extends Supplier<? extends RuntimeException>>> failedRules) {
    try {
      failedRules.stream().map(ValidationPair::getException).findAny().ifPresent(this::throwException);
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

  private T validateWithHardcodeMessage() {
    var failedRules1 = findFailedRules1();
    if (mustSatisfyInUse) {
      consumeThenThrow(failedRules1);
    }
    if (couldSatisfyInUse && failedRules1.size() == otherPairList.size()) {
      consumeThenThrow(failedRules1);
    }
    return this.input;
  }

  private T validateWithCustomMessage() {
    var failedRules = findFailedRules();
    if (mustSatisfyInUse) {
      consumeThenThrow(failedRules, throwException());
    }
    if (couldSatisfyInUse && failedRules.size() == pairLst.size()) {
      consumeThenThrow(failedRules, throwException());
    }
    return this.input;
  }

  private T validateWithValidationExceptionCustomMessage() {
    var failedRules = findFailedRules();
    var allExceptionMessages = createAllExceptionMessage(failedRules);
    String exceptionMessage = format("For input: '%s', the following problems occurred: '%s'", input, allExceptionMessages);
    var validationException = new ExceptionAndInput<>(new ValidationException(exceptionMessage), exceptionMessage, input);
    if (mustSatisfyInUse && !failedRules.isEmpty()) {
      optionalConsumer.ifPresent(con -> con.accept(validationException));
      throw validationException.getException();
    }
    if (couldSatisfyInUse && failedRules.size() == pairLst.size()) {
      optionalConsumer.ifPresent(con -> con.accept(validationException));
      throw validationException.getException();
    }
    return this.input;
  }

  private T validateWithValidationExceptionHardcodedMessage() {
    var failedRules1 = findFailedRules1();
    var allExceptionMessages = createAllExceptionMessage1(failedRules1);
    String exceptionMessage = format("For input: '%s', the following problems occurred: '%s'", input, allExceptionMessages);
    var validationException = new ExceptionAndInput<>(new ValidationException(exceptionMessage), exceptionMessage, input);
    if (mustSatisfyInUse && !failedRules1.isEmpty()) {
      optionalConsumer.ifPresent(con -> con.accept(validationException));
      throw validationException.getException();
    }
    if (couldSatisfyInUse && failedRules1.size() == otherPairList.size()) {
      optionalConsumer.ifPresent(con -> con.accept(validationException));
      throw validationException.getException();
    }
    return this.input;
  }

  private T validateWithOverallCustomExceptionHardcodedMessage(Function<String, ? extends RuntimeException> exceptionFunction, BiFunction<T, String, String> message) {
    var failedRules1 = findFailedRules1();
    var allExceptionMessages = createAllExceptionMessage1(failedRules1);
    var validationException = new ExceptionAndInput<>(exceptionFunction.apply(allExceptionMessages), allExceptionMessages, input);

    if (mustSatisfyInUse && !failedRules1.isEmpty()) {
      optionalConsumer.ifPresent(con -> con.accept(validationException));
      customValidationExceptionMessage(exceptionFunction, message).accept(validationException.getMessage());
    }
    if (couldSatisfyInUse && failedRules1.size() == otherPairList.size()) {
      optionalConsumer.ifPresent(con -> con.accept(validationException));
      customValidationExceptionMessage(exceptionFunction, message).accept(validationException.getMessage());
    }
    return this.input;
  }

  private T validateWithOverallCustomExceptionCustomMessage(Function<String, ? extends RuntimeException> exceptionFunction, BiFunction<T, String, String> message) {
    var failedRules = findFailedRules();
    var allExceptionMessages = createAllExceptionMessage(failedRules);
    var validationException = new ExceptionAndInput<>(exceptionFunction.apply(allExceptionMessages), allExceptionMessages, input);

    if (mustSatisfyInUse && !failedRules.isEmpty()) {
      optionalConsumer.ifPresent(con -> con.accept(validationException));
      customValidationExceptionMessage(exceptionFunction, message).accept(validationException.getMessage());
    }
    if (couldSatisfyInUse && failedRules.size() == pairLst.size()) {
      optionalConsumer.ifPresent(con -> con.accept(validationException));
      customValidationExceptionMessage(exceptionFunction, message).accept(validationException.getMessage());
    }
    return this.input;
  }
}
