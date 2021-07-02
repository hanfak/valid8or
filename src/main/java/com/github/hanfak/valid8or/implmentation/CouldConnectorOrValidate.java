package com.github.hanfak.valid8or.implmentation;

import java.util.function.Consumer;
import java.util.function.Predicate;

public interface CouldConnectorOrValidate<T> extends Terminal<T> {
  CouldThrowException<T> orSatisfies(Predicate<T> predicateRule);

  ConsumerTerminal<T> thenConsume(Consumer<ExceptionAndInput<? extends RuntimeException, T>> consumer);
}