package com.github.hanfak.valid8or.implmentation;

import java.util.function.Consumer;
import java.util.function.Predicate;

public interface MustConnectorOrValidate<T> extends Terminal<T> {
  MustThrowException<T> and(Predicate<T> predicateRule);

  ConsumerTerminal<T> useConsumer(Consumer<ExceptionAndInput<? extends RuntimeException, T>> consumer);
}
