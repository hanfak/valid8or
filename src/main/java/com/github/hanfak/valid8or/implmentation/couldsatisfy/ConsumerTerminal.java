package com.github.hanfak.valid8or.implmentation.couldsatisfy;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface ConsumerTerminal<T> {
  T validate();

  Optional<T> validateThenReturnOptional();
}
