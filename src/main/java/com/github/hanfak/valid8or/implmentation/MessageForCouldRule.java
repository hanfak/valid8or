package com.github.hanfak.valid8or.implmentation;

import java.util.function.Function;

public interface MessageForCouldRule<T> {
  ConnectorOrValidateForCouldSatisfy<T> withMessage(Function<String, String> message);
}
