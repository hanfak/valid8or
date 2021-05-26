package com.github.hanfak.valid8or.implmentation.compound;

import java.util.function.Function;

public interface MessageForMustRule<T> {
  ConnectorOrValidateForMustSatisfy<T> hasMessage(Function<String, String> message);
}
