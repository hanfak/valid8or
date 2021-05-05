package com.github.hanfak.valid8or.implmentation.mustsatisfy.singlerule;

import org.junit.jupiter.api.Test;
import testinfrastructure.TestFixtures;

import static com.github.hanfak.valid8or.api.Valid8or.forInput;

// No use of consumer or exception thrown, this is up to the user
public class checkIsValidhasMessageInExceptionTest extends TestFixtures {
  // TODO paramtise for true/false assertion
  @Test
  void checksInputIsValidUsingExceptionWithoutMessage() {
    assertThat(
        forInput(4)
            .mustSatisfy(isEven).ifNotWillThrowAn(IllegalStateException::new)
            .isValid()
    ).isTrue();

    assertThat(
        forInput(3)
            .mustSatisfy(isEven).ifNotWillThrowAn(IllegalStateException::new)
            .isValid()
    ).isFalse();
  }

  @Test
  void checksInputIsValidUsingCustomMessageInException() {
    assertThat(
        forInput(4)
            .mustSatisfy(isEven).ifNotWillThrowAn(() -> new IllegalStateException("Some Exception"))
            .isValid()
    ).isTrue();

    assertThat(
        forInput(3)
            .mustSatisfy(isEven).ifNotWillThrowAn(() -> new IllegalStateException("Some Exception"))
            .isValid()
    ).isFalse();
  }

  @Test
  void checksInputIsValidUsingCustomMessageOutsideException() {
    assertThat(
        forInput(4)
            .mustSatisfy(isEven).ifNotWillThrow(IllegalStateException::new)
            .hasMessage(input -> "Is not even, for input: " + input)
            .isValid()
    ).isTrue();

    assertThat(
        forInput(3)
            .mustSatisfy(isEven).ifNotWillThrow(IllegalStateException::new)
            .hasMessage(input -> "Is not even, for input: " + input)
            .isValid()
    ).isFalse();
  }

  @Test
  void checksInputIsValidUsingCustomMessageOnly() {
    assertThat(
        forInput(4)
            .mustSatisfy(isEven)
            .butIs(input -> "Is not even, for input: " + input)
            .isValid()
    ).isTrue();

    assertThat(
        forInput(3)
            .mustSatisfy(isEven)
            .butIs(input -> "Is not even, for input: " + input)
            .isValid()
    ).isFalse();
  }
}
