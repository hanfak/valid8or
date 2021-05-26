package com.github.hanfak.valid8or.api;

import com.github.hanfak.valid8or.implmentation.mustsatisfy.Satisfy;
import com.github.hanfak.valid8or.implmentation.mustsatisfy.Valid8OrMustSatisfyAllRulesBuilder;

public class Valid8orMustSatisfyAllRules {

  private Valid8orMustSatisfyAllRules() {
  }

  public static <T> Satisfy<T> forInput(T input) {
    return new Valid8OrMustSatisfyAllRulesBuilder<T>().forInput(input);
  }
}
