package com.github.hanfak.valid8or.implmentation.compound.couldsatisfy.singlerule;

import testinfrastructure.TestFixtures;
import org.junit.jupiter.api.Test;

import static com.github.hanfak.valid8or.api.Valid8or.forInput;
import static java.lang.String.format;

// No use of consumer or exception thrown, this is up to the user
public class AllExceptionMessagesReturnedAfterValidatingWithMessageInExceptionTest extends TestFixtures {
  // TODO paramtise for true/false assertion
  @Test
  void checksInputIsValidUsingExceptionWithoutMessage() {
    assertThat(
        forInput(4)
            .couldSatisfy(isEven).ifNotThrowAn(IllegalStateException::new)
            .allExceptionMessages()
    ).isEmpty();

    assertThat(
        forInput(3)
            .couldSatisfy(isEven).ifNotThrowAn(IllegalStateException::new)
            .allExceptionMessages()
    ).containsOnly("null");
  }

  @Test
  void checksInputIsValidUsingCustomMessageInException() {
    assertThat(
        forInput(4)
            .couldSatisfy(isEven).ifNotThrowAn(() -> new IllegalStateException("Some Exception"))
            .allExceptionMessages()
    ).isEmpty();

    assertThat(
        forInput(3)
            .couldSatisfy(isEven).ifNotThrowAn(() -> new IllegalStateException("Some Exception"))
            .allExceptionMessages()
    ).containsOnly("Some Exception");
  }

  @Test
  void checksInputIsValidUsingCustomMessageOutsideException() {
    assertThat(
        forInput(4)
            .couldSatisfy(isEven).ifNotThrow(IllegalStateException::new)
            .withMessage(input -> "Is not even, for input: " + input)
            .allExceptionMessages()
    ).isEmpty();

    assertThat(
        forInput(3)
            .couldSatisfy(isEven).ifNotThrow(IllegalStateException::new)
            .withMessage(input -> "Is not even, for input: " + input)
            .allExceptionMessages()
    ).containsOnly("Is not even, for input: 3");
  }

  @Test
  void checksInputIsValidUsingCustomMessageOnly() {
    assertThat(
        forInput(4)
            .couldSatisfy(isEven)
            .butWas(input -> "Is not even, for input: " + input)
            .allExceptionMessages()
    ).isEmpty();

    assertThat(
        forInput(3)
            .couldSatisfy(isEven)
            .butWas(input -> "Is not even, for input: " + input)
            .allExceptionMessages()
    ).containsOnly("Is not even, for input: 3");
  }
}
