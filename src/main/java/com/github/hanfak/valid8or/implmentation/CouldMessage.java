package com.github.hanfak.valid8or.implmentation;

import java.util.function.UnaryOperator;

public interface CouldMessage<T> {
  CouldConnectorOrValidate<T> withExceptionMessage(UnaryOperator<String> exceptionMessageFunction);
}
