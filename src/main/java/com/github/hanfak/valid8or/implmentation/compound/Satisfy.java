package com.github.hanfak.valid8or.implmentation.compound;

import java.util.function.Predicate;

public interface Satisfy<T> {
  ThrowExceptionForCouldRule<T> couldSatisfy(Predicate<T> input);

  ThrowExceptionForMustRule<T> mustSatisfy(Predicate<T> input);
}
