package com.github.hanfak.valid8or.implmentation;

import java.util.function.UnaryOperator;

public interface MustMessage<T> {
  MustConnectorOrValidate<T> withExceptionMessage(UnaryOperator<String> exceptionMessageFunction);
}
