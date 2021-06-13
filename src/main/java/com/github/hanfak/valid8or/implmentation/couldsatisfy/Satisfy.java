package com.github.hanfak.valid8or.implmentation.couldsatisfy;

import java.util.function.Predicate;

public interface Satisfy<T> {
  ThrowException<T> couldSatisfy(Predicate<T> input);
}
