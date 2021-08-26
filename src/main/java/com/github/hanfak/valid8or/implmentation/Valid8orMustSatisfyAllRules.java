package com.github.hanfak.valid8or.implmentation;

public final class Valid8orMustSatisfyAllRules {

  private Valid8orMustSatisfyAllRules() {
  }

  public static <T> MustSatisfy<T> forInput(T input) {
    return new Valid8OrMustSatisfyAllRulesBuilderSatisfyAllRulesBuilderFlow<>(
        new ValidationRules<T>(),
        new ValidationLogic<>())
        .forInput(input);
  }
}
