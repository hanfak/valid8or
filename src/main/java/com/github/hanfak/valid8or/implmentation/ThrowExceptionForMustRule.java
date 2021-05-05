package com.github.hanfak.valid8or.implmentation;

import java.util.function.Function;
import java.util.function.Supplier;

public interface ThrowExceptionForMustRule<T> {
  MessageForMustRule<T> ifNotWillThrow(Function<String, ? extends RuntimeException> exceptionFunction);

  ConnectorOrValidateForMustSatisfy<T> ifNotWillThrowAn(Supplier<? extends RuntimeException> exception);

  ConnectorOrValidateForMustSatisfy<T> butIs(Function<String, String> message);
}
