package com.github.hanfak.valid8or.implmentation.mustsatisfy.singlerule;

import com.github.hanfak.valid8or.implmentation.domain.ValidationException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import testinfrastructure.TestFixtures;

import java.util.Optional;

import static com.github.hanfak.valid8or.api.Valid8or.forInput;

public class ValidateWithMessageInExceptionReturnsOptionalTest extends TestFixtures {

  @Nested
  class ReturnsInputAsOptionalAndNoExceptionThrownWhenInputIsValid {
    @Test
    void usingCustomExceptionWithNoMessage() {
      assertThat(
          forInput(4)
              .mustSatisfy(isEven).ifNotWillThrowAn(IllegalStateException::new)
              .validateThenReturnOptional()
      ).isEqualTo(Optional.of(4));
    }

    @Test
    void usingCustomExceptionhasMessage() {
      assertThat(
          forInput(4)
              .mustSatisfy(isEven).ifNotWillThrowAn(() -> new IllegalStateException("Some Exception"))
              .validateThenReturnOptional()
      ).isEqualTo(Optional.of(4));
    }

    @Test
    void usingCustomExceptionWithCustomMessageUsingInput() {
      assertThat(
          forInput(4)
              .mustSatisfy(isEven).ifNotWillThrow(IllegalStateException::new)
              .hasMessage(input -> "Is not even, for input: " + input)
              .validateThenReturnOptional()
      ).isEqualTo(Optional.of(4));
    }

    @Test
    void usingCustomMessageAndNoCustomException() {
      assertThat(
          forInput(4)
              .mustSatisfy(isEven)
              .butIs(input -> "Is not even, for input: " + input)
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
              .mustSatisfy(isEven).ifNotWillThrowAn(IllegalStateException::new)
              .validateThenReturnOptional()
      )
          .hasMessage(null)
          .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void usingCustomExceptionhasMessageThrowsCustomException() {
      assertThatThrownBy(() ->
          forInput(3)
              .mustSatisfy(isEven).ifNotWillThrowAn(() -> new IllegalStateException("Some Exception"))
              .validateThenReturnOptional()
      )
          .hasMessage("Some Exception")
          .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void usingCustomExceptionWithCustomMessageUsingInputThrowsCustomException() {
      assertThatThrownBy(() ->
          forInput(3)
              .mustSatisfy(isEven).ifNotWillThrow(IllegalStateException::new)
              .hasMessage(input -> "Is not even, for input: " + input)
              .validateThenReturnOptional()
      )
          .hasMessage("Is not even, for input: 3")
          .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void usingCustomMessageAndNoCustomExceptionWillThrowValidationException() {
      assertThatThrownBy(() ->
          forInput(3)
              .mustSatisfy(isEven)
              .butIs(input -> "Is not even, for input: " + input)
              .validateThenReturnOptional()
      )
          .hasMessage("Is not even, for input: 3")
          .isInstanceOf(ValidationException.class);
    }
  }
}
