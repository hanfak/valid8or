package com.github.hanfak.valid8or.implmentation.mustsatisfy;


public interface ForInput<T> {
  Satisfy<T> forInput(T input);
}