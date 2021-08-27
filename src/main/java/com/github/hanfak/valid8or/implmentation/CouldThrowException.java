package com.github.hanfak.valid8or.implmentation;

import java.util.function.Function;
import java.util.function.UnaryOperator;

public interface CouldThrowException<T> {
  CouldMessage<T> orElseThrow(Function<String, ? extends RuntimeException> exceptionFunction);

  CouldConnectorOrValidate<T> orThrowExceptionWith(UnaryOperator<String> exceptionMessageFunction);
}
