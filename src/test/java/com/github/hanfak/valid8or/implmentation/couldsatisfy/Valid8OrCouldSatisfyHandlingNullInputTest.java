package com.github.hanfak.valid8or.implmentation.couldsatisfy;

import com.github.hanfak.valid8or.implmentation.ValidationException;
import org.junit.jupiter.api.Test;
import testinfrastructure.TestFixtures;

import static com.github.hanfak.valid8or.implmentation.Valid8orCouldSatisfyAllRules.forInput;

class Valid8OrCouldSatisfyHandlingNullInputTest extends TestFixtures {

  @Test
  void validateThenReturnOptionalWrapsValidatedInputWithOptional() {
    assertThat(
        forInput(4)
            .couldSatisfy(isEven)
            .orThrowExceptionWith(input -> "not legal")
            .isValidReturnOptionalOrThrow())
        .isPresent().containsInstanceOf(Integer.class).contains(4);
  }

  @Test
  void validateThenReturnOptionalEmptyWhenInputIsNull() {
    assertThat(
        forInput(null)
            .couldSatisfy(input -> true)
            .orThrowExceptionWith(input -> "not legal")
            .isValidReturnOptionalOrThrow())
        .isEmpty();
  }

  @Test
  void validationFailsForNullInputThrowsValidationExceptionUsingValidateThenReturnOptional() {
    assertThatThrownBy(() ->
        forInput(null)
            .couldSatisfy(input -> false)
            .orThrowExceptionWith(input -> "not legal")
            .isValidReturnOptionalOrThrow())
        .isInstanceOf(ValidationException.class).hasMessage("not legal");
  }

  @Test
  void validationFailsForNullInputThrowsCustomExceptionUsingValidateThenReturnOptional() {
    assertThatThrownBy(() ->
        forInput(null)
            .couldSatisfy(input -> false)
            .orElseThrow(IllegalStateException::new)
            .withMessage(input -> "not legal")
            .isValidReturnOptionalOrThrow())
        .isInstanceOf(IllegalStateException.class).hasMessage("not legal");
  }

  @Test
  void validationFailsForNullInputThrowsValidationExceptionUsingValidate() {
    assertThatThrownBy(() ->
        forInput(null)
            .couldSatisfy(input -> false)
            .orThrowExceptionWith(input -> "not legal")
            .isValidOrThrow())
        .isInstanceOf(ValidationException.class).hasMessage("not legal");
  }

  @Test
  void validationFailsForNullInputThrowsCustomExceptionUsingValidate() {
    assertThatThrownBy(() ->
        forInput(null)
            .couldSatisfy(input -> false)
            .orElseThrow(IllegalStateException::new)
            .withMessage(input -> "not legal")
            .isValidOrThrow())
        .isInstanceOf(IllegalStateException.class).hasMessage("not legal");
  }

  @Test
  void validationFailsForNullInputListAsFailedValidation() {
    assertThat(
        forInput(null)
            .couldSatisfy(x -> false).orElseThrow(IllegalStateException::new)
            .withMessage(input -> "Is not even, for input: " + input)
            .allExceptionMessages()
    ).containsOnly("Is not even, for input: null");

    assertThat(
        forInput(null)
            .couldSatisfy(x -> false)
            .orThrowExceptionWith(input -> "Is not even, for input: " + input)
            .allExceptionMessages()
    ).containsOnly("Is not even, for input: null");
  }
}
