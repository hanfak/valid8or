package com.github.hanfak.valid8or.implmentation.compound;

import com.github.hanfak.valid8or.implmentation.domain.ExceptionAndInput;

import java.util.function.Consumer;
import java.util.function.Predicate;

public interface ConnectorOrValidateForCouldSatisfy<T> extends Terminal<T> {
  ThrowExceptionForCouldRule<T> orSatisfies(Predicate<T> predicate);

  ConsumerTerminal<T> thenConsume(Consumer<ExceptionAndInput<? extends RuntimeException, T>> consumer);
}