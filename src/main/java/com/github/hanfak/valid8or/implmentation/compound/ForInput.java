package com.github.hanfak.valid8or.implmentation.compound;

public interface ForInput<T> {
  Satisfy<T> forInput(T input);
}