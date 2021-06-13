package com.github.hanfak.valid8or.implmentation.couldsatisfy.notification;

import java.util.Set;

public interface Terminal<T> extends ConsumerTerminal<T> {
  Set<String> allExceptionMessages();

  boolean isValid();

  boolean isInvalid();
}
