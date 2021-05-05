package com.github.hanfak.valid8or.implmentation.couldsatisfy.singlerule;

import testinfrastructure.TestFixtures;
import com.github.hanfak.valid8or.implmentation.domain.ValidationException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.github.hanfak.valid8or.api.Valid8or.forInput;

public class ValidateWithMessageInExceptionReturnsOptionalTest extends TestFixtures {

  @Nested
  class ReturnsInputAsOptionalAndNoExceptionThrownWhenInputIsValid {
    @Test
    void usingCustomExceptionWithNoMessage() {
      assertThat(
          forInput(4)
              .couldSatisfy(isEven).ifNotThrowAn(IllegalStateException::new)
              .validateThenReturnOptional()
      ).isEqualTo(Optional.of(4));
    }

    @Test
    void usingCustomExceptionWithMessage() {
      assertThat(
          forInput(4)
              .couldSatisfy(isEven).ifNotThrowAn(() -> new IllegalStateException("Some Exception"))
              .validateThenReturnOptional()
      ).isEqualTo(Optional.of(4));
    }

    @Test
    void usingCustomExceptionWithCustomMessageUsingInput() {
      assertThat(
          forInput(4)
              .couldSatisfy(isEven).ifNotThrow(IllegalStateException::new)
              .withMessage(input -> "Is not even, for input: " + input)
              .validateThenReturnOptional()
      ).isEqualTo(Optional.of(4));
    }

    @Test
    void usingCustomMessageAndNoCustomException() {
      assertThat(
          forInput(4)
              .couldSatisfy(isEven)
              .butWas(input -> "Is not even, for input: " + input)
              .validateThenReturnOptional()
      ).isEqualTo(Optional.of(4));
    }
  }

  @Nested
  class ThrowsAnExceptionWhenInputIsInvalid {

    @Test
    void usingCustomExceptionWithNoMessageThrowsCustomException() {
      assertThatThrownBy(() ->
          forInput(3)
              .couldSatisfy(isEven).ifNotThrowAn(IllegalStateException::new)
              .validateThenReturnOptional()
      )
          .hasMessage(null)
          .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void usingCustomExceptionWithMessageThrowsCustomException() {
      assertThatThrownBy(() ->
          forInput(3)
              .couldSatisfy(isEven).ifNotThrowAn(() -> new IllegalStateException("Some Exception"))
              .validateThenReturnOptional()
      )
          .hasMessage("Some Exception")
          .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void usingCustomExceptionWithCustomMessageUsingInputThrowsCustomException() {
      assertThatThrownBy(() ->
          forInput(3)
              .couldSatisfy(isEven).ifNotThrow(IllegalStateException::new)
              .withMessage(input -> "Is not even, for input: " + input)
              .validateThenReturnOptional()
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
              .validateThenReturnOptional()
      )
          .hasMessage("Is not even, for input: 3")
          .isInstanceOf(ValidationException.class);
    }
  }
}
