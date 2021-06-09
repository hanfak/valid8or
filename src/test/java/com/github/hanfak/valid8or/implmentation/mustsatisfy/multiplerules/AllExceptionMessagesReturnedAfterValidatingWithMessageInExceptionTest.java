package com.github.hanfak.valid8or.implmentation.mustsatisfy.multiplerules;

import org.junit.jupiter.api.Test;
import testinfrastructure.TestFixtures;

import static com.github.hanfak.valid8or.api.Valid8orMustSatisfyAllRules.forInput;

// No use of consumer or exception thrown
public class AllExceptionMessagesReturnedAfterValidatingWithMessageInExceptionTest extends TestFixtures {
  // TODO paramtise for true/false assertion
  // TODO separate tests with in tests

  @Test
  void checksInputIsValidUsingCustomMessageOutsideException() {
    assertThat(
        forInput(4)
            .mustSatisfy(isEven).orThrow(IllegalStateException::new)
            .withMessage(input -> "Is not even, for input: " + input)
            .andSatisfies(isGreaterThan2).orThrow(IllegalArgumentException::new)
            .withMessage(input -> "Is not greater than 2, for input: " + input)
            .allExceptionMessages()
    ).isEmpty();
    assertThat(
        forInput(4)
            .mustSatisfy(isGreaterThan2).orThrow(IllegalArgumentException::new)
            .withMessage(input -> "Is not greater than 2, for input: " + input)
            .andSatisfies(isEven).orThrow(IllegalStateException::new)
            .withMessage(input -> "Is not even, for input: " + input)
            .allExceptionMessages()
    ).isEmpty();

    assertThat(
        forInput(1)
            .mustSatisfy(isEven).orThrow(IllegalStateException::new)
            .withMessage(input -> "Is not even, for input: " + input)
            .andSatisfies(isGreaterThan2).orThrow(IllegalArgumentException::new)
            .withMessage(input -> "Is not greater than 2, for input: " + input)
            .allExceptionMessages()
    ).containsOnly("Is not greater than 2, for input: 1", "Is not even, for input: 1");
    assertThat(
        forInput(1)
            .mustSatisfy(isGreaterThan2).orThrow(IllegalArgumentException::new)
            .withMessage(input -> "Is not greater than 2, for input: " + input)
            .andSatisfies(isEven).orThrow(IllegalStateException::new)
            .withMessage(input -> "Is not even, for input: " + input)
            .allExceptionMessages()
    ).containsOnly("Is not greater than 2, for input: 1", "Is not even, for input: 1");
    assertThat(
        forInput(2)
            .mustSatisfy(isEven).orThrow(IllegalStateException::new)
            .withMessage(input -> "Is not even, for input: " + input)
            .andSatisfies(isGreaterThan2).orThrow(IllegalArgumentException::new)
            .withMessage(input -> "Is not greater than 2, for input: " + input)
            .allExceptionMessages()
    ).containsOnly("Is not greater than 2, for input: 2");
    assertThat(
        forInput(2)
            .mustSatisfy(isGreaterThan2).orThrow(IllegalArgumentException::new)
            .withMessage(input -> "Is not greater than 2, for input: " + input)
            .andSatisfies(isEven).orThrow(IllegalStateException::new)
            .withMessage(input -> "Is not even, for input: " + input)
            .allExceptionMessages()
    ).containsOnly("Is not greater than 2, for input: 2");
    assertThat(
        forInput(3)
            .mustSatisfy(isEven).orThrow(IllegalStateException::new)
            .withMessage(input -> "Is not even, for input: " + input)
            .andSatisfies(isGreaterThan2).orThrow(IllegalArgumentException::new)
            .withMessage(input -> "Is not greater than 2, for input: " + input)
            .allExceptionMessages()
    ).containsOnly("Is not even, for input: 3");
    assertThat(
        forInput(3)
            .mustSatisfy(isGreaterThan2).orThrow(IllegalArgumentException::new)
            .withMessage(input -> "Is not greater than 2, for input: " + input)
            .andSatisfies(isEven).orThrow(IllegalStateException::new)
            .withMessage(input -> "Is not even, for input: " + input)
            .allExceptionMessages()
    ).containsOnly("Is not even, for input: 3");

    assertThat(
        forInput(1)
            .mustSatisfy(isEven).orThrow(IllegalStateException::new)
            .withMessage(input -> "Is not even, for input")
            .andSatisfies(isGreaterThan2).orThrow(IllegalArgumentException::new)
            .withMessage(input -> "Is not greater than 2, for input")
            .allExceptionMessages()
    ).containsOnly("Is not greater than 2, for input", "Is not even, for input");
    assertThat(
        forInput(1)
            .mustSatisfy(isGreaterThan2).orThrow(IllegalArgumentException::new)
            .withMessage(input -> "Is not greater than 2, for input")
            .andSatisfies(isEven).orThrow(IllegalStateException::new)
            .withMessage(input -> "Is not even, for input")
            .allExceptionMessages()
    ).containsOnly("Is not greater than 2, for input", "Is not even, for input");
    assertThat(
        forInput(2)
            .mustSatisfy(isEven).orThrow(IllegalStateException::new)
            .withMessage(input -> "Is not even, for input")
            .andSatisfies(isGreaterThan2).orThrow(IllegalArgumentException::new)
            .withMessage(input -> "Is not greater than 2, for input")
            .allExceptionMessages()
    ).containsOnly("Is not greater than 2, for input");
    assertThat(
        forInput(2)
            .mustSatisfy(isGreaterThan2).orThrow(IllegalArgumentException::new)
            .withMessage(input -> "Is not greater than 2, for input")
            .andSatisfies(isEven).orThrow(IllegalStateException::new)
            .withMessage(input -> "Is not even, for input")
            .allExceptionMessages()
    ).containsOnly("Is not greater than 2, for input");
    assertThat(
        forInput(3)
            .mustSatisfy(isEven).orThrow(IllegalStateException::new)
            .withMessage(input -> "Is not even, for input")
            .andSatisfies(isGreaterThan2).orThrow(IllegalArgumentException::new)
            .withMessage(input -> "Is not greater than 2, for input")
            .allExceptionMessages()
    ).containsOnly("Is not even, for input");
    assertThat(
        forInput(3)
            .mustSatisfy(isGreaterThan2).orThrow(IllegalArgumentException::new)
            .withMessage(input -> "Is not greater than 2, for input")
            .andSatisfies(isEven).orThrow(IllegalStateException::new)
            .withMessage(input -> "Is not even, for input")
            .allExceptionMessages()
    ).containsOnly("Is not even, for input");
  }

