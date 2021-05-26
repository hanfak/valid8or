package com.github.hanfak.valid8or.implmentation.mustsatisfy;

import java.util.function.Function;

public interface Message<T> {
  ConnectorOrValidate<T> withMessage(Function<String, String> messageFunction);
}
