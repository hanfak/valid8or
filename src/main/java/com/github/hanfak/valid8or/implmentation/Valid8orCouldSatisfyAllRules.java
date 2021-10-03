package com.github.hanfak.valid8or.implmentation;

public final class Valid8orCouldSatisfyAllRules {

  private Valid8orCouldSatisfyAllRules() {
  }

  public static <T> CouldSatisfy<T> forInput(T input) {
    var valid8orCouldSatisfyAllRulesBuilder = new Valid8OrCouldSatisfyAllRulesBuilder<>(
        new ValidationRules<T>(), new ValidationLogic<>());
    return valid8orCouldSatisfyAllRulesBuilder
        .forInput(input);
  }
}