  @Test
  void checksInputIsValidUsingCustomMessageOnly() {
    assertThat(
        forInput(4)
            .mustSatisfy(isEven)
            .butWas(input -> "Is not even, for input: " + input)
            .andSatisfies(isGreaterThan2)
            .butWas(input -> "Is not greater than 2, for input: " + input)
            .allExceptionMessages()
    ).isEmpty();
    assertThat(
        forInput(4)
            .mustSatisfy(isGreaterThan2)
            .butWas(input -> "Is not greater than 2, for input: " + input)
            .andSatisfies(isEven)
            .butWas(input -> "Is not even, for input: " + input)
            .allExceptionMessages()
    ).isEmpty();

    assertThat(
        forInput(1)
            .mustSatisfy(isEven)
            .butWas(input -> "Is not even, for input: " + input)
            .andSatisfies(isGreaterThan2)
            .butWas(input -> "Is not greater than 2, for input: " + input)
            .allExceptionMessages()
    ).containsOnly("Is not greater than 2, for input: 1", "Is not even, for input: 1");
    assertThat(
        forInput(1)
            .mustSatisfy(isGreaterThan2)
            .butWas(input -> "Is not greater than 2, for input: " + input)
            .andSatisfies(isEven)
            .butWas(input -> "Is not even, for input: " + input)
            .allExceptionMessages()
    ).containsOnly("Is not greater than 2, for input: 1", "Is not even, for input: 1");
    assertThat(
        forInput(2)
            .mustSatisfy(isEven)
            .butWas(input -> "Is not even, for input: " + input)
            .andSatisfies(isGreaterThan2)
            .butWas(input -> "Is not greater than 2, for input: " + input)
            .allExceptionMessages()
    ).containsOnly("Is not greater than 2, for input: 2");
    assertThat(
        forInput(2)
            .mustSatisfy(isGreaterThan2)
            .butWas(input -> "Is not greater than 2, for input: " + input)
            .andSatisfies(isEven)
            .butWas(input -> "Is not even, for input: " + input)
            .allExceptionMessages()
    ).containsOnly("Is not greater than 2, for input: 2");
    assertThat(
        forInput(3)
            .mustSatisfy(isEven)
            .butWas(input -> "Is not even, for input: " + input)
            .andSatisfies(isGreaterThan2)
            .butWas(input -> "Is not greater than 2, for input: " + input)
            .allExceptionMessages()
    ).containsOnly("Is not even, for input: 3");
    assertThat(
        forInput(3)
            .mustSatisfy(isGreaterThan2)
            .butWas(input -> "Is not greater than 2, for input: " + input)
            .andSatisfies(isEven)
            .butWas(input -> "Is not even, for input: " + input)
            .allExceptionMessages()
    ).containsOnly("Is not even, for input: 3");

    assertThat(
        forInput(1)
            .mustSatisfy(isEven)
            .butWas(input -> "Is not even, for input")
            .andSatisfies(isGreaterThan2)
            .butWas(input -> "Is not greater than 2, for input")
            .allExceptionMessages()
    ).containsOnly("Is not greater than 2, for input", "Is not even, for input");
    assertThat(
        forInput(1)
            .mustSatisfy(isGreaterThan2)
            .butWas(input -> "Is not greater than 2, for input")
            .andSatisfies(isEven)
            .butWas(input -> "Is not even, for input")
            .allExceptionMessages()
    ).containsOnly("Is not greater than 2, for input", "Is not even, for input");
    assertThat(
        forInput(2)
            .mustSatisfy(isEven)
            .butWas(input -> "Is not even, for input")
            .andSatisfies(isGreaterThan2)
            .butWas(input -> "Is not greater than 2, for input")
            .allExceptionMessages()
    ).containsOnly("Is not greater than 2, for input");
    assertThat(
        forInput(2)
            .mustSatisfy(isGreaterThan2)
            .butWas(input -> "Is not greater than 2, for input")
            .andSatisfies(isEven)
            .butWas(input -> "Is not even, for input")
            .allExceptionMessages()
    ).containsOnly("Is not greater than 2, for input");
    assertThat(
        forInput(3)
            .mustSatisfy(isEven)
            .butWas(input -> "Is not even, for input")
            .andSatisfies(isGreaterThan2)
            .butWas(input -> "Is not greater than 2, for input")
            .allExceptionMessages()
    ).containsOnly("Is not even, for input");
    assertThat(
        forInput(3)
            .mustSatisfy(isGreaterThan2)
            .butWas(input -> "Is not greater than 2, for input")
            .andSatisfies(isEven)
            .butWas(input -> "Is not even, for input")
            .allExceptionMessages()
    ).containsOnly("Is not even, for input");
  }
}