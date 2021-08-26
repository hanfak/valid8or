package com.github.hanfak.valid8or.implmentation.couldsatisfy;

import org.junit.jupiter.api.Test;
import testinfrastructure.CustomException;
import testinfrastructure.TestFixtures;

import static com.github.hanfak.valid8or.implmentation.Valid8orCouldSatisfyAllRules.forInput;

class Valid8OrCouldSatisfyAllValidationRulesBuilderTest extends TestFixtures {

  @Test
  void coudSatisfiesMethodCannotAcceptNullArguments() {
    assertThatThrownBy(() ->
        forInput(4)
            .couldSatisfy(null).butWas(input -> "not legal"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Predicate rule must be provided");
  }

  @Test
  void andSatisfiesMethodCannotAcceptNullArguments() {
    assertThatThrownBy(() ->
        forInput(4)
            .couldSatisfy(isEven).butWas(input -> "not legal")
            .orSatisfies(null)
            .butWas(input -> "not legal"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Predicate rule must be provided");
  }

  @Test
  void orThrowMethodCannotAcceptNullArguments() {
    assertThatThrownBy(() ->
        forInput(4)
            .couldSatisfy(isEven)
            .orThrow(null)
            .withMessage(input -> "not legal"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("An exception function must be provided");
  }

  @Test
  void withMessageMethodCannotAcceptNullArguments() {
    assertThatThrownBy(() ->
        forInput(4)
            .couldSatisfy(isEven)
            .orThrow(IllegalArgumentException::new)
            .withMessage(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Message function must be provided");
  }

  @Test
  void butWasMethodCannotAcceptNullArguments() {
    assertThatThrownBy(() ->
        forInput(4)
            .couldSatisfy(isEven)
            .butWas(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Message function must be provided");
  }

  @Test
  void thenConsumesMethodCannotAcceptNullArguments() {
    assertThatThrownBy(() ->
        forInput(4)
            .couldSatisfy(isEven)
            .butWas(input -> "not legal")
            .thenConsume(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("A consumer must be provided");
  }
  @Test
  void thenValidateAndThrowNotifyWithCustomExceptionMethodCannotAcceptNullArguments() {
    assertThatThrownBy(() ->
        forInput(4)
            .couldSatisfy(isEven)
            .butWas(input -> "not legal")
            .validateOrThrowNotify(null, (input, error) -> "some message"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("An exception function must be provided");

    assertThatThrownBy(() ->
        forInput(4)
            .couldSatisfy(isEven)
            .butWas(input -> "not legal")
            .validateOrThrowNotify(CustomException::new, null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Message function must be provided");
  }
}
