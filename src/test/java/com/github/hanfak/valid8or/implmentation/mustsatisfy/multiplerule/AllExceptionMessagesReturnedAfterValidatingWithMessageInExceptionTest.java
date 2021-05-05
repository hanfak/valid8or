package com.github.hanfak.valid8or.implmentation.mustsatisfy.multiplerule;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import testinfrastructure.TestFixtures;

import static com.github.hanfak.valid8or.api.Valid8or.forInput;

// No use of consumer or exception thrown, this is up to the user
public class AllExceptionMessagesReturnedAfterValidatingWithMessageInExceptionTest extends TestFixtures {
  // TODO paramtise for true/false assertion
  @ParameterizedTest
  @ValueSource(ints = {3, 2,1})
  void checksInputIsValidUsingExceptionWithoutMessage(int value) {
    assertThat(
        forInput(4)
            .mustSatisfy(isEven).ifNotWillThrowAn(IllegalStateException::new)
            .andSatisfies(isGreaterThan2).ifNotWillThrowAn(IllegalArgumentException::new)
            .allExceptionMessages()
    ).isEmpty();
    assertThat(
        forInput(4)
            .mustSatisfy(isGreaterThan2).ifNotWillThrowAn(IllegalArgumentException::new)
            .andSatisfies(isEven).ifNotWillThrowAn(IllegalStateException::new)
            .allExceptionMessages()
    ).isEmpty();

    assertThat(
        forInput(value)
            .mustSatisfy(isEven).ifNotWillThrowAn(IllegalStateException::new)
            .andSatisfies(isGreaterThan2).ifNotWillThrowAn(IllegalArgumentException::new)
            .allExceptionMessages()
    ).containsOnly("null");
    assertThat(
        forInput(value)
            .mustSatisfy(isGreaterThan2).ifNotWillThrowAn(IllegalArgumentException::new)
            .andSatisfies(isEven).ifNotWillThrowAn(IllegalStateException::new)
            .allExceptionMessages()
    ).containsOnly("null");
  }

  @ParameterizedTest
  @ValueSource(ints = {3, 2,1})
  void checksInputIsValidUsingCustomMessageInException() {
    assertThat(
        forInput(4)
            .mustSatisfy(isEven).ifNotWillThrowAn(() -> new IllegalStateException("Some Exception"))
            .andSatisfies(isGreaterThan2).ifNotWillThrowAn(() -> new IllegalArgumentException("Some Other Exception"))
            .allExceptionMessages()
    ).isEmpty();
    assertThat(
        forInput(4)
            .mustSatisfy(isGreaterThan2).ifNotWillThrowAn(() -> new IllegalArgumentException("Some Other Exception"))
            .andSatisfies(isEven).ifNotWillThrowAn(() -> new IllegalStateException("Some Exception"))
            .allExceptionMessages()
    ).isEmpty();

    assertThat(
        forInput(1)
            .mustSatisfy(isEven).ifNotWillThrowAn(() -> new IllegalStateException("Some Exception"))
            .andSatisfies(isGreaterThan2).ifNotWillThrowAn(() -> new IllegalArgumentException("Some Other Exception"))
            .allExceptionMessages()
    ).containsOnly("Some Exception", "Some Other Exception");
    assertThat(
        forInput(1)
            .mustSatisfy(isGreaterThan2).ifNotWillThrowAn(() -> new IllegalArgumentException("Some Other Exception"))
            .andSatisfies(isEven).ifNotWillThrowAn(() -> new IllegalStateException("Some Exception"))
            .allExceptionMessages()
    ).containsOnly("Some Other Exception", "Some Exception");
    assertThat(
        forInput(2)
            .mustSatisfy(isEven).ifNotWillThrowAn(() -> new IllegalStateException("Some Exception"))
            .andSatisfies(isGreaterThan2).ifNotWillThrowAn(() -> new IllegalArgumentException("Some Other Exception"))
            .allExceptionMessages()
    ).containsOnly("Some Other Exception");
    assertThat(
        forInput(2)
            .mustSatisfy(isGreaterThan2).ifNotWillThrowAn(() -> new IllegalArgumentException("Some Other Exception"))
            .andSatisfies(isEven).ifNotWillThrowAn(() -> new IllegalStateException("Some Exception"))
            .allExceptionMessages()
    ).containsOnly("Some Other Exception");
    assertThat(
        forInput(3)
            .mustSatisfy(isEven).ifNotWillThrowAn(() -> new IllegalStateException("Some Exception"))
            .andSatisfies(isGreaterThan2).ifNotWillThrowAn(() -> new IllegalArgumentException("Some Other Exception"))
            .allExceptionMessages()
    ).containsOnly("Some Exception");
    assertThat(
        forInput(3)
            .mustSatisfy(isGreaterThan2).ifNotWillThrowAn(() -> new IllegalArgumentException("Some Other Exception"))
            .andSatisfies(isEven).ifNotWillThrowAn(() -> new IllegalStateException("Some Exception"))
            .allExceptionMessages()
    ).containsOnly("Some Exception");
  }

