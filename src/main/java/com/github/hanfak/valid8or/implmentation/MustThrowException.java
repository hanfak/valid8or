package com.github.hanfak.valid8or.implmentation;

import java.util.function.Function;
import java.util.function.UnaryOperator;

public interface MustThrowException<T> {
  MustMessage<T> orThrow(Function<String, ? extends RuntimeException> exceptionFunction);

  MustConnectorOrValidate<T> butWas(UnaryOperator<String> messageFunction);
}
