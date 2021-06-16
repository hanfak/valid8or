package com.github.hanfak.valid8or.implmentation;

import java.util.function.Predicate;

public interface CouldSatisfy<T> {
  CouldThrowException<T> couldSatisfy(Predicate<T> input);
}
