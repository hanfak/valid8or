package com.github.hanfak.valid8or.implmentation.mustsatisfy.notification;

import java.util.function.Function;

public interface MessageForMustRule<T> {
  ConnectorOrValidateForMustSatisfy<T> withMessage(Function<String, String> message);
}
