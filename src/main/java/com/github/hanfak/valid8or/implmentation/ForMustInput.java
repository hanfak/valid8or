package com.github.hanfak.valid8or.implmentation;


public interface ForMustInput<T> {
  MustSatisfy<T> forInput(T input);
}