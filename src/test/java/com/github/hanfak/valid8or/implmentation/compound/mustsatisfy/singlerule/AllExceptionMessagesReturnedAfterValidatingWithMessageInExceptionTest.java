package com.github.hanfak.valid8or.implmentation.compound.mustsatisfy.singlerule;

import org.junit.jupiter.api.Test;
import testinfrastructure.TestFixtures;

import static com.github.hanfak.valid8or.api.Valid8or.forInput;

// No use of consumer or exception thrown, this is up to the user
public class AllExceptionMessagesReturnedAfterValidatingWithMessageInExceptionTest extends TestFixtures {
  // TODO paramtise for true/false assertion
  @Test
  void checksInputIsValidUsingExceptionWithoutMessage() {
    assertThat(
        forInput(4)
            .mustSatisfy(isEven).ifNotWillThrowAn(IllegalStateException::new)
            .allExceptionMessages()
    ).isEmpty();

    assertThat(
        forInput(3)
            .mustSatisfy(isEven).ifNotWillThrowAn(IllegalStateException::new)
            .allExceptionMessages()
    ).containsOnly("null");
  }

  @Test
  void checksInputIsValidUsingCustomMessageInException() {
    assertThat(
        forInput(4)
            .mustSatisfy(isEven).ifNotWillThrowAn(() -> new IllegalStateException("Some Exception"))
            .allExceptionMessages()
    ).isEmpty();

    assertThat(
        forInput(3)
            .mustSatisfy(isEven).ifNotWillThrowAn(() -> new IllegalStateException("Some Exception"))
            .allExceptionMessages()
    ).containsOnly("Some Exception");
  }

  @Test
  void checksInputIsValidUsingCustomMessageOutsideException() {
    assertThat(
        forInput(4)
            .mustSatisfy(isEven).ifNotWillThrow(IllegalStateException::new)
            .hasMessage(input -> "Is not even, for input: " + input)
            .allExceptionMessages()
    ).isEmpty();

    assertThat(
        forInput(3)
            .mustSatisfy(isEven).ifNotWillThrow(IllegalStateException::new)
            .hasMessage(input -> "Is not even, for input: " + input)
            .allExceptionMessages()
    ).containsOnly("Is not even, for input: 3");
  }

  @Test
  void checksInputIsValidUsingCustomMessageOnly() {
    assertThat(
        forInput(4)
            .mustSatisfy(isEven)
            .butIs(input -> "Is not even, for input: " + input)
            .allExceptionMessages()
    ).isEmpty();

    assertThat(
        forInput(3)
            .mustSatisfy(isEven)
            .butIs(input -> "Is not even, for input: " + input)
            .allExceptionMessages()
    ).containsOnly("Is not even, for input: 3");
  }
}
