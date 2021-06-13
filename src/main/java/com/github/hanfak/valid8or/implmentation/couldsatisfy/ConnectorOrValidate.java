package com.github.hanfak.valid8or.implmentation.couldsatisfy;

import com.github.hanfak.valid8or.implmentation.domain.ExceptionAndInput;

import java.util.function.Consumer;
import java.util.function.Predicate;

public interface ConnectorOrValidate<T> extends Terminal<T> {
  ThrowException<T> orSatisfies(Predicate<T> predicate);

  ConsumerTerminal<T> thenConsume(Consumer<ExceptionAndInput<? extends RuntimeException, T>> consumer);
}