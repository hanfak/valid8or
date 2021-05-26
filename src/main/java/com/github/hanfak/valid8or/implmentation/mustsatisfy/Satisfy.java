package com.github.hanfak.valid8or.implmentation.mustsatisfy;

import java.util.function.Predicate;

public interface Satisfy<T> {
  ThrowException<T> mustSatisfy(Predicate<T> input);
}
