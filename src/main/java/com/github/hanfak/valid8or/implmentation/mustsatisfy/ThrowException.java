package com.github.hanfak.valid8or.implmentation.mustsatisfy;

import java.util.function.Function;

public interface ThrowException<T> {
  Message<T> orThrow(Function<String, ? extends RuntimeException> exceptionFunction);

  ConnectorOrValidate<T> butWas(Function<String, String> messageFunction);
}
