package com.github.hanfak.valid8or.implmentation.mustsatisfy.singlerule;

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
            .isNotValid()
    ).isFalse();

    assertThat(
        forInput(3)
            .mustSatisfy(isEven).orElseThrow(IllegalStateException::new)
            .withExceptionMessage(input -> "Is not even, for input: " + input)
            .isNotValid()
    ).isTrue();
  }

  @Test
  void checksInputIsInvalidUsingCustomMessageOnly() {
    assertThat(
        forInput(4)
            .mustSatisfy(isEven)
            .orThrowExceptionWith(input -> "Is not even, for input: " + input)
            .isNotValid()
    ).isFalse();

    assertThat(
        forInput(3)
            .mustSatisfy(isEven)
            .orThrowExceptionWith(input -> "Is not even, for input: " + input)
            .isNotValid()
    ).isTrue();
  }
}
