package com.github.hanfak.valid8or.implmentation.couldsatisfy.multiplerules;

import com.github.hanfak.valid8or.implmentation.ValidationException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import testinfrastructure.TestFixtures;

import static com.github.hanfak.valid8or.implmentation.Valid8orCouldSatisfyAllRules.forInput;

class ValidateWithMustCouldMessageInExceptionTest extends TestFixtures {

  @Nested
  class ReturnsInputAndNoExceptionThrownWhenInputIsValid {

    @ParameterizedTest
    @ValueSource(ints = {4, 3, 2})
    void usingCustomExceptionWithCustomMessageUsingInput(int value) {
      assertThat(
          forInput(value)
              .couldSatisfy(isEven).orElseThrow(IllegalStateException::new)
              .withMessage(input -> "Is not even, for input: " + input)
              .or(isGreaterThan2).orElseThrow(IllegalArgumentException::new)
              .withMessage(input -> "Is not greater than 2, for input: " + input)
              .isValidOrThrow()
      ).isEqualTo(value);

      assertThat(
          forInput(value)
              .couldSatisfy(isGreaterThan2).orElseThrow(IllegalArgumentException::new)
              .withMessage(input -> "Is not greater than 2, for input: " + input)
              .or(isEven).orElseThrow(IllegalStateException::new)
              .withMessage(input -> "Is not even, for input: " + input)
              .isValidOrThrow()
      ).isEqualTo(value);
    }

    @ParameterizedTest
    @ValueSource(ints = {4, 3, 2})
    void usingCustomMessageAndNoCustomException(int value) {
      assertThat(
          forInput(value)
              .couldSatisfy(isEven)
              .orThrowExceptionWith(input -> "Is not even, for input: " + input)
              .or(isGreaterThan2)
              .orThrowExceptionWith(input -> "Is not greater than 2, for input: " + input)
              .isValidOrThrow()
      ).isEqualTo(value);

      assertThat(
          forInput(value)
              .couldSatisfy(isGreaterThan2)
              .orThrowExceptionWith(input -> "Is not greater than 2, for input: " + input)
              .or(isEven)
              .orThrowExceptionWith(input -> "Is not even, for input: " + input)
              .isValidOrThrow()
      ).isEqualTo(value);
    }
  }

  @Nested
  class ThrowsAnExceptionWhenInputIsInvalid {

    @Test
    void usingCustomExceptionWithCustomMessageUsingInputThrowsCustomException() {
      assertThatThrownBy(() ->
          forInput(1)
              .couldSatisfy(isEven).orElseThrow(IllegalStateException::new)
              .withMessage(input -> "Is not even, for input: " + input)
              .or(isGreaterThan2).orElseThrow(IllegalArgumentException::new)
              .withMessage(input -> "Is not greater than 2, for input: " + input)
              .isValidOrThrow()
      )
          .hasMessage("Is not even, for input: 1")
          .isInstanceOf(IllegalStateException.class);

      assertThatThrownBy(() ->
          forInput(1)
              .couldSatisfy(isGreaterThan2).orElseThrow(IllegalArgumentException::new)
              .withMessage(input -> "Is not greater than 2, for input: " + input)
              .or(isEven).orElseThrow(IllegalStateException::new)
              .withMessage(input -> "Is not even, for input: " + input)
              .isValidOrThrow()
      )
          .hasMessage("Is not greater than 2, for input: 1")
          .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void usingCustomMessageAndNoCustomExceptionWillThrowValidationException() {
      assertThatThrownBy(() ->
          forInput(1)
              .couldSatisfy(isEven)
              .orThrowExceptionWith(input -> "Is not even, for input: " + input)
              .or(isGreaterThan2)
              .orThrowExceptionWith(input -> "Is not greater than 2, for input: " + input)
              .isValidOrThrow()
      )
          .hasMessage("Is not even, for input: 1")
          .isInstanceOf(ValidationException.class);

      assertThatThrownBy(() ->
          forInput(1)
              .couldSatisfy(isGreaterThan2)
              .orThrowExceptionWith(input -> "Is not greater than 2, for input: " + input)
              .or(isEven)
              .orThrowExceptionWith(input -> "Is not even, for input: " + input)
              .isValidOrThrow()
      )
          .hasMessage("Is not greater than 2, for input: 1")
          .isInstanceOf(ValidationException.class);
    }
  }
}
