package com.github.hanfak.valid8or.implmentation.compound.mustsatisfy.singlerule;

import org.junit.jupiter.api.Test;
import testinfrastructure.TestFixtures;

import static com.github.hanfak.valid8or.api.Valid8or.forInput;

// No use of consumer or exception thrown, this is up to the user
public class checkIsInvalidWithMessageInExceptionTest extends TestFixtures {
  // TODO paramtise for true/false assertion
  @Test
  void checksInputIsInvalidUsingExceptionWithoutMessage() {
    assertThat(
        forInput(4)
            .mustSatisfy(isEven).ifNotWillThrowAn(IllegalStateException::new)
            .isInvalid()
    ).isFalse();

    assertThat(
        forInput(3)
            .mustSatisfy(isEven).ifNotWillThrowAn(IllegalStateException::new)
            .isInvalid()
    ).isTrue();
  }

  @Test
  void checksInputIsInvalidUsingCustomMessageInException() {
    assertThat(
        forInput(4)
            .mustSatisfy(isEven).ifNotWillThrowAn(() -> new IllegalStateException("Some Exception"))
            .isInvalid()
    ).isFalse();

    assertThat(
        forInput(3)
            .mustSatisfy(isEven).ifNotWillThrowAn(() -> new IllegalStateException("Some Exception"))
            .isInvalid()
    ).isTrue();
  }

  @Test
  void checksInputIsInvalidUsingCustomMessageOutsideException() {
    assertThat(
        forInput(4)
            .mustSatisfy(isEven).ifNotWillThrow(IllegalStateException::new)
            .hasMessage(input -> "Is not even, for input: " + input)
            .isInvalid()
    ).isFalse();

    assertThat(
        forInput(3)
            .mustSatisfy(isEven).ifNotWillThrow(IllegalStateException::new)
            .hasMessage(input -> "Is not even, for input: " + input)
            .isInvalid()
    ).isTrue();
  }

  @Test
  void checksInputIsInvalidUsingCustomMessageOnly() {
    assertThat(
        forInput(4)
            .mustSatisfy(isEven)
            .butIs(input -> "Is not even, for input: " + input)
            .isInvalid()
    ).isFalse();

    assertThat(
        forInput(3)
            .mustSatisfy(isEven)
            .butIs(input -> "Is not even, for input: " + input)
            .isInvalid()
    ).isTrue();
  }
}
