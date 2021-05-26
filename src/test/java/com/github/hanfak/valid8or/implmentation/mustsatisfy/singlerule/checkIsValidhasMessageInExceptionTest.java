package com.github.hanfak.valid8or.implmentation.mustsatisfy.singlerule;

import org.junit.jupiter.api.Test;
import testinfrastructure.TestFixtures;

import static com.github.hanfak.valid8or.api.Valid8orMustSatisfyAllRules.forInput;

// No use of consumer or exception thrown
public class checkIsValidhasMessageInExceptionTest extends TestFixtures {
  // TODO paramtise for true/false assertion

  @Test
  void checksInputIsValidUsingCustomMessageOutsideException() {
    assertThat(
        forInput(4)
            .mustSatisfy(isEven).orThrow(IllegalStateException::new)
            .withMessage(input -> "Is not even, for input: " + input)
            .isValid()
    ).isTrue();

    assertThat(
        forInput(3)
            .mustSatisfy(isEven).orThrow(IllegalStateException::new)
            .withMessage(input -> "Is not even, for input: " + input)
            .isValid()
    ).isFalse();
  }

  @Test
  void checksInputIsValidUsingCustomMessageOnly() {
    assertThat(
        forInput(4)
            .mustSatisfy(isEven)
            .butWas(input -> "Is not even, for input: " + input)
            .isValid()
    ).isTrue();

    assertThat(
        forInput(3)
            .mustSatisfy(isEven)
            .butWas(input -> "Is not even, for input: " + input)
            .isValid()
    ).isFalse();
  }
}
