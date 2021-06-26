package com.github.hanfak.valid8or.implmentation.mustsatisfy;

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
            .butWas(input -> "not legal")
            .validateThenReturnOptional())
        .isPresent().containsInstanceOf(Integer.class).contains(4);
  }

  @Test
  void validateThenReturnOptionalEmptyWhenInputIsNull() {
    assertThat(
        forInput(null)
            .couldSatisfy(input -> true)
            .butWas(input -> "not legal")
            .validateThenReturnOptional())
        .isEmpty();
  }

  @Test
  void validationFailsForNullInputThrowsValidationExceptionUsingValidateThenReturnOptional() {
    assertThatThrownBy(() ->
        forInput(null)
            .couldSatisfy(input -> false)
            .butWas(input -> "not legal")
            .validateThenReturnOptional())
        .isInstanceOf(ValidationException.class).hasMessage("not legal");
  }

  @Test
  void validationFailsForNullInputThrowsCustomExceptionUsingValidateThenReturnOptional() {
    assertThatThrownBy(() ->
        forInput(null)
            .couldSatisfy(input -> false)
            .orThrow(IllegalStateException::new)
            .withMessage(input -> "not legal")
            .validateThenReturnOptional())
        .isInstanceOf(IllegalStateException.class).hasMessage("not legal");
  }

  @Test
  void validationFailsForNullInputThrowsValidationExceptionUsingValidate() {
    assertThatThrownBy(() ->
        forInput(null)
            .couldSatisfy(input -> false)
            .butWas(input -> "not legal")
            .validate())
        .isInstanceOf(ValidationException.class).hasMessage("not legal");
  }

  @Test
  void validationFailsForNullInputThrowsCustomExceptionUsingValidate() {
    assertThatThrownBy(() ->
        forInput(null)
            .couldSatisfy(input -> false)
            .orThrow(IllegalStateException::new)
            .withMessage(input -> "not legal")
            .validate())
        .isInstanceOf(IllegalStateException.class).hasMessage("not legal");
  }

  @Test
  void validationFailsForNullInputListAsFailedValidation() {
    assertThat(
        forInput(null)
            .couldSatisfy(x -> false).orThrow(IllegalStateException::new)
            .withMessage(input -> "Is not even, for input: " + input)
            .allExceptionMessages()
    ).containsOnly("Is not even, for input: null");

    assertThat(
        forInput(null)
            .couldSatisfy(x -> false)
            .butWas(input -> "Is not even, for input: " + input)
            .allExceptionMessages()
    ).containsOnly("Is not even, for input: null");
  }
}
