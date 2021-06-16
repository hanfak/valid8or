package com.github.hanfak.valid8or.implmentation;

import java.util.function.Consumer;
import java.util.function.Predicate;

public interface MustConnectorOrValidate<T> extends Terminal<T> {
  MustThrowException<T> andSatisfies(Predicate<T> predicate);

  ConsumerTerminal<T> thenConsume(Consumer<ExceptionAndInput<? extends RuntimeException, T>> consumer);
}
