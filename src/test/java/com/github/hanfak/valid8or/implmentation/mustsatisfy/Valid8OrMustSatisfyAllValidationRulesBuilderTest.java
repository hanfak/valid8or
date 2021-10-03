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
            .mustSatisfy(null)
            .orThrowExceptionWith(input -> "not legal"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Predicate rule must be provided");
  }

  @Test
  void andSatisfiesMethodCannotAcceptNullArguments() {
    assertThatThrownBy(() ->
        forInput(4)
            .mustSatisfy(isEven).orThrowExceptionWith(input -> "not legal")
            .and(null).orThrowExceptionWith(input -> "not legal"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Predicate rule must be provided");
  }

  @Test
  void orThrowMethodCannotAcceptNullArguments() {
    assertThatThrownBy(() ->
        forInput(4)
            .mustSatisfy(isEven)
            .orElseThrow(null).withExceptionMessage(input -> "not legal"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("An exception function must be provided");
  }

  @Test
  void withMessageMethodCannotAcceptNullArguments() {
    assertThatThrownBy(() ->
        forInput(4)
            .mustSatisfy(isEven)
            .orElseThrow(IllegalArgumentException::new)
            .withExceptionMessage(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Message function must be provided");
  }

  @Test
  void butWasMethodCannotAcceptNullArguments() {
    assertThatThrownBy(() ->
        forInput(4)
            .mustSatisfy(isEven)
            .orThrowExceptionWith(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Message function must be provided");
  }

  @Test
  void thenConsumesMethodCannotAcceptNullArguments() {
    assertThatThrownBy(() ->
        forInput(4)
            .mustSatisfy(isEven)
            .orThrowExceptionWith(input -> "not legal")
            .useConsumer(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("A consumer must be provided");
  }
  @Test
  void thenValidateAndThrowNotifyWithCustomExceptionMethodCannotAcceptNullArguments() {
    assertThatThrownBy(() ->
        forInput(4)
            .mustSatisfy(isEven)
            .orThrowExceptionWith(input -> "not legal")
            .isValidOrThrowCombined(null, (input, error) -> "some message"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("An exception function must be provided");

    assertThatThrownBy(() ->
        forInput(4)
            .mustSatisfy(isEven)
            .orThrowExceptionWith(input -> "not legal")
            .isValidOrThrowCombined(CustomException::new, null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Message function must be provided");
  }
}
