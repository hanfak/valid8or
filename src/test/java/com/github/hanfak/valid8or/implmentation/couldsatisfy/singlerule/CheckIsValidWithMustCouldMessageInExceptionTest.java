package com.github.hanfak.valid8or.implmentation.couldsatisfy.singlerule;

import org.junit.jupiter.api.Test;
import testinfrastructure.TestFixtures;

import static com.github.hanfak.valid8or.implmentation.Valid8orCouldSatisfyAllRules.forInput;

// No use of consumer or exception thrown
class CheckIsValidWithMustCouldMessageInExceptionTest extends TestFixtures {
  // TODO paramtise for true/false assertion

  @Test
  void checksInputIsValidUsingCustomMessageOutsideException() {
    assertThat(
        forInput(4)
            .couldSatisfy(isEven).orThrow(IllegalStateException::new)
            .withMessage(input -> "Is not even, for input: " + input)
            .isValid()
    ).isTrue();


    assertThat(
        forInput(3)
            .couldSatisfy(isEven).orThrow(IllegalStateException::new)
            .withMessage(input -> "Is not even, for input: " + input)
            .isValid()
    ).isFalse();
  }

  @Test
  void checksInputIsValidUsingCustomMessageOnly() {
    assertThat(
        forInput(4)
            .couldSatisfy(isEven)
            .butWas(input -> "Is not even, for input: " + input)
            .isValid()
    ).isTrue();

    assertThat(
        forInput(3)
            .couldSatisfy(isEven)
            .butWas(input -> "Is not even, for input: " + input)
            .isValid()
    ).isFalse();
  }
}
