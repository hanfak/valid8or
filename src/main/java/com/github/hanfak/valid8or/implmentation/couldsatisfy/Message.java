package com.github.hanfak.valid8or.implmentation.couldsatisfy;

import java.util.function.Function;
import java.util.function.UnaryOperator;

public interface Message<T> {
  ConnectorOrValidate<T> withMessage(UnaryOperator<String> messageFunction);
}
