package com.github.hanfak.valid8or.implmentation.mustsatisfy;

import java.util.function.Function;
import java.util.function.UnaryOperator;

public interface ThrowException<T> {
  Message<T> orThrow(Function<String, ? extends RuntimeException> exceptionFunction);

  ConnectorOrValidate<T> butWas(UnaryOperator<String> messageFunction);
}
