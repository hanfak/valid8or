package com.github.hanfak.valid8or.implmentation.couldsatisfy.singlerule;

import org.junit.jupiter.api.Test;
import testinfrastructure.TestFixtures;

import static com.github.hanfak.valid8or.implmentation.Valid8orCouldSatisfyAllRules.forInput;

class CheckIsValidWithMustCouldMessageInExceptionTest extends TestFixtures {

  @Test
  void checksInputIsValidUsingCustomMessageOutsideException() {
    assertThat(
        forInput(4)
            .couldSatisfy(isEven).orElseThrow(IllegalStateException::new)
            .withMessage(input -> "Is not even, for input: " + input)
            .isValid()
    ).isTrue();


    assertThat(
        forInput(3)
            .couldSatisfy(isEven).orElseThrow(IllegalStateException::new)
            .withMessage(input -> "Is not even, for input: " + input)
            .isValid()
    ).isFalse();
  }

  @Test
  void checksInputIsValidUsingCustomMessageOnly() {
    assertThat(
        forInput(4)
            .couldSatisfy(isEven)
            .orThrowExceptionWith(input -> "Is not even, for input: " + input)
            .isValid()
    ).isTrue();

    assertThat(
        forInput(3)
            .couldSatisfy(isEven)
            .orThrowExceptionWith(input -> "Is not even, for input: " + input)
            .isValid()
    ).isFalse();
  }
}
