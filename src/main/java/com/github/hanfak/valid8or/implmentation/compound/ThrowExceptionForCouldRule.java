package com.github.hanfak.valid8or.implmentation.compound;

import java.util.function.Function;
import java.util.function.Supplier;

public interface ThrowExceptionForCouldRule<T> {
  MessageForCouldRule<T> ifNotThrow(Function<String, ? extends RuntimeException> exceptionFunction);

  ConnectorOrValidateForCouldSatisfy<T> ifNotThrowAn(Supplier<? extends RuntimeException> exception);

  ConnectorOrValidateForCouldSatisfy<T> butWas(Function<String, String> message);
}
