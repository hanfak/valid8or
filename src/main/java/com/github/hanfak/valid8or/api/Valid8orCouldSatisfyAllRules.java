package com.github.hanfak.valid8or.api;

import com.github.hanfak.valid8or.implmentation.couldsatisfy.Satisfy;
import com.github.hanfak.valid8or.implmentation.couldsatisfy.Valid8OrCouldSatisfyAllRulesBuilder;
import com.github.hanfak.valid8or.implmentation.domain.Rules;

public class Valid8orCouldSatisfyAllRules {

  private Valid8orCouldSatisfyAllRules() {
  }

  public static <T> Satisfy<T> forInput(T input) {
    return new Valid8OrCouldSatisfyAllRulesBuilder<T>(new Rules<T>()).forInput(input);
  }
}
