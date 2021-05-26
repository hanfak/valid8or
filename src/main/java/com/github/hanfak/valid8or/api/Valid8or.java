package com.github.hanfak.valid8or.api;

import com.github.hanfak.valid8or.implmentation.compound.Satisfy;
import com.github.hanfak.valid8or.implmentation.compound.Valid8orBuilder;

public final class Valid8or {

  private Valid8or() {
  }

  public static <T> Satisfy<T> forInput(T input) {
    return new Valid8orBuilder<T>().forInput(input);
  }
}
