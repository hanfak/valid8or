package com.github.hanfak.valid8or.implmentation;

import java.util.function.Consumer;
import java.util.function.Predicate;

public interface CouldConnectorOrValidate<T> extends Terminal<T> {
  CouldThrowException<T> or(Predicate<T> predicateRule);

  Terminal<T> useConsumer(Consumer<ExceptionAndInput<? extends RuntimeException, T>> consumer);
}