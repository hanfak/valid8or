package com.github.hanfak.valid8or.implmentation.mustsatisfy;

import com.github.hanfak.valid8or.implmentation.ValidationException;
import org.junit.jupiter.api.Test;
import testinfrastructure.TestFixtures;

import static com.github.hanfak.valid8or.implmentation.Valid8orMustSatisfyAllRules.forInput;

class Valid8OrMustSatisfyHandlingNullInputTest extends TestFixtures {

  @Test
  void validateThenReturnOptionalWrapsValidatedInputWithOptional() {
    assertThat(
        forInput(4)
            .mustSatisfy(isEven)
            .orThrowExceptionWith(input -> "not legal")
            .throwIfNotValidOrReturnOptional())
        .isPresent().containsInstanceOf(Integer.class).contains(4);
  }

  @Test
  void validateThenReturnOptionalEmptyWhenInputIsNull() {
    assertThat(
        forInput(null)
            .mustSatisfy(input -> true)
            .orThrowExceptionWith(input -> "not legal")
            .throwIfNotValidOrReturnOptional())
        .isEmpty();
  }

  @Test
  void validationFailsForNullInputThrowsValidationExceptionUsingValidateThenReturnOptional() {
    assertThatThrownBy(() ->
        forInput(null)
            .mustSatisfy(input -> false)
            .orThrowExceptionWith(input -> "not legal")
            .throwIfNotValidOrReturnOptional())
        .isInstanceOf(ValidationException.class).hasMessage("not legal");
  }

  @Test
  void validationFailsForNullInputThrowsCustomExceptionUsingValidateThenReturnOptional() {
    assertThatThrownBy(() ->
        forInput(null)
            .mustSatisfy(input -> false)
            .orElseThrow(IllegalStateException::new)
            .withExceptionMessage(input -> "not legal")
            .throwIfNotValidOrReturnOptional())
        .isInstanceOf(IllegalStateException.class).hasMessage("not legal");
  }

  @Test
  void validationFailsForNullInputThrowsValidationExceptionUsingValidate() {
    assertThatThrownBy(() ->
        forInput(null)
            .mustSatisfy(input -> false)
            .orThrowExceptionWith(input -> "not legal")
            .throwIfNotValid())
        .isInstanceOf(ValidationException.class).hasMessage("not legal");
  }

  @Test
  void validationFailsForNullInputThrowsCustomExceptionUsingValidate() {
    assertThatThrownBy(() ->
        forInput(null)
            .mustSatisfy(input -> false)
            .orElseThrow(IllegalStateException::new)
            .withExceptionMessage(input -> "not legal")
            .throwIfNotValid())
        .isInstanceOf(IllegalStateException.class).hasMessage("not legal");
  }

  @Test
  void validationFailsForNullInputListAsFailedValidation() {
    assertThat(
        forInput(null)
            .mustSatisfy(x -> false).orElseThrow(IllegalStateException::new)
            .withExceptionMessage(input -> "Is not even, for input: " + input)
            .allExceptionMessages()
    ).containsOnly("Is not even, for input: null");

    assertThat(
        forInput(null)
            .mustSatisfy(x -> false)
            .orThrowExceptionWith(input -> "Is not even, for input: " + input)
            .allExceptionMessages()
    ).containsOnly("Is not even, for input: null");
  }
}
