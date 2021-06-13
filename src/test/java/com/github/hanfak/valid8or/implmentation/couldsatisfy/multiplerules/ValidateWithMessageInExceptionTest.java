package com.github.hanfak.valid8or.implmentation.couldsatisfy.multiplerules;

import com.github.hanfak.valid8or.implmentation.domain.ValidationException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import testinfrastructure.TestFixtures;

import static com.github.hanfak.valid8or.api.Valid8orCouldSatisfyAllRules.forInput;

class ValidateWithMessageInExceptionTest extends TestFixtures {

  @Nested
  class ReturnsInputAndNoExceptionThrownWhenInputIsValid {

    @ParameterizedTest
    @ValueSource(ints = {4, 3, 2})
    void usingCustomExceptionWithCustomMessageUsingInput(int value) {
      assertThat(
          forInput(value)
              .couldSatisfy(isEven).orThrow(IllegalStateException::new)
              .withMessage(input -> "Is not even, for input: " + input)
              .orSatisfies(isGreaterThan2).orThrow(IllegalArgumentException::new)
              .withMessage(input -> "Is not greater than 2, for input: " + input)
              .validate()
      ).isEqualTo(value);

      assertThat(
          forInput(value)
              .couldSatisfy(isGreaterThan2).orThrow(IllegalArgumentException::new)
              .withMessage(input -> "Is not greater than 2, for input: " + input)
              .orSatisfies(isEven).orThrow(IllegalStateException::new)
              .withMessage(input -> "Is not even, for input: " + input)
              .validate()
      ).isEqualTo(value);
    }

    @ParameterizedTest
    @ValueSource(ints = {4, 3, 2})
    void usingCustomMessageAndNoCustomException(int value) {
      assertThat(
          forInput(value)
              .couldSatisfy(isEven)
              .butWas(input -> "Is not even, for input: " + input)
              .orSatisfies(isGreaterThan2)
              .butWas(input -> "Is not greater than 2, for input: " + input)
              .validate()
      ).isEqualTo(value);

      assertThat(
          forInput(value)
              .couldSatisfy(isGreaterThan2)
              .butWas(input -> "Is not greater than 2, for input: " + input)
              .orSatisfies(isEven)
              .butWas(input -> "Is not even, for input: " + input)
              .validate()
      ).isEqualTo(value);
    }
  }

  @Nested
  class ThrowsAnExceptionWhenInputIsInvalid {

    @Test
    void usingCustomExceptionWithCustomMessageUsingInputThrowsCustomException() {
      assertThatThrownBy(() ->
          forInput(1)
              .couldSatisfy(isEven).orThrow(IllegalStateException::new)
              .withMessage(input -> "Is not even, for input: " + input)
              .orSatisfies(isGreaterThan2).orThrow(IllegalArgumentException::new)
              .withMessage(input -> "Is not greater than 2, for input: " + input)
              .validate()
      )
          .hasMessage("Is not even, for input: 1")
          .isInstanceOf(IllegalStateException.class);

      assertThatThrownBy(() ->
          forInput(1)
              .couldSatisfy(isGreaterThan2).orThrow(IllegalArgumentException::new)
              .withMessage(input -> "Is not greater than 2, for input: " + input)
              .orSatisfies(isEven).orThrow(IllegalStateException::new)
              .withMessage(input -> "Is not even, for input: " + input)
              .validate()
      )
          .hasMessage("Is not greater than 2, for input: 1")
          .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void usingCustomMessageAndNoCustomExceptionWillThrowValidationException() {
      assertThatThrownBy(() ->
          forInput(1)
              .couldSatisfy(isEven)
              .butWas(input -> "Is not even, for input: " + input)
              .orSatisfies(isGreaterThan2)
              .butWas(input -> "Is not greater than 2, for input: " + input)
              .validate()
      )
          .hasMessage("Is not even, for input: 1")
          .isInstanceOf(ValidationException.class);

      assertThatThrownBy(() ->
          forInput(1)
              .couldSatisfy(isGreaterThan2)
              .butWas(input -> "Is not greater than 2, for input: " + input)
              .orSatisfies(isEven)
              .butWas(input -> "Is not even, for input: " + input)
              .validate()
      )
          .hasMessage("Is not greater than 2, for input: 1")
          .isInstanceOf(ValidationException.class);
    }
  }
}
