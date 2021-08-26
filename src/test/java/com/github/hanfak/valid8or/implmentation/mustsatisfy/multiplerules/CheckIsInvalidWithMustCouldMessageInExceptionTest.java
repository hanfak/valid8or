package com.github.hanfak.valid8or.implmentation.mustsatisfy.multiplerules;

import org.junit.jupiter.api.Test;
import testinfrastructure.TestFixtures;

import static com.github.hanfak.valid8or.implmentation.Valid8orMustSatisfyAllRules.forInput;

class CheckIsInvalidWithMustCouldMessageInExceptionTest extends TestFixtures {

  @Test
  void checksInputIsInvalidUsingCustomMessageOutsideException() {
    assertThat(
        forInput(4)
            .mustSatisfy(isEven).orElseThrow(IllegalStateException::new)
            .withExceptionMessage(input -> "Is not even, for input: " + input)
            .and(isGreaterThan2).orElseThrow(IllegalArgumentException::new)
            .withExceptionMessage(input -> "Is not greater than 2, for input: " + input)
            .isNotValid()
    ).isFalse();
    assertThat(
        forInput(4)
            .mustSatisfy(isGreaterThan2).orElseThrow(IllegalArgumentException::new)
            .withExceptionMessage(input -> "Is not greater than 2, for input: " + input)
            .and(isEven).orElseThrow(IllegalStateException::new)
            .withExceptionMessage(input -> "Is not even, for input: " + input)
            .isNotValid()
    ).isFalse();

    assertThat(
        forInput(1)
            .mustSatisfy(isEven).orElseThrow(IllegalStateException::new)
            .withExceptionMessage(input -> "Is not even, for input: " + input)
            .and(isGreaterThan2).orElseThrow(IllegalArgumentException::new)
            .withExceptionMessage(input -> "Is not greater than 2, for input: " + input)
            .isNotValid()
    ).isTrue();
    assertThat(
        forInput(1)
            .mustSatisfy(isGreaterThan2).orElseThrow(IllegalArgumentException::new)
            .withExceptionMessage(input -> "Is not greater than 2, for input: " + input)
            .and(isEven).orElseThrow(IllegalStateException::new)
            .withExceptionMessage(input -> "Is not even, for input: " + input)
            .isNotValid()
    ).isTrue();
  }

  @Test
  void checksInputIsInvalidUsingCustomMessageOnly() {
    assertThat(
        forInput(4)
            .mustSatisfy(isEven)
            .butWas(input -> "Is not even, for input: " + input)
            .and(isGreaterThan2)
            .butWas(input -> "Is not greater than 2, for input: " + input)
            .isNotValid()
    ).isFalse();
    assertThat(
        forInput(4)
            .mustSatisfy(isGreaterThan2)
            .butWas(input -> "Is not greater than 2, for input: " + input)
            .and(isEven)
            .butWas(input -> "Is not even, for input: " + input)
            .isNotValid()
    ).isFalse();

    assertThat(
        forInput(1)
            .mustSatisfy(isEven)
            .butWas(input -> "Is not even, for input: " + input)
            .and(isGreaterThan2)
            .butWas(input -> "Is not greater than 2, for input: " + input)
            .isNotValid()
    ).isTrue();
    assertThat(
        forInput(1)
            .mustSatisfy(isGreaterThan2)
            .butWas(input -> "Is not greater than 2, for input: " + input)
            .and(isEven)
            .butWas(input -> "Is not even, for input: " + input)
            .isNotValid()
    ).isTrue();
  }
}
