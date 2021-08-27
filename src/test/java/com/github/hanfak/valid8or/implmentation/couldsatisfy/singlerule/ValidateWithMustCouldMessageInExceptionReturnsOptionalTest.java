package com.github.hanfak.valid8or.implmentation.couldsatisfy.singlerule;

import com.github.hanfak.valid8or.implmentation.ValidationException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import testinfrastructure.TestFixtures;

import java.util.Optional;

import static com.github.hanfak.valid8or.implmentation.Valid8orCouldSatisfyAllRules.forInput;

class ValidateWithMustCouldMessageInExceptionReturnsOptionalTest extends TestFixtures {

  @Nested
  class ReturnsInputAsOptionalAndNoExceptionThrownWhenInputIsValid {

    @Test
    void usingCustomExceptionWithCustomMessageUsingInput() {
      assertThat(
          forInput(4)
              .couldSatisfy(isEven).orElseThrow(IllegalStateException::new)
              .withExceptionMessage(input -> "Is not even, for input: " + input)
              .throwIfNotValidOrReturnOptional()
      ).isEqualTo(Optional.of(4));
    }

    @Test
    void usingCustomMessageAndNoCustomException() {
      assertThat(
          forInput(4)
              .couldSatisfy(isEven)
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
          forInput(3)
              .couldSatisfy(isEven).orElseThrow(IllegalStateException::new)
              .withExceptionMessage(input -> "Is not even, for input: " + input)
              .throwIfNotValidOrReturnOptional()
      )
          .hasMessage("Is not even, for input: 3")
          .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void usingCustomMessageAndNoCustomExceptionWillThrowValidationException() {
      assertThatThrownBy(() ->
          forInput(3)
              .couldSatisfy(isEven)
              .orThrowExceptionWith(input -> "Is not even, for input: " + input)
              .throwIfNotValidOrReturnOptional()
      )
          .hasMessage("Is not even, for input: 3")
          .isInstanceOf(ValidationException.class);
    }
  }
}
