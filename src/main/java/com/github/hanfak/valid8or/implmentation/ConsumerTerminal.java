package com.github.hanfak.valid8or.implmentation;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface ConsumerTerminal<T> {
  // TODO: change name
  T validateOrThrowNotify();

  // TODO: change name
  T validateOrThrowNotify(Function<String, ? extends RuntimeException> exceptionFunction,
                          BiFunction<T, String, String> message);

  // TODO : Do the above 2 methods and return Optional

  T validate();

  Optional<T> validateThenReturnOptional();
}