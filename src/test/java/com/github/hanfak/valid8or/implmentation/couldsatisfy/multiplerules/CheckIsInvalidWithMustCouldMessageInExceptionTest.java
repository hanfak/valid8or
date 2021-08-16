package com.github.hanfak.valid8or.implmentation.couldsatisfy.multiplerules;

import com.github.hanfak.valid8or.helper.PredicateStringRules;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import testinfrastructure.TestFixtures;

import static com.github.hanfak.valid8or.implmentation.Valid8orCouldSatisfyAllRules.forInput;

// No use of consumer or exception thrown
class CheckIsInvalidWithMustCouldMessageInExceptionTest extends TestFixtures {
  // TODO paramtise for true/false assertion
  @ParameterizedTest
  @ValueSource(ints = {4, 3, 2})
  void checksInputIsInvalidUsingCustomMessageOutsideException(int value) {
    assertThat(
        forInput(value)
            .couldSatisfy(isEven).orThrow(IllegalStateException::new)
            .withMessage(input -> "Is not even, for input: " + input)
            .orSatisfies(isGreaterThan2).orThrow(IllegalArgumentException::new)
            .withMessage(input -> "Is not greater than 2, for input: " + input)
            .isInvalid()
    ).isFalse();
    assertThat(
        forInput(value)
            .couldSatisfy(isGreaterThan2).orThrow(IllegalArgumentException::new)
            .withMessage(input -> "Is not greater than 2, for input: " + input)
            .orSatisfies(isEven).orThrow(IllegalStateException::new)
            .withMessage(input -> "Is not even, for input: " + input)
            .isInvalid()
    ).isFalse();

    assertThat(
        forInput(1)
            .couldSatisfy(isEven).orThrow(IllegalStateException::new)
            .withMessage(input -> "Is not even, for input: " + input)
            .orSatisfies(isGreaterThan2).orThrow(IllegalArgumentException::new)
            .withMessage(input -> "Is not greater than 2, for input: " + input)
            .isInvalid()
    ).isTrue();
    assertThat(
        forInput(1)
            .couldSatisfy(isGreaterThan2).orThrow(IllegalArgumentException::new)
            .withMessage(input -> "Is not greater than 2, for input: " + input)
            .orSatisfies(isEven).orThrow(IllegalStateException::new)
            .withMessage(input -> "Is not even, for input: " + input)
            .isInvalid()
    ).isTrue();
  }

  @ParameterizedTest
  @ValueSource(ints = {4, 3, 2})
  void checksInputIsInvalidUsingCustomMessageOnly(int value) {
    assertThat(
        forInput(value)
            .couldSatisfy(isEven)
            .butWas(input -> "Is not even, for input: " + input)
            .orSatisfies(isGreaterThan2)
            .butWas(input -> "Is not greater than 2, for input: " + input)
            .isInvalid()
    ).isFalse();
    assertThat(
        forInput(value)
            .couldSatisfy(isGreaterThan2)
            .butWas(input -> "Is not greater than 2, for input: " + input)
            .orSatisfies(isEven)
            .butWas(input -> "Is not even, for input: " + input)
            .isInvalid()
    ).isFalse();

    assertThat(
        forInput(1)
            .couldSatisfy(isEven)
            .butWas(input -> "Is not even, for input: " + input)
            .orSatisfies(isGreaterThan2)
            .butWas(input -> "Is not greater than 2, for input: " + input)
            .isInvalid()
    ).isTrue();
    assertThat(
        forInput(1)
            .couldSatisfy(isGreaterThan2)
            .butWas(input -> "Is not greater than 2, for input: " + input)
            .orSatisfies(isEven)
            .butWas(input -> "Is not even, for input: " + input)
            .isInvalid()
    ).isTrue();
  }
}
