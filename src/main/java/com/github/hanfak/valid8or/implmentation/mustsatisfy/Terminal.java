package com.github.hanfak.valid8or.implmentation.mustsatisfy;

import java.util.Set;

public interface Terminal<T> extends ConsumerTerminal<T> {
  Set<String> allExceptionMessages();

  boolean isValid();

  boolean isInvalid();
}
