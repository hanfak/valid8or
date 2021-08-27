package com.github.hanfak.valid8or.implmentation;

import java.util.function.Function;
import java.util.function.UnaryOperator;

public interface MustThrowException<T> {
  MustMessage<T> orElseThrow(Function<String, ? extends RuntimeException> exceptionFunction);

  MustConnectorOrValidate<T> orThrowExceptionWith(UnaryOperator<String> exceptionMessageFunction);
}