  @Test
  void checksInputIsValidUsingCustomMessageOutsideException() {
    assertThat(
        forInput(4)
            .mustSatisfy(isEven).ifNotWillThrow(IllegalStateException::new)
            .hasMessage(input -> "Is not even, for input: " + input)
            .andSatisfies(isGreaterThan2).ifNotWillThrow(IllegalStateException::new)
            .hasMessage(input -> "Is not greater than 2, for input: " + input)
            .allExceptionMessages()
    ).isEmpty();
    assertThat(
        forInput(4)
            .mustSatisfy(isGreaterThan2).ifNotWillThrow(IllegalStateException::new)
            .hasMessage(input -> "Is not greater than 2, for input: " + input)
            .andSatisfies(isEven).ifNotWillThrow(IllegalStateException::new)
            .hasMessage(input -> "Is not even, for input: " + input)
            .allExceptionMessages()
    ).isEmpty();

    assertThat(
        forInput(1)
            .mustSatisfy(isEven).ifNotWillThrow(IllegalStateException::new)
            .hasMessage(input -> "Is not even, for input: " + input)
            .andSatisfies(isGreaterThan2).ifNotWillThrow(IllegalStateException::new)
            .hasMessage(input -> "Is not greater than 2, for input: " + input)
            .allExceptionMessages()
    ).containsOnly("Is not greater than 2, for input: 1", "Is not even, for input: 1");
    assertThat(
        forInput(1)
            .mustSatisfy(isGreaterThan2).ifNotWillThrow(IllegalStateException::new)
            .hasMessage(input -> "Is not greater than 2, for input: " + input)
            .andSatisfies(isEven).ifNotWillThrow(IllegalStateException::new)
            .hasMessage(input -> "Is not even, for input: " + input)
            .allExceptionMessages()
    ).containsOnly("Is not greater than 2, for input: 1", "Is not even, for input: 1");
    assertThat(
        forInput(2)
            .mustSatisfy(isEven).ifNotWillThrow(IllegalStateException::new)
            .hasMessage(input -> "Is not even, for input: " + input)
            .andSatisfies(isGreaterThan2).ifNotWillThrow(IllegalStateException::new)
            .hasMessage(input -> "Is not greater than 2, for input: " + input)
            .allExceptionMessages()
    ).containsOnly("Is not greater than 2, for input: 2");
    assertThat(
        forInput(2)
            .mustSatisfy(isGreaterThan2).ifNotWillThrow(IllegalStateException::new)
            .hasMessage(input -> "Is not greater than 2, for input: " + input)
            .andSatisfies(isEven).ifNotWillThrow(IllegalStateException::new)
            .hasMessage(input -> "Is not even, for input: " + input)
            .allExceptionMessages()
    ).containsOnly("Is not greater than 2, for input: 2");
    assertThat(
        forInput(3)
            .mustSatisfy(isEven).ifNotWillThrow(IllegalStateException::new)
            .hasMessage(input -> "Is not even, for input: " + input)
            .andSatisfies(isGreaterThan2).ifNotWillThrow(IllegalStateException::new)
            .hasMessage(input -> "Is not greater than 2, for input: " + input)
            .allExceptionMessages()
    ).containsOnly("Is not even, for input: 3");
    assertThat(
        forInput(3)
            .mustSatisfy(isGreaterThan2).ifNotWillThrow(IllegalStateException::new)
            .hasMessage(input -> "Is not greater than 2, for input: " + input)
            .andSatisfies(isEven).ifNotWillThrow(IllegalStateException::new)
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
            .andSatisfies(isGreaterThan2)
            .butIs(input -> "Is not greater than 2, for input: " + input)
            .allExceptionMessages()
    ).isEmpty();
    assertThat(
        forInput(4)
            .mustSatisfy(isGreaterThan2)
            .butIs(input -> "Is not greater than 2, for input: " + input)
            .andSatisfies(isEven)
            .butIs(input -> "Is not even, for input: " + input)
            .allExceptionMessages()
    ).isEmpty();

    assertThat(
        forInput(1)
            .mustSatisfy(isEven)
            .butIs(input -> "Is not even, for input: " + input)
            .andSatisfies(isGreaterThan2)
            .butIs(input -> "Is not greater than 2, for input: " + input)
            .allExceptionMessages()
    ).containsOnly("Is not greater than 2, for input: 1", "Is not even, for input: 1");
    assertThat(
        forInput(1)
            .mustSatisfy(isGreaterThan2)
            .butIs(input -> "Is not greater than 2, for input: " + input)
            .andSatisfies(isEven)
            .butIs(input -> "Is not even, for input: " + input)
            .allExceptionMessages()
    ).containsOnly("Is not greater than 2, for input: 1", "Is not even, for input: 1");
    assertThat(
        forInput(2)
            .mustSatisfy(isEven)
            .butIs(input -> "Is not even, for input: " + input)
            .andSatisfies(isGreaterThan2)
            .butIs(input -> "Is not greater than 2, for input: " + input)
            .allExceptionMessages()
    ).containsOnly("Is not greater than 2, for input: 2");
    assertThat(
        forInput(2)
            .mustSatisfy(isGreaterThan2)
            .butIs(input -> "Is not greater than 2, for input: " + input)
            .andSatisfies(isEven)
            .butIs(input -> "Is not even, for input: " + input)
            .allExceptionMessages()
    ).containsOnly("Is not greater than 2, for input: 2");
    assertThat(
        forInput(3)
            .mustSatisfy(isEven)
            .butIs(input -> "Is not even, for input: " + input)
            .andSatisfies(isGreaterThan2)
            .butIs(input -> "Is not greater than 2, for input: " + input)
            .allExceptionMessages()
    ).containsOnly("Is not even, for input: 3");
    assertThat(
        forInput(3)
            .mustSatisfy(isGreaterThan2)
            .butIs(input -> "Is not greater than 2, for input: " + input)
            .andSatisfies(isEven)
            .butIs(input -> "Is not even, for input: " + input)
            .allExceptionMessages()
    ).containsOnly("Is not even, for input: 3");
  }
}
