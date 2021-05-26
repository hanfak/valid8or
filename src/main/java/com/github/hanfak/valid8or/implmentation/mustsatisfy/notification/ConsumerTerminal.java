package com.github.hanfak.valid8or.implmentation.mustsatisfy.notification;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface ConsumerTerminal<T> {
  T orElseThrowValidationException(Function<String, ? extends RuntimeException> exceptionFunction, BiFunction<T, String, String> message);

  T orElseThrowValidationException();

  T validate();

  Optional<T> validateThenReturnOptional();
}
