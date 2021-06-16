package com.github.hanfak.valid8or.implmentation;

import java.util.function.Function;
import java.util.function.UnaryOperator;

public interface CouldThrowException<T> {
  CouldMessage<T> orThrow(Function<String, ? extends RuntimeException> exceptionFunction);

  CouldConnectorOrValidate<T> butWas(UnaryOperator<String> messageFunction);
}
