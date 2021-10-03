package com.github.hanfak.valid8or.implmentation;

import java.util.function.UnaryOperator;

public interface CouldMessage<T> {
  CouldConnectorOrValidate<T> withMessage(UnaryOperator<String> exceptionMessageFunction);
}
