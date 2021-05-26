package com.github.hanfak.valid8or.implmentation.mustsatisfy.notification;

import java.util.function.Predicate;

public interface Satisfy<T> {
  ThrowExceptionForMustRule<T> mustSatisfy(Predicate<T> input);
}
