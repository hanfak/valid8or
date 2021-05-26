package com.github.hanfak.valid8or.implmentation.mustsatisfy.notification;

import com.github.hanfak.valid8or.implmentation.domain.ExceptionAndInput;

import java.util.function.Consumer;
import java.util.function.Predicate;

public interface ConnectorOrValidateForMustSatisfy<T> extends Terminal<T> {
  ThrowExceptionForMustRule<T> andSatisfies(Predicate<T> predicate);

  ConsumerTerminal<T> thenConsume(Consumer<ExceptionAndInput<? extends RuntimeException, T>> consumer);
}
