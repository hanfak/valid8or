package com.github.hanfak.valid8or.implmentation.mustsatisfy;

import org.junit.jupiter.api.Test;
import testinfrastructure.CustomException;
import testinfrastructure.TestFixtures;

import static com.github.hanfak.valid8or.implmentation.Valid8orMustSatisfyAllRules.forInput;

class Valid8OrMustSatisfyAllValidationRulesBuilderTest extends TestFixtures {

  @Test
  void mustSatisfiesMethodCannotAcceptNullArguments() {
    assertThatThrownBy(() ->
        forInput(4)
            .mustSatisfy(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Rule must be provided");
  }

  @Test
  void andSatisfiesMethodCannotAcceptNullArguments() {
    assertThatThrownBy(() ->
        forInput(4)
            .mustSatisfy(isEven).butWas(input -> "not legal")
            .andSatisfies(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Rule must be provided");
  }

  @Test
  void orThrowMethodCannotAcceptNullArguments() {
    assertThatThrownBy(() ->
        forInput(4)
            .mustSatisfy(isEven)
            .orThrow(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("An exception function must be provided");
  }

  @Test
  void withMessageMethodCannotAcceptNullArguments() {
    assertThatThrownBy(() ->
        forInput(4)
            .mustSatisfy(isEven)
            .orThrow(IllegalArgumentException::new)
            .withMessage(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Message function must be provided");
  }

  @Test
  void butWasMethodCannotAcceptNullArguments() {
    assertThatThrownBy(() ->
        forInput(4)
            .mustSatisfy(isEven)
            .butWas(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Message function must be provided");
  }

  @Test
  void thenConsumesMethodCannotAcceptNullArguments() {
    assertThatThrownBy(() ->
        forInput(4)
            .mustSatisfy(isEven)
            .butWas(input -> "not legal")
            .thenConsume(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("A consumer must be provided");
  }
  @Test
  void thenValidateAndThrowNotifyWithCustomExceptionMethodCannotAcceptNullArguments() {
    assertThatThrownBy(() ->
        forInput(4)
            .mustSatisfy(isEven)
            .butWas(input -> "not legal")
            .validateOrThrowNotify(null, (input, error) -> "some message"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("An exception function must be provided");

    assertThatThrownBy(() ->
        forInput(4)
            .mustSatisfy(isEven)
            .butWas(input -> "not legal")
            .validateOrThrowNotify(CustomException::new, null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Message function must be provided");
  }
}
