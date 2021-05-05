package com.github.hanfak.valid8or.implmentation;

import java.util.function.Function;

public interface MessageForMustRule<T> {
  ConnectorOrValidateForMustSatisfy<T> hasMessage(Function<String, String> message);
}
