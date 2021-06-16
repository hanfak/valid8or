package com.github.hanfak.valid8or.implmentation;

import java.util.Optional;

public interface ConsumerTerminal<T> {
  T validate();

  Optional<T> validateThenReturnOptional();
}
