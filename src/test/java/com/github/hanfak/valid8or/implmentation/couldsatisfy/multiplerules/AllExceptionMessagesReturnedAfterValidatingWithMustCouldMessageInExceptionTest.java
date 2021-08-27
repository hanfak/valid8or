package com.github.hanfak.valid8or.implmentation.couldsatisfy.multiplerules;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import testinfrastructure.TestFixtures;

import static com.github.hanfak.valid8or.implmentation.Valid8orCouldSatisfyAllRules.forInput;

class AllExceptionMessagesReturnedAfterValidatingWithMustCouldMessageInExceptionTest extends TestFixtures {

  @ParameterizedTest
  @ValueSource(ints = {4, 3, 2})
  void checksInputIsValidUsingCustomMessageOutsideException(int value) {
    assertThat(
        forInput(value)
            .couldSatisfy(isEven).orElseThrow(IllegalStateException::new)
            .withExceptionMessage(input -> "Is not even, for input: " + input)
            .or(isGreaterThan2).orElseThrow(IllegalArgumentException::new)
            .withExceptionMessage(input -> "Is not greater than 2, for input: " + input)
            .allExceptionMessages()
    ).isEmpty();
    assertThat(
        forInput(value)
            .couldSatisfy(isGreaterThan2).orElseThrow(IllegalArgumentException::new)
            .withExceptionMessage(input -> "Is not greater than 2, for input: " + input)
            .or(isEven).orElseThrow(IllegalStateException::new)
            .withExceptionMessage(input -> "Is not even, for input: " + input)
            .allExceptionMessages()
    ).isEmpty();

    assertThat(
        forInput(1)
            .couldSatisfy(isEven).orElseThrow(IllegalStateException::new)
            .withExceptionMessage(input -> "Is not even, for input: " + input)
            .or(isGreaterThan2).orElseThrow(IllegalArgumentException::new)
            .withExceptionMessage(input -> "Is not greater than 2, for input: " + input)
            .allExceptionMessages()
    ).containsOnly("Is not greater than 2, for input: 1", "Is not even, for input: 1");
    assertThat(
        forInput(1)
            .couldSatisfy(isGreaterThan2).orElseThrow(IllegalArgumentException::new)
            .withExceptionMessage(input -> "Is not greater than 2, for input: " + input)
            .or(isEven).orElseThrow(IllegalStateException::new)
            .withExceptionMessage(input -> "Is not even, for input: " + input)
            .allExceptionMessages()
    ).containsOnly("Is not greater than 2, for input: 1", "Is not even, for input: 1");
  }

  @ParameterizedTest
  @ValueSource(ints = {4, 3, 2})
  void checksInputIsValidUsingCustomMessageOnly(int value) {
    assertThat(
        forInput(value)
            .couldSatisfy(isEven)
            .orThrowExceptionWith(input -> "Is not even, for input: " + input)
            .or(isGreaterThan2)
            .orThrowExceptionWith(input -> "Is not greater than 2, for input: " + input)
            .allExceptionMessages()
    ).isEmpty();
    assertThat(
        forInput(value)
            .couldSatisfy(isGreaterThan2)
            .orThrowExceptionWith(input -> "Is not greater than 2, for input: " + input)
            .or(isEven)
            .orThrowExceptionWith(input -> "Is not even, for input: " + input)
            .allExceptionMessages()
    ).isEmpty();

    assertThat(
        forInput(1)
            .couldSatisfy(isEven)
            .orThrowExceptionWith(input -> "Is not even, for input: " + input)
            .or(isGreaterThan2)
            .orThrowExceptionWith(input -> "Is not greater than 2, for input: " + input)
            .allExceptionMessages()
    ).containsOnly("Is not greater than 2, for input: 1", "Is not even, for input: 1");
    assertThat(
        forInput(1)
            .couldSatisfy(isGreaterThan2)
            .orThrowExceptionWith(input -> "Is not greater than 2, for input: " + input)
            .or(isEven)
            .orThrowExceptionWith(input -> "Is not even, for input: " + input)
            .allExceptionMessages()
    ).containsOnly("Is not greater than 2, for input: 1", "Is not even, for input: 1");
  }
}
