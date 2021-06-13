package com.github.hanfak.valid8or.api;

import com.github.hanfak.valid8or.implmentation.couldsatisfy.Satisfy;
import com.github.hanfak.valid8or.implmentation.couldsatisfy.Valid8OrCouldSatisfyAllRulesBuilder;

public class Valid8orCouldSatisfyAllRules {

  private Valid8orCouldSatisfyAllRules() {
  }

  public static <T> Satisfy<T> forInput(T input) {
    return new Valid8OrCouldSatisfyAllRulesBuilder<T>().forInput(input);
  }
}
