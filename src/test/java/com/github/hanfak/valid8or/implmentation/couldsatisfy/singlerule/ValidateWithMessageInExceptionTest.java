package com.github.hanfak.valid8or.implmentation.couldsatisfy.singlerule;

import com.github.hanfak.valid8or.implmentation.domain.ValidationException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import testinfrastructure.TestFixtures;

import static com.github.hanfak.valid8or.api.Valid8orCouldSatisfyAllRules.forInput;


class ValidateWithMessageInExceptionTest extends TestFixtures {

  @Nested
  class ReturnsInputAndNoExceptionThrownWhenInputIsValid {

    @Test
    void usingCustomExceptionWithCustomMessageUsingInput() {
      assertThat(
          forInput(4)
              .couldSatisfy(isEven).orThrow(IllegalStateException::new)
              .withMessage(input -> "Is not even, for input: " + input)
              .validate()
      ).isEqualTo(4);
    }

    @Test
    void usingCustomMessageAndNoCustomException() {
      assertThat(
          forInput(4)
              .couldSatisfy(isEven)
              .butWas(input -> "Is not even, for input: " + input)
              .validate()
      ).isEqualTo(4);
    }
  }

  @Nested
  class ThrowsAnExceptionWhenInputIsInvalid {


    @Test
    void usingCustomExceptionWithCustomMessageUsingInputThrowsCustomException() {
      assertThatThrownBy(() ->
          forInput(3)
              .couldSatisfy(isEven).orThrow(IllegalStateException::new)
              .withMessage(input -> "Is not even, for input: " + input)
              .validate()
      )
          .hasMessage("Is not even, for input: 3")
          .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void usingCustomMessageAndNoCustomExceptionWillThrowValidationException() {
      assertThatThrownBy(() ->
          forInput(3)
              .couldSatisfy(isEven)
              .butWas(input -> "Is not even, for input: " + input)
              .validate()
      )
          .hasMessage("Is not even, for input: 3")
          .isInstanceOf(ValidationException.class);
    }
  }
}
