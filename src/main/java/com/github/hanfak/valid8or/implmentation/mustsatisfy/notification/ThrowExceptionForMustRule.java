package com.github.hanfak.valid8or.implmentation.mustsatisfy.notification;

import java.util.function.Function;

public interface ThrowExceptionForMustRule<T> {
  MessageForMustRule<T> orThrow(Function<String, ? extends RuntimeException> exceptionFunction);

  ConnectorOrValidateForMustSatisfy<T> butWas(Function<String, String> message);
}
