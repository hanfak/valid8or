package com.github.hanfak.valid8or.implmentation;

public final class Valid8orCouldSatisfyAllRules {

  private Valid8orCouldSatisfyAllRules() {
  }

  public static <T> CouldSatisfy<T> forInput(T input) {
    return new Valid8OrCouldCouldSatisfyAllRulesBuilderCould<>(
        new ValidationRules<T>(),
        new ValidationLogic<>(new Utils<>()))
        .forInput(input);
  }
}
