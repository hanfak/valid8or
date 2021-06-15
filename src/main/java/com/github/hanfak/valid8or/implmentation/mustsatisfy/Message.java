package com.github.hanfak.valid8or.implmentation.mustsatisfy;

import java.util.function.UnaryOperator;

public interface Message<T> {
  ConnectorOrValidate<T> withMessage(UnaryOperator<String> messageFunction);
}
