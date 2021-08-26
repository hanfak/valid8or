package com.github.hanfak.valid8or.implmentation;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface ConsumerTerminal<T> {
  T throwNotificationIfNotValid(); //validateUsingNotification

  T throwNotificationIfNotValid(Function<String, ? extends RuntimeException> exceptionFunction,
                                BiFunction<T, String, String> exceptionMessageFunction);

  T throwIfNotValid(); //validate, toBeValid

  Optional<T> throwIfNotValidReturnOptional(); //validateReturnOptional, toBeValidOptional
}
