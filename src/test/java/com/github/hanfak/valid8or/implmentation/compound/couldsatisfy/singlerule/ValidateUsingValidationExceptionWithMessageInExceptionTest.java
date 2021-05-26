package com.github.hanfak.valid8or.implmentation.compound.couldsatisfy.singlerule;

import testinfrastructure.TestFixtures;
import com.github.hanfak.valid8or.implmentation.domain.ValidationException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.github.hanfak.valid8or.api.Valid8or.forInput;

// Despite using ifNotThrown or ifNotThrowAn, with butWas will not throw the custom exception defined
// when the input is invalid. Too avoid wastage, best to use butWas without defining custom exception methods
public class ValidateUsingValidationExceptionWithMessageInExceptionTest extends TestFixtures {

  @Nested
  class ReturnsInputAndNoExceptionThrownWhenInputIsValid {

    @Test
    void usingCustomExceptionWithNoMessage() {
      assertThat(
          forInput(4)
              .couldSatisfy(isEven).ifNotThrowAn(IllegalStateException::new)
              .orElseThrowValidationException()
      ).isEqualTo(4);
    }

    @Test
    void usingCustomExceptionWithMessage() {
      assertThat(
          forInput(4)
              .couldSatisfy(isEven).ifNotThrowAn(() -> new IllegalStateException("Some Exception"))
              .orElseThrowValidationException()
      ).isEqualTo(4);
    }

    @Test
    void usingCustomExceptionWithCustomMessageUsingInput() {
      assertThat(
          forInput(4)
              .couldSatisfy(isEven).ifNotThrow(IllegalStateException::new)
              .withMessage(input -> "Is not even, for input: " + input)
              .orElseThrowValidationException()
      ).isEqualTo(4);
    }

    @Test
    void usingCustomMessageAndNoCustomException() {
      assertThat(
          forInput(4)
              .couldSatisfy(isEven)
              .butWas(input -> "Is not even, for input: " + input)
              .orElseThrowValidationException()
      ).isEqualTo(4);
    }
  }

  @Nested
  class ThrowsAnExceptionWhenInputIsInvalid {

    @Test
    void usingCustomExceptionWithNoMessageThrowsCustomException() {
      assertThatThrownBy(() ->
          forInput(3)
              .couldSatisfy(isEven).ifNotThrowAn(IllegalStateException::new)
              .orElseThrowValidationException()
      )
          .hasMessage("For input: '3', the following problems occurred: 'null'")
          .isInstanceOf(ValidationException.class);
    }

    @Test
    void usingCustomExceptionWithMessageThrowsCustomException() {
      assertThatThrownBy(() ->
          forInput(3)
              .couldSatisfy(isEven).ifNotThrowAn(() -> new IllegalStateException("Some Exception"))
              .orElseThrowValidationException()
      )
          .hasMessage("For input: '3', the following problems occurred: 'Some Exception'")
          .isInstanceOf(ValidationException.class);
    }

    @Test
    void usingCustomExceptionWithCustomMessageUsingInputThrowsCustomException() {
      assertThatThrownBy(() ->
          forInput(3)
              .couldSatisfy(isEven).ifNotThrow(IllegalStateException::new)
              .withMessage(input -> "Is not even, for input: " + input)
              .orElseThrowValidationException()
      )
          .hasMessage("For input: '3', the following problems occurred: 'Is not even, for input: 3'")
          .isInstanceOf(ValidationException.class);
    }

    @Test
    void usingCustomMessageAndNoCustomExceptionWillThrowValidationException() {
      assertThatThrownBy(() ->
          forInput(3)
              .couldSatisfy(isEven)
              .butWas(input -> "Is not even, for input: " + input)
              .orElseThrowValidationException()
      )
          .hasMessage("For input: '3', the following problems occurred: 'Is not even, for input: 3'")
          .isInstanceOf(ValidationException.class);
    }
  }
}
