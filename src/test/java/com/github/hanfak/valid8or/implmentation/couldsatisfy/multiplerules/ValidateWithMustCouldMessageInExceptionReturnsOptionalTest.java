package com.github.hanfak.valid8or.implmentation.couldsatisfy.multiplerules;

import com.github.hanfak.valid8or.implmentation.ValidationException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import testinfrastructure.TestFixtures;

import java.util.Optional;

import static com.github.hanfak.valid8or.implmentation.Valid8orCouldSatisfyAllRules.forInput;

class ValidateWithMustCouldMessageInExceptionReturnsOptionalTest extends TestFixtures {

  @Nested
  class ReturnsInputAsOptionalAndNoExceptionThrownWhenInputIsValid {

    @ParameterizedTest
    @ValueSource(ints = {4, 3, 2})
    void usingCustomExceptionWithCustomMessageUsingInput() {
      assertThat(
          forInput(4)
              .couldSatisfy(isEven).orElseThrow(IllegalStateException::new)
              .withExceptionMessage(input -> "Is not even, for input: " + input)
              .or(isGreaterThan2).orElseThrow(IllegalArgumentException::new)
              .withExceptionMessage(input -> "Is not greater than 2, for input: " + input)
              .throwIfNotValidOrReturnOptional()
      ).isEqualTo(Optional.of(4));

      assertThat(
          forInput(4)
              .couldSatisfy(isGreaterThan2).orElseThrow(IllegalArgumentException::new)
              .withExceptionMessage(input -> "Is not greater than 2, for input: " + input)
              .or(isEven).orElseThrow(IllegalStateException::new)
              .withExceptionMessage(input -> "Is not even, for input: " + input)
              .throwIfNotValidOrReturnOptional()
      ).isEqualTo(Optional.of(4));
    }

    @ParameterizedTest
    @ValueSource(ints = {4, 3, 2})
    void usingCustomMessageAndNoCustomException() {
      assertThat(
          forInput(4)
              .couldSatisfy(isEven)
              .orThrowExceptionWith(input -> "Is not even, for input: " + input)
              .or(isGreaterThan2)
              .orThrowExceptionWith(input -> "Is not greater than 2, for input: " + input)
              .throwIfNotValidOrReturnOptional()
      ).isEqualTo(Optional.of(4));

      assertThat(
          forInput(4)
              .couldSatisfy(isGreaterThan2)
              .orThrowExceptionWith(input -> "Is not greater than 2, for input: " + input)
              .or(isEven)
              .orThrowExceptionWith(input -> "Is not even, for input: " + input)
              .throwIfNotValidOrReturnOptional()
      ).isEqualTo(Optional.of(4));
    }
  }

  @Nested
  class ThrowsAnExceptionWhenInputIsInvalid {

    @Test
    void usingCustomExceptionWithCustomMessageUsingInputThrowsCustomException() {
      assertThatThrownBy(() ->
          forInput(1)
              .couldSatisfy(isEven).orElseThrow(IllegalStateException::new)
              .withExceptionMessage(input -> "Is not even, for input: " + input)
              .or(isGreaterThan2).orElseThrow(IllegalArgumentException::new)
              .withExceptionMessage(input -> "Is not greater than 2, for input: " + input)
              .throwIfNotValidOrReturnOptional()
      )
          .hasMessage("Is not even, for input: 1")
          .isInstanceOf(IllegalStateException.class);

      assertThatThrownBy(() ->
          forInput(1)
              .couldSatisfy(isGreaterThan2).orElseThrow(IllegalArgumentException::new)
              .withExceptionMessage(input -> "Is not greater than 2, for input: " + input)
              .or(isEven).orElseThrow(IllegalStateException::new)
              .withExceptionMessage(input -> "Is not even, for input: " + input)
              .throwIfNotValidOrReturnOptional()
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
              .throwIfNotValidOrReturnOptional()
      )
          .hasMessage("Is not even, for input: 1")
          .isInstanceOf(ValidationException.class);

      assertThatThrownBy(() ->
          forInput(1)
              .couldSatisfy(isGreaterThan2)
              .orThrowExceptionWith(input -> "Is not greater than 2, for input: " + input)
              .or(isEven)
              .orThrowExceptionWith(input -> "Is not even, for input: " + input)
              .throwIfNotValidOrReturnOptional()
      )
          .hasMessage("Is not greater than 2, for input: 1")
          .isInstanceOf(ValidationException.class);
    }
  }
}
