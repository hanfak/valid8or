package com.github.hanfak.valid8or.implmentation.couldsatisfy.singlerule;

import testinfrastructure.TestFixtures;
import org.junit.jupiter.api.Test;

import static com.github.hanfak.valid8or.api.Valid8or.forInput;
import static java.lang.String.format;

// No use of consumer or exception thrown, this is up to the user
public class checkIsValidWithMessageInExceptionTest extends TestFixtures {
  // TODO paramtise for true/false assertion
  @Test
  void checksInputIsValidUsingExceptionWithoutMessage() {
    assertThat(
        forInput(4)
            .couldSatisfy(isEven).ifNotThrowAn(IllegalStateException::new)
            .isValid()
    ).isTrue();

    assertThat(
        forInput(3)
            .couldSatisfy(isEven).ifNotThrowAn(IllegalStateException::new)
            .isValid()
    ).isFalse();
  }

  @Test
  void checksInputIsValidUsingCustomMessageInException() {
    assertThat(
        forInput(4)
            .couldSatisfy(isEven).ifNotThrowAn(() -> new IllegalStateException("Some Exception"))
            .isValid()
    ).isTrue();

    assertThat(
        forInput(3)
            .couldSatisfy(isEven).ifNotThrowAn(() -> new IllegalStateException("Some Exception"))
            .isValid()
    ).isFalse();
  }

  @Test
  void checksInputIsValidUsingCustomMessageOutsideException() {
    assertThat(
        forInput(4)
            .couldSatisfy(isEven).ifNotThrow(IllegalStateException::new)
            .withMessage(input -> "Is not even, for input: " + input)
            .isValid()
    ).isTrue();

    assertThat(
        forInput(3)
            .couldSatisfy(isEven).ifNotThrow(IllegalStateException::new)
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
