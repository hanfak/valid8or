package com.github.hanfak.valid8or.implmentation;

public final class Valid8orMustSatisfyAllRules {

  private Valid8orMustSatisfyAllRules() {
  }

  public static <T> MustSatisfy<T> forInput(T input) {
    return new Valid8OrMustMustSatisfyAllRulesBuilderMust<>(
        new ValidationRules<T>(),
        new ValidationLogic<>(new Utils<>()))
        .forInput(input);
  }
}
