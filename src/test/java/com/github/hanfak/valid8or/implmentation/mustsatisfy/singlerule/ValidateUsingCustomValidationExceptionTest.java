package com.github.hanfak.valid8or.implmentation.mustsatisfy.singlerule;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import testinfrastructure.CustomException;
import testinfrastructure.TestFixtures;

import static com.github.hanfak.valid8or.api.Valid8or.forInput;
import static java.lang.String.format;

public class ValidateUsingCustomValidationExceptionTest extends TestFixtures {

  @Nested
  class ReturnsInputAndNoExceptionThrownWhenInputIsValid {

    @Test
    void usingCustomExceptionWithNoMessage() {
      assertThat(
          forInput(4)
              .mustSatisfy(isEven).ifNotWillThrowAn(IllegalStateException::new)
              .orElseThrowValidationException(CustomException::new,
                  (input, errors) -> format("All problems are, for input: %s, with messages: '%s'", input, errors))
      ).isEqualTo(4);
    }

    @Test
    void usingCustomExceptionhasMessage() {
      assertThat(
          forInput(4)
              .mustSatisfy(isEven).ifNotWillThrowAn(() -> new IllegalStateException("Some Exception"))
              .orElseThrowValidationException(CustomException::new,
                  (input, errors) -> format("All problems are, for input: %s, with messages: '%s'", input, errors))
      ).isEqualTo(4);
    }

    @Test
    void usingCustomExceptionWithCustomMessageUsingInput() {
      assertThat(
          forInput(4)
              .mustSatisfy(isEven).ifNotWillThrow(IllegalStateException::new)
              .hasMessage(input -> "Is not even, for input: " + input)
              .orElseThrowValidationException(CustomException::new,
                  (input, errors) -> format("All problems are, for input: %s, with messages: '%s'", input, errors))
      ).isEqualTo(4);
    }

    @Test
    void usingCustomMessageAndNoCustomException() {
      assertThat(
          forInput(4)
              .mustSatisfy(isEven)
              .butIs(input -> "Is not even, for input: " + input)
              .orElseThrowValidationException(CustomException::new,
                  (input, errors) -> format("All problems are, for input: %s, with messages: '%s'", input, errors))
      ).isEqualTo(4);
    }
  }

  @Nested
  class ThrowsAnExceptionWhenInputIsInvalid {

    @Test
    void usingCustomExceptionWithNoMessageThrowsCustomException() {
      assertThatThrownBy(() ->
          forInput(3)
              .mustSatisfy(isEven).ifNotWillThrowAn(IllegalStateException::new)
              .orElseThrowValidationException(CustomException::new,
                  (input, errors) -> format("All problems are, for input: %s, with messages: '%s'", input, errors))
      )
          .hasMessage("All problems are, for input: 3, with messages: 'null'")
          .isInstanceOf(CustomException.class);
    }

    @Test
    void usingCustomExceptionhasMessageThrowsCustomException() {
      assertThatThrownBy(() ->
          forInput(3)
              .mustSatisfy(isEven).ifNotWillThrowAn(() -> new IllegalStateException("Some Exception"))
              .orElseThrowValidationException(CustomException::new,
                  (input, errors) -> format("All problems are, for input: %s, with messages: '%s'", input, errors))
      )
          .hasMessage("All problems are, for input: 3, with messages: 'Some Exception'")
          .isInstanceOf(CustomException.class);
    }

    @Test
    void usingCustomExceptionWithCustomMessageUsingInputThrowsCustomException() {
      assertThatThrownBy(() ->
          forInput(3)
              .mustSatisfy(isEven).ifNotWillThrow(IllegalStateException::new)
              .hasMessage(input -> "Is not even, for input: " + input)
              .orElseThrowValidationException(CustomException::new,
                  (input, errors) -> format("All problems are, for input: %s, with messages: '%s'", input, errors))
      )
          .hasMessage("All problems are, for input: 3, with messages: 'Is not even, for input: 3'")
          .isInstanceOf(CustomException.class);
    }

    @Test
    void usingCustomMessageAndNoCustomExceptionWillThrowValidationException() {
      assertThatThrownBy(() ->
          forInput(3)
              .mustSatisfy(isEven)
              .butIs(input -> "Is not even, for input: " + input)
              .orElseThrowValidationException(CustomException::new,
                  (input, errors) -> format("All problems are, for input: %s, with messages: '%s'", input, errors))
      )
          .hasMessage("All problems are, for input: 3, with messages: 'Is not even, for input: 3'")
          .isInstanceOf(CustomException.class);
    }
  }
}
