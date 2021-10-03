package com.github.hanfak.valid8or.implmentation;

public final class Valid8orMustSatisfyAllRules {

  private Valid8orMustSatisfyAllRules() {
  }

  public static <T> MustSatisfy<T> forInput(T input) {
    var valid8orMustSatisfyAllRulesBuilder = new Valid8OrMustSatisfyAllRulesBuilder<>(
        new ValidationRules<T>(), new ValidationLogic<>());
    return valid8orMustSatisfyAllRulesBuilder.forInput(input);
  }
}
