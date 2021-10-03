package com.github.hanfak.valid8or.implmentation;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface ConsumerTerminal<T> {
  T isValidOrThrow();

  Optional<T> isValidReturnOptionalOrThrow();

  T throwNotificationIfNotValid();

  T throwNotificationIfNotValid(Function<String, ? extends RuntimeException> exceptionFunction,
                                BiFunction<T, String, String> exceptionMessageFunction);
}
