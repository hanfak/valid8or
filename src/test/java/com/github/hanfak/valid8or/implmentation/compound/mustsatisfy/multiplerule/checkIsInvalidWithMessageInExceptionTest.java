package com.github.hanfak.valid8or.implmentation.compound.mustsatisfy.multiplerule;

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
            .andSatisfies(isGreaterThan2).ifNotWillThrowAn(IllegalArgumentException::new)
            .isInvalid()
    ).isFalse();
    assertThat(
        forInput(4)
            .mustSatisfy(isGreaterThan2).ifNotWillThrowAn(IllegalArgumentException::new)
            .andSatisfies(isEven).ifNotWillThrowAn(IllegalStateException::new)
            .isInvalid()
    ).isFalse();

    assertThat(
        forInput(1)
            .mustSatisfy(isEven).ifNotWillThrowAn(IllegalStateException::new)
            .andSatisfies(isGreaterThan2).ifNotWillThrowAn(IllegalArgumentException::new)
            .isInvalid()
    ).isTrue();
    assertThat(
        forInput(1)
            .mustSatisfy(isGreaterThan2).ifNotWillThrowAn(IllegalArgumentException::new)
            .andSatisfies(isEven).ifNotWillThrowAn(IllegalStateException::new)
            .isInvalid()
    ).isTrue();
  }

  @Test
  void checksInputIsInvalidUsingCustomMessageInException() {
    assertThat(
        forInput(4)
            .mustSatisfy(isEven).ifNotWillThrowAn(() -> new IllegalStateException("Some Exception"))
            .andSatisfies(isGreaterThan2).ifNotWillThrowAn(() -> new IllegalArgumentException("Some Other Exception"))
            .isInvalid()
    ).isFalse();
    assertThat(
        forInput(4)
            .mustSatisfy(isGreaterThan2).ifNotWillThrowAn(() -> new IllegalArgumentException("Some Other Exception"))
            .andSatisfies(isEven).ifNotWillThrowAn(() -> new IllegalStateException("Some Exception"))
            .isInvalid()
    ).isFalse();

    assertThat(
        forInput(1)
            .mustSatisfy(isEven).ifNotWillThrowAn(() -> new IllegalStateException("Some Exception"))
            .andSatisfies(isGreaterThan2).ifNotWillThrowAn(() -> new IllegalArgumentException("Some Other Exception"))
            .isInvalid()
    ).isTrue();
    assertThat(
        forInput(1)
            .mustSatisfy(isGreaterThan2).ifNotWillThrowAn(() -> new IllegalArgumentException("Some Other Exception"))
            .andSatisfies(isEven).ifNotWillThrowAn(() -> new IllegalStateException("Some Exception"))
            .isInvalid()
    ).isTrue();
  }

  @Test
  void checksInputIsInvalidUsingCustomMessageOutsideException() {
    assertThat(
        forInput(4)
            .mustSatisfy(isEven).ifNotWillThrow(IllegalStateException::new)
            .hasMessage(input -> "Is not even, for input: " + input)
            .andSatisfies(isGreaterThan2).ifNotWillThrow(IllegalArgumentException::new)
            .hasMessage(input -> "Is not greater than 2, for input: " + input)
            .isInvalid()
    ).isFalse();
    assertThat(
        forInput(4)
            .mustSatisfy(isGreaterThan2).ifNotWillThrow(IllegalArgumentException::new)
            .hasMessage(input -> "Is not greater than 2, for input: " + input)
            .andSatisfies(isEven).ifNotWillThrow(IllegalStateException::new)
            .hasMessage(input -> "Is not even, for input: " + input)
            .isInvalid()
    ).isFalse();

    assertThat(
        forInput(1)
            .mustSatisfy(isEven).ifNotWillThrow(IllegalStateException::new)
            .hasMessage(input -> "Is not even, for input: " + input)
            .andSatisfies(isGreaterThan2).ifNotWillThrow(IllegalArgumentException::new)
            .hasMessage(input -> "Is not greater than 2, for input: " + input)
            .isInvalid()
    ).isTrue();
    assertThat(
        forInput(1)
            .mustSatisfy(isGreaterThan2).ifNotWillThrow(IllegalArgumentException::new)
            .hasMessage(input -> "Is not greater than 2, for input: " + input)
            .andSatisfies(isEven).ifNotWillThrow(IllegalStateException::new)
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
            .andSatisfies(isGreaterThan2)
            .butIs(input -> "Is not greater than 2, for input: " + input)
            .isInvalid()
    ).isFalse();
    assertThat(
        forInput(4)
            .mustSatisfy(isGreaterThan2)
            .butIs(input -> "Is not greater than 2, for input: " + input)
            .andSatisfies(isEven)
            .butIs(input -> "Is not even, for input: " + input)
            .isInvalid()
    ).isFalse();

    assertThat(
        forInput(1)
            .mustSatisfy(isEven)
            .butIs(input -> "Is not even, for input: " + input)
            .andSatisfies(isGreaterThan2)
            .butIs(input -> "Is not greater than 2, for input: " + input)
            .isInvalid()
    ).isTrue();
    assertThat(
        forInput(1)
            .mustSatisfy(isGreaterThan2)
            .butIs(input -> "Is not greater than 2, for input: " + input)
            .andSatisfies(isEven)
            .butIs(input -> "Is not even, for input: " + input)
            .isInvalid()
    ).isTrue();
  }
}
