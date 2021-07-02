package com.github.hanfak.valid8or.implmentation;

import java.util.function.Predicate;

public interface MustSatisfy<T> {
  MustThrowException<T> mustSatisfy(Predicate<T> predicateRule);
}
