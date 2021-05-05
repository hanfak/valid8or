package com.github.hanfak.valid8or.implmentation.couldsatisfy.multiplerule;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import testinfrastructure.TestFixtures;

import static com.github.hanfak.valid8or.api.Valid8or.forInput;

// No use of consumer or exception thrown, this is up to the user
public class checkIsInvalidWithMessageInExceptionTest extends TestFixtures {
  // TODO paramtise for true/false assertion
  @ParameterizedTest
  @ValueSource(ints = {4, 3, 2})
  void checksInputIsInvalidUsingExceptionWithoutMessage(int value) {
    assertThat(
        forInput(value)
            .couldSatisfy(isEven).ifNotThrowAn(IllegalStateException::new)
            .orSatisfies(isGreaterThan2).ifNotThrowAn(IllegalArgumentException::new)
            .isInvalid()
    ).isFalse();
    assertThat(
        forInput(value)
            .couldSatisfy(isGreaterThan2).ifNotThrowAn(IllegalArgumentException::new)
            .orSatisfies(isEven).ifNotThrowAn(IllegalStateException::new)
            .isInvalid()
    ).isFalse();

    assertThat(
        forInput(1)
            .couldSatisfy(isEven).ifNotThrowAn(IllegalStateException::new)
            .orSatisfies(isGreaterThan2).ifNotThrowAn(IllegalArgumentException::new)
            .isInvalid()
    ).isTrue();
    assertThat(
        forInput(1)
            .couldSatisfy(isGreaterThan2).ifNotThrowAn(IllegalArgumentException::new)
            .orSatisfies(isEven).ifNotThrowAn(IllegalStateException::new)
            .isInvalid()
    ).isTrue();
  }

  @ParameterizedTest
  @ValueSource(ints = {4, 3, 2})
  void checksInputIsInvalidUsingCustomMessageInException(int value) {
    assertThat(
        forInput(value)
            .couldSatisfy(isEven).ifNotThrowAn(() -> new IllegalStateException("Some Exception"))
            .orSatisfies(isGreaterThan2).ifNotThrowAn(() -> new IllegalArgumentException("Some Other Exception"))
            .isInvalid()
    ).isFalse();
    assertThat(
        forInput(value)
            .couldSatisfy(isGreaterThan2).ifNotThrowAn(() -> new IllegalArgumentException("Some Other Exception"))
            .orSatisfies(isEven).ifNotThrowAn(() -> new IllegalStateException("Some Exception"))
            .isInvalid()
    ).isFalse();

    assertThat(
        forInput(1)
            .couldSatisfy(isEven).ifNotThrowAn(() -> new IllegalStateException("Some Exception"))
            .orSatisfies(isGreaterThan2).ifNotThrowAn(() -> new IllegalArgumentException("Some Other Exception"))
            .isInvalid()
    ).isTrue();
    assertThat(
        forInput(1)
            .couldSatisfy(isGreaterThan2).ifNotThrowAn(() -> new IllegalArgumentException("Some Other Exception"))
            .orSatisfies(isEven).ifNotThrowAn(() -> new IllegalStateException("Some Exception"))
            .isInvalid()
    ).isTrue();
  }

  @ParameterizedTest
  @ValueSource(ints = {4, 3, 2})
  void checksInputIsInvalidUsingCustomMessageOutsideException(int value) {
    assertThat(
        forInput(value)
            .couldSatisfy(isEven).ifNotThrow(IllegalStateException::new)
            .withMessage(input -> "Is not even, for input: " + input)
            .orSatisfies(isGreaterThan2).ifNotThrow(IllegalArgumentException::new)
            .withMessage(input -> "Is not greater than 2, for input: " + input)
            .isInvalid()
    ).isFalse();
    assertThat(
        forInput(value)
            .couldSatisfy(isGreaterThan2).ifNotThrow(IllegalArgumentException::new)
            .withMessage(input -> "Is not greater than 2, for input: " + input)
            .orSatisfies(isEven).ifNotThrow(IllegalStateException::new)
            .withMessage(input -> "Is not even, for input: " + input)
            .isInvalid()
    ).isFalse();

    assertThat(
        forInput(1)
            .couldSatisfy(isEven).ifNotThrow(IllegalStateException::new)
            .withMessage(input -> "Is not even, for input: " + input)
            .orSatisfies(isGreaterThan2).ifNotThrow(IllegalArgumentException::new)
            .withMessage(input -> "Is not greater than 2, for input: " + input)
            .isInvalid()
    ).isTrue();
    assertThat(
        forInput(1)
            .couldSatisfy(isGreaterThan2).ifNotThrow(IllegalArgumentException::new)
            .withMessage(input -> "Is not greater than 2, for input: " + input)
            .orSatisfies(isEven).ifNotThrow(IllegalStateException::new)
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
