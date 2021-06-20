package com.github.hanfak.valid8or.implmentation;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface ConsumerTerminal<T> {
  T validateOrThrowNotify(); // TODO: change name

  // TODO: change name
  T validateOrThrowNotify(Function<String, ? extends RuntimeException> exceptionFunction,
                          BiFunction<T, String, String> message);

  T validate();

  Optional<T> validateThenReturnOptional();
}
