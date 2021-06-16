package com.github.hanfak.valid8or.implmentation;

public interface ForCouldInput<T> {
  CouldSatisfy<T> forInput(T input);
}