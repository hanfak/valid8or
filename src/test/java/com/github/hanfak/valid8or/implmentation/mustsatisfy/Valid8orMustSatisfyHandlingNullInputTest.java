package com.github.hanfak.valid8or.implmentation.mustsatisfy;

import com.github.hanfak.valid8or.implmentation.domain.ValidationException;
import org.junit.jupiter.api.Test;
import testinfrastructure.TestFixtures;

import static com.github.hanfak.valid8or.api.Valid8orMustSatisfyAllRules.forInput;

public class Valid8orMustSatisfyHandlingNullInputTest extends TestFixtures {
  @Test
  void validateThenReturnOptionalWrapsValidatedInputWithOptional() {
    assertThat(
        forInput(4)
            .mustSatisfy(isEven)
            .butWas(input -> "not legal")
            .validateThenReturnOptional())
        .isPresent().containsInstanceOf(Integer.class).contains(4);
  }

  @Test
  void validateThenReturnOptionalEmptyWhenInputIsNull() {
    assertThat(
        forInput(null)
            .mustSatisfy(input -> true)
            .butWas(input -> "not legal")
            .validateThenReturnOptional())
        .isEmpty();
  }

  @Test
  void validationFailsForNullInputThrowsValidationExceptionUsingValidateThenReturnOptional() {
    assertThatThrownBy( () ->
        forInput(null)
            .mustSatisfy(input -> false)
            .butWas(input -> "not legal")
            .validateThenReturnOptional())
        .isInstanceOf(ValidationException.class).hasMessage("not legal");
  }

  @Test
  void validationFailsForNullInputThrowsCustomExceptionUsingValidateThenReturnOptional() {
    assertThatThrownBy( () ->
        forInput(null)
            .mustSatisfy(input -> false)
            .orThrow(IllegalStateException::new)
            .withMessage(input -> "not legal")
            .validateThenReturnOptional())
        .isInstanceOf(IllegalStateException.class).hasMessage("not legal");
  }

  @Test
  void validationFailsForNullInputThrowsValidationExceptionUsingValidate() {
    assertThatThrownBy( () ->
        forInput(null)
            .mustSatisfy(input -> false)
            .butWas(input -> "not legal")
            .validate())
        .isInstanceOf(ValidationException.class).hasMessage("not legal");
  }

  @Test
  void validationFailsForNullInputThrowsCustomExceptionUsingValidate() {
    assertThatThrownBy( () ->
        forInput(null)
            .mustSatisfy(input -> false)
            .orThrow(IllegalStateException::new)
            .withMessage(input -> "not legal")
            .validate())
        .isInstanceOf(IllegalStateException.class).hasMessage("not legal");
  }

  @Test
  void validationFailsForNullInputListAsFailedValidation() {
    assertThat(
        forInput(null)
            .mustSatisfy(x -> false).orThrow(IllegalStateException::new)
            .withMessage(input -> "Is not even, for input: " + input)
            .allExceptionMessages()
    ).containsOnly("Is not even, for input: null");

    assertThat(
        forInput(null)
            .mustSatisfy(x -> false)
            .butWas(input -> "Is not even, for input: " + input)
            .allExceptionMessages()
    ).containsOnly("Is not even, for input: null");
  }
}
