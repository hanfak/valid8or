package com.github.hanfak.valid8or.implmentation.couldsatisfy.multiplerules;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import testinfrastructure.TestFixtures;

import static com.github.hanfak.valid8or.implmentation.Valid8orCouldSatisfyAllRules.forInput;

class CheckIsInvalidWithMustCouldMessageInExceptionTest extends TestFixtures {

  @ParameterizedTest
  @ValueSource(ints = {4, 3, 2})
  void checksInputIsInvalidUsingCustomMessageOutsideException(int value) {
    assertThat(
        forInput(value)
            .couldSatisfy(isEven).orElseThrow(IllegalStateException::new)
            .withExceptionMessage(input -> "Is not even, for input: " + input)
            .or(isGreaterThan2).orElseThrow(IllegalArgumentException::new)
            .withExceptionMessage(input -> "Is not greater than 2, for input: " + input)
            .isNotValid()
    ).isFalse();
    assertThat(
        forInput(value)
            .couldSatisfy(isGreaterThan2).orElseThrow(IllegalArgumentException::new)
            .withExceptionMessage(input -> "Is not greater than 2, for input: " + input)
            .or(isEven).orElseThrow(IllegalStateException::new)
            .withExceptionMessage(input -> "Is not even, for input: " + input)
            .isNotValid()
    ).isFalse();

    assertThat(
        forInput(1)
            .couldSatisfy(isEven).orElseThrow(IllegalStateException::new)
            .withExceptionMessage(input -> "Is not even, for input: " + input)
            .or(isGreaterThan2).orElseThrow(IllegalArgumentException::new)
            .withExceptionMessage(input -> "Is not greater than 2, for input: " + input)
            .isNotValid()
    ).isTrue();
    assertThat(
        forInput(1)
            .couldSatisfy(isGreaterThan2).orElseThrow(IllegalArgumentException::new)
            .withExceptionMessage(input -> "Is not greater than 2, for input: " + input)
            .or(isEven).orElseThrow(IllegalStateException::new)
            .withExceptionMessage(input -> "Is not even, for input: " + input)
            .isNotValid()
    ).isTrue();
  }

  @ParameterizedTest
  @ValueSource(ints = {4, 3, 2})
  void checksInputIsInvalidUsingCustomMessageOnly(int value) {
    assertThat(
        forInput(value)
            .couldSatisfy(isEven)
            .orThrowExceptionWith(input -> "Is not even, for input: " + input)
            .or(isGreaterThan2)
            .orThrowExceptionWith(input -> "Is not greater than 2, for input: " + input)
            .isNotValid()
    ).isFalse();
    assertThat(
        forInput(value)
            .couldSatisfy(isGreaterThan2)
            .orThrowExceptionWith(input -> "Is not greater than 2, for input: " + input)
            .or(isEven)
            .orThrowExceptionWith(input -> "Is not even, for input: " + input)
            .isNotValid()
    ).isFalse();

    assertThat(
        forInput(1)
            .couldSatisfy(isEven)
            .orThrowExceptionWith(input -> "Is not even, for input: " + input)
            .or(isGreaterThan2)
            .orThrowExceptionWith(input -> "Is not greater than 2, for input: " + input)
            .isNotValid()
    ).isTrue();
    assertThat(
        forInput(1)
            .couldSatisfy(isGreaterThan2)
            .orThrowExceptionWith(input -> "Is not greater than 2, for input: " + input)
            .or(isEven)
            .orThrowExceptionWith(input -> "Is not even, for input: " + input)
            .isNotValid()
    ).isTrue();
  }
}
