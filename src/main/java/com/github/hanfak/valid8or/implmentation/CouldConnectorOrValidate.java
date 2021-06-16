package com.github.hanfak.valid8or.implmentation;

import java.util.function.Consumer;
import java.util.function.Predicate;

public interface CouldConnectorOrValidate<T> extends Terminal<T> {
  CouldThrowException<T> orSatisfies(Predicate<T> predicate);

  ConsumerTerminal<T> thenConsume(Consumer<ExceptionAndInput<? extends RuntimeException, T>> consumer);
}