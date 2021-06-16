package com.github.hanfak.valid8or.implmentation;

import com.github.hanfak.valid8or.implmentation.MustConnectorOrValidate;

import java.util.function.UnaryOperator;

public interface MustMessage<T> {
  MustConnectorOrValidate<T> withMessage(UnaryOperator<String> messageFunction);
}
