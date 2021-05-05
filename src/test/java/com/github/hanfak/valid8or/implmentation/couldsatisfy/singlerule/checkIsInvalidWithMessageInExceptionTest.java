package com.github.hanfak.valid8or.implmentation.couldsatisfy.singlerule;

import testinfrastructure.TestFixtures;
import org.junit.jupiter.api.Test;

import static com.github.hanfak.valid8or.api.Valid8or.forInput;
import static java.lang.String.format;

// No use of consumer or exception thrown, this is up to the user
public class checkIsInvalidWithMessageInExceptionTest extends TestFixtures {
  // TODO paramtise for true/false assertion
  @Test
  void checksInputIsInvalidUsingExceptionWithoutMessage() {
    assertThat(
        forInput(4)
            .couldSatisfy(isEven).ifNotThrowAn(IllegalStateException::new)
            .isInvalid()
    ).isFalse();

    assertThat(
        forInput(3)
            .couldSatisfy(isEven).ifNotThrowAn(IllegalStateException::new)
            .isInvalid()
    ).isTrue();
  }

  @Test
  void checksInputIsInvalidUsingCustomMessageInException() {
    assertThat(
        forInput(4)
            .couldSatisfy(isEven).ifNotThrowAn(() -> new IllegalStateException("Some Exception"))
            .isInvalid()
    ).isFalse();

    assertThat(
        forInput(3)
            .couldSatisfy(isEven).ifNotThrowAn(() -> new IllegalStateException("Some Exception"))
            .isInvalid()
    ).isTrue();
  }

  @Test
  void checksInputIsInvalidUsingCustomMessageOutsideException() {
    assertThat(
        forInput(4)
            .couldSatisfy(isEven).ifNotThrow(IllegalStateException::new)
            .withMessage(input -> "Is not even, for input: " + input)
            .isInvalid()
    ).isFalse();

    assertThat(
        forInput(3)
            .couldSatisfy(isEven).ifNotThrow(IllegalStateException::new)
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
