package com.github.hanfak.valid8or.implmentation.mustsatisfy;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import testinfrastructure.TestFixtures;

import static com.github.hanfak.valid8or.implmentation.Valid8orMustSatisfyAllRules.forInput;

class Valid8OrMustMustCouldSatisfyAllValidationRulesBuilderTest extends TestFixtures {

  @Test
  void mustSatisfiesMethodCannotAcceptNullArguments() {
    Assertions.assertThatThrownBy(() ->
        forInput(4)
            .mustSatisfy(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Rule must be provided");
  }

  @Test
  void andSatisfiesMethodCannotAcceptNullArguments() {
    Assertions.assertThatThrownBy(() ->
        forInput(4)
            .mustSatisfy(isEven).butWas(input -> "not legal")
            .andSatisfies(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Rule must be provided");
  }

  @Test
  void orThrowMethodCannotAcceptNullArguments() {
    Assertions.assertThatThrownBy(() ->
        forInput(4)
            .mustSatisfy(isEven)
            .orThrow(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("An exception function must not be provided");
  }

  @Test
  void withMessageMethodCannotAcceptNullArguments() {
    Assertions.assertThatThrownBy(() ->
        forInput(4)
            .mustSatisfy(isEven)
            .orThrow(IllegalArgumentException::new)
            .withMessage(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Message must not be provided");
  }

  @Test
  void butWasMethodCannotAcceptNullArguments() {
    Assertions.assertThatThrownBy(() ->
        forInput(4)
            .mustSatisfy(isEven)
            .butWas(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Message must not be provided");
  }

  @Test
  void thenConsumesMethodCannotAcceptNullArguments() {
    Assertions.assertThatThrownBy(() ->
        forInput(4)
            .mustSatisfy(isEven)
            .butWas(input -> "not legal")
            .thenConsume(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("A consumer must not be provided");
  }
}
