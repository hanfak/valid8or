package com.github.hanfak.valid8or.implmentation;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface ConsumerTerminal<T> {
  T throwNotificationIfNotValid();

  T throwNotificationIfNotValid(Function<String, ? extends RuntimeException> exceptionFunction,
                                BiFunction<T, String, String> exceptionMessageFunction);

  T throwIfNotValid();

  Optional<T> throwIfNotValidOrReturnOptional();
}
