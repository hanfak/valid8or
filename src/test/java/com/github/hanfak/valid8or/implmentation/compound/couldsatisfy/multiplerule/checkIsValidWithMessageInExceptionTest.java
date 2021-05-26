package com.github.hanfak.valid8or.implmentation.compound.couldsatisfy.multiplerule;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import testinfrastructure.TestFixtures;

import static com.github.hanfak.valid8or.api.Valid8or.forInput;

// No use of consumer or exception thrown, this is up to the user
public class checkIsValidWithMessageInExceptionTest extends TestFixtures {
  // TODO paramtise for true/false assertion
  @ParameterizedTest
  @ValueSource(ints = {4, 3, 2})
  void checksInputIsValidUsingExceptionWithoutMessage(int value) {
    assertThat(
        forInput(value)
            .couldSatisfy(isEven).ifNotThrowAn(IllegalStateException::new)
            .orSatisfies(isGreaterThan2).ifNotThrowAn(IllegalArgumentException::new)
            .isValid()
    ).isTrue();
    assertThat(
        forInput(value)
            .couldSatisfy(isGreaterThan2).ifNotThrowAn(IllegalArgumentException::new)
            .orSatisfies(isEven).ifNotThrowAn(IllegalStateException::new)
            .isValid()
    ).isTrue();

    assertThat(
        forInput(1)
            .couldSatisfy(isEven).ifNotThrowAn(IllegalStateException::new)
            .orSatisfies(isGreaterThan2).ifNotThrowAn(IllegalArgumentException::new)
            .isValid()
    ).isFalse();
    assertThat(
        forInput(1)
            .couldSatisfy(isGreaterThan2).ifNotThrowAn(IllegalArgumentException::new)
            .orSatisfies(isEven).ifNotThrowAn(IllegalStateException::new)
            .isValid()
    ).isFalse();
  }

  @ParameterizedTest
  @ValueSource(ints = {4, 3, 2})
  void checksInputIsValidUsingCustomMessageInException(int value) {
    assertThat(
        forInput(value)
            .couldSatisfy(isEven).ifNotThrowAn(() -> new IllegalStateException("Some Exception"))
            .orSatisfies(isGreaterThan2).ifNotThrowAn(() -> new IllegalArgumentException("Some Other Exception"))
            .isValid()
    ).isTrue();
    assertThat(
        forInput(value)
            .couldSatisfy(isGreaterThan2).ifNotThrowAn(() -> new IllegalArgumentException("Some Other Exception"))
            .orSatisfies(isEven).ifNotThrowAn(() -> new IllegalStateException("Some Exception"))
            .isValid()
    ).isTrue();

    assertThat(
        forInput(1)
            .couldSatisfy(isEven).ifNotThrowAn(() -> new IllegalStateException("Some Exception"))
            .orSatisfies(isGreaterThan2).ifNotThrowAn(() -> new IllegalArgumentException("Some Other Exception"))
            .isValid()
    ).isFalse();
    assertThat(
        forInput(1)
            .couldSatisfy(isGreaterThan2).ifNotThrowAn(() -> new IllegalArgumentException("Some Other Exception"))
            .orSatisfies(isEven).ifNotThrowAn(() -> new IllegalStateException("Some Exception"))
            .isValid()
    ).isFalse();
  }

  @ParameterizedTest
  @ValueSource(ints = {4, 3, 2})
  void checksInputIsValidUsingCustomMessageOutsideException(int value) {
    assertThat(
        forInput(value)
            .couldSatisfy(isEven).ifNotThrow(IllegalStateException::new)
            .withMessage(input -> "Is not even, for input: " + input)
            .orSatisfies(isGreaterThan2).ifNotThrow(IllegalArgumentException::new)
            .withMessage(input -> "Is not greater than 2, for input: " + input)
            .isValid()
    ).isTrue();
    assertThat(
        forInput(value)
            .couldSatisfy(isGreaterThan2).ifNotThrow(IllegalArgumentException::new)
            .withMessage(input -> "Is not greater than 2, for input: " + input)
            .orSatisfies(isEven).ifNotThrow(IllegalStateException::new)
            .withMessage(input -> "Is not even, for input: " + input)
            .isValid()
    ).isTrue();

    assertThat(
        forInput(1)
            .couldSatisfy(isEven).ifNotThrow(IllegalStateException::new)
            .withMessage(input -> "Is not even, for input: " + input)
            .orSatisfies(isGreaterThan2).ifNotThrow(IllegalArgumentException::new)
            .withMessage(input -> "Is not greater than 2, for input: " + input)
            .isValid()
    ).isFalse();
    assertThat(
        forInput(1)
            .couldSatisfy(isGreaterThan2).ifNotThrow(IllegalArgumentException::new)
            .withMessage(input -> "Is not greater than 2, for input: " + input)
            .orSatisfies(isEven).ifNotThrow(IllegalStateException::new)
            .withMessage(input -> "Is not even, for input: " + input)
            .isValid()
    ).isFalse();
  }

  @ParameterizedTest
  @ValueSource(ints = {4, 3, 2})
  void checksInputIsValidUsingCustomMessageOnly(int value) {
    assertThat(
        forInput(value)
            .couldSatisfy(isEven)
            .butWas(input -> "Is not even, for input: " + input)
            .orSatisfies(isGreaterThan2)
            .butWas(input -> "Is not greater than 2, for input: " + input)
            .isValid()
    ).isTrue();
    assertThat(
        forInput(value)
            .couldSatisfy(isGreaterThan2)
            .butWas(input -> "Is not greater than 2, for input: " + input)
            .orSatisfies(isEven)
            .butWas(input -> "Is not even, for input: " + input)
            .isValid()
    ).isTrue();

    assertThat(
        forInput(1)
            .couldSatisfy(isEven)
            .butWas(input -> "Is not even, for input: " + input)
            .orSatisfies(isGreaterThan2)
            .butWas(input -> "Is not greater than 2, for input: " + input)
            .isValid()
    ).isFalse();
    assertThat(
        forInput(1)
            .couldSatisfy(isGreaterThan2)
            .butWas(input -> "Is not greater than 2, for input: " + input)
            .orSatisfies(isEven)
            .butWas(input -> "Is not even, for input: " + input)
            .isValid()
    ).isFalse();
  }
}
