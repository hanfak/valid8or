package com.github.hanfak.valid8or.implmentation.mustsatisfy.notification;


public interface ForInput<T> {
  Satisfy<T> forInput(T input);
}