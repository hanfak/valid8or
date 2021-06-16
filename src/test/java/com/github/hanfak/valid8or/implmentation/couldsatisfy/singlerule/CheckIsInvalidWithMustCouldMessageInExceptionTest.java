package com.github.hanfak.valid8or.implmentation.couldsatisfy.singlerule;

import org.junit.jupiter.api.Test;
import testinfrastructure.TestFixtures;

import static com.github.hanfak.valid8or.implmentation.Valid8orCouldSatisfyAllRules.forInput;

// No use of consumer or exception thrown
class CheckIsInvalidWithMustCouldMessageInExceptionTest extends TestFixtures {
  // TODO paramtise for true/false assertion
  @Test
  void checksInputIsInvalidUsingCustomMessageOutsideException() {
    assertThat(
        forInput(4)
            .couldSatisfy(isEven).orThrow(IllegalStateException::new)
            .withMessage(input -> "Is not even, for input: " + input)
            .isInvalid()
    ).isFalse();

    assertThat(
        forInput(3)
            .couldSatisfy(isEven).orThrow(IllegalStateException::new)
            .withMessage(input -> "Is not even, for input: " + input)
            .isInvalid()
    ).isTrue();
  }

  @Test
  void checksInputIsInvalidUsingCustomMessageOnly() {
    assertThat(
        forInput(4)
            .couldSatisfy(isEven)
            .butWas(input -> "Is not even, for input: " + input)
            .isInvalid()
    ).isFalse();

    assertThat(
        forInput(3)
            .couldSatisfy(isEven)
            .butWas(input -> "Is not even, for input: " + input)
            .isInvalid()
    ).isTrue();
  }
}
