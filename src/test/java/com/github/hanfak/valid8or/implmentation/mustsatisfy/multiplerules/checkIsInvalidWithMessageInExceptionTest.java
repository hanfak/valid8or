package com.github.hanfak.valid8or.implmentation.mustsatisfy.multiplerules;

import org.junit.jupiter.api.Test;
import testinfrastructure.TestFixtures;

import static com.github.hanfak.valid8or.api.Valid8orMustSatisfyAllRules.forInput;

// No use of consumer or exception thrown
public class checkIsInvalidWithMessageInExceptionTest extends TestFixtures {
  // TODO paramtise for true/false assertion
  // TODO separate tests with in tests
  @Test
  void checksInputIsInvalidUsingCustomMessageOutsideException() {
    assertThat(
        forInput(4)
            .mustSatisfy(isEven).orThrow(IllegalStateException::new)
            .withMessage(input -> "Is not even, for input: " + input)
            .andSatisfies(isGreaterThan2).orThrow(IllegalArgumentException::new)
            .withMessage(input -> "Is not greater than 2, for input: " + input)
            .isInvalid()
    ).isFalse();
    assertThat(
        forInput(4)
            .mustSatisfy(isGreaterThan2).orThrow(IllegalArgumentException::new)
            .withMessage(input -> "Is not greater than 2, for input: " + input)
            .andSatisfies(isEven).orThrow(IllegalStateException::new)
            .withMessage(input -> "Is not even, for input: " + input)
            .isInvalid()
    ).isFalse();

    assertThat(
        forInput(1)
            .mustSatisfy(isEven).orThrow(IllegalStateException::new)
            .withMessage(input -> "Is not even, for input: " + input)
            .andSatisfies(isGreaterThan2).orThrow(IllegalArgumentException::new)
            .withMessage(input -> "Is not greater than 2, for input: " + input)
            .isInvalid()
    ).isTrue();
    assertThat(
        forInput(1)
            .mustSatisfy(isGreaterThan2).orThrow(IllegalArgumentException::new)
            .withMessage(input -> "Is not greater than 2, for input: " + input)
            .andSatisfies(isEven).orThrow(IllegalStateException::new)
            .withMessage(input -> "Is not even, for input: " + input)
            .isInvalid()
    ).isTrue();
  }

  @Test
  void checksInputIsInvalidUsingCustomMessageOnly() {
    assertThat(
        forInput(4)
            .mustSatisfy(isEven)
            .butWas(input -> "Is not even, for input: " + input)
            .andSatisfies(isGreaterThan2)
            .butWas(input -> "Is not greater than 2, for input: " + input)
            .isInvalid()
    ).isFalse();
    assertThat(
        forInput(4)
            .mustSatisfy(isGreaterThan2)
            .butWas(input -> "Is not greater than 2, for input: " + input)
            .andSatisfies(isEven)
            .butWas(input -> "Is not even, for input: " + input)
            .isInvalid()
    ).isFalse();

    assertThat(
        forInput(1)
            .mustSatisfy(isEven)
            .butWas(input -> "Is not even, for input: " + input)
            .andSatisfies(isGreaterThan2)
            .butWas(input -> "Is not greater than 2, for input: " + input)
            .isInvalid()
    ).isTrue();
    assertThat(
        forInput(1)
            .mustSatisfy(isGreaterThan2)
            .butWas(input -> "Is not greater than 2, for input: " + input)
            .andSatisfies(isEven)
            .butWas(input -> "Is not even, for input: " + input)
            .isInvalid()
    ).isTrue();
  }
}
