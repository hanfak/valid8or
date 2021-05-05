package com.github.hanfak.valid8or.implmentation;

public interface ForInput<T> {
  Satisfy<T> forInput(T input);
}