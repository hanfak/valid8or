package com.github.hanfak.valid8or.implmentation.couldsatisfy.multiplerule;

import com.github.hanfak.valid8or.implmentation.domain.ValidationException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import testinfrastructure.TestFixtures;

import java.util.Optional;

import static com.github.hanfak.valid8or.api.Valid8or.forInput;

public class ValidateWithMessageInExceptionReturnsOptionalTest extends TestFixtures {

  @Nested
  class ReturnsInputAsOptionalAndNoExceptionThrownWhenInputIsValid {

    @ParameterizedTest
    @ValueSource(ints = {4, 3, 2})
    void usingCustomExceptionWithNoMessage(int value) {
      assertThat(
          forInput(value)
              .couldSatisfy(isEven).ifNotThrowAn(IllegalStateException::new)
              .orSatisfies(isGreaterThan2).ifNotThrowAn(IllegalArgumentException::new)
              .validateThenReturnOptional()
      ).isEqualTo(Optional.of(value));

      assertThat(
          forInput(value)
              .couldSatisfy(isGreaterThan2).ifNotThrowAn(IllegalArgumentException::new)
              .orSatisfies(isEven).ifNotThrowAn(IllegalStateException::new)
              .validateThenReturnOptional()
      ).isEqualTo(Optional.of(value));
    }

    @ParameterizedTest
    @ValueSource(ints = {4, 3, 2})
    void usingCustomExceptionWithMessage() {
      assertThat(
          forInput(4)
              .couldSatisfy(isEven).ifNotThrowAn(() -> new IllegalStateException("Some Exception"))
              .orSatisfies(isGreaterThan2).ifNotThrowAn(() -> new IllegalArgumentException("Some Other Exception"))
              .validateThenReturnOptional()
      ).isEqualTo(Optional.of(4));

      assertThat(
          forInput(4)
              .couldSatisfy(isGreaterThan2).ifNotThrowAn(() -> new IllegalArgumentException("Some Other Exception"))
              .orSatisfies(isEven).ifNotThrowAn(() -> new IllegalStateException("Some Exception"))
              .validateThenReturnOptional()
      ).isEqualTo(Optional.of(4));
    }

    @ParameterizedTest
    @ValueSource(ints = {4, 3, 2})
    void usingCustomExceptionWithCustomMessageUsingInput() {
      assertThat(
          forInput(4)
              .couldSatisfy(isEven).ifNotThrow(IllegalStateException::new)
              .withMessage(input -> "Is not even, for input: " + input)
              .orSatisfies(isGreaterThan2).ifNotThrow(IllegalArgumentException::new)
              .withMessage(input -> "Is not greater than 2, for input: " + input)
              .validateThenReturnOptional()
      ).isEqualTo(Optional.of(4));

      assertThat(
          forInput(4)
              .couldSatisfy(isGreaterThan2).ifNotThrow(IllegalArgumentException::new)
              .withMessage(input -> "Is not greater than 2, for input: " + input)
              .orSatisfies(isEven).ifNotThrow(IllegalStateException::new)
              .withMessage(input -> "Is not even, for input: " + input)
              .validateThenReturnOptional()
      ).isEqualTo(Optional.of(4));
    }

    @ParameterizedTest
    @ValueSource(ints = {4, 3, 2})
    void usingCustomMessageAndNoCustomException() {
      assertThat(
          forInput(4)
              .couldSatisfy(isEven)
              .butWas(input -> "Is not even, for input: " + input)
              .orSatisfies(isGreaterThan2)
              .butWas(input -> "Is not greater than 2, for input: " + input)
              .validateThenReturnOptional()
      ).isEqualTo(Optional.of(4));

      assertThat(
          forInput(4)
              .couldSatisfy(isGreaterThan2)
              .butWas(input -> "Is not greater than 2, for input: " + input)
              .orSatisfies(isEven)
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
          forInput(1)
              .couldSatisfy(isEven).ifNotThrowAn(IllegalStateException::new)
              .orSatisfies(isGreaterThan2).ifNotThrowAn(IllegalArgumentException::new)
              .validateThenReturnOptional()
      )
          .hasMessage(null)
          .isInstanceOf(IllegalStateException.class);

      assertThatThrownBy(() ->
          forInput(1)
              .couldSatisfy(isGreaterThan2).ifNotThrowAn(IllegalArgumentException::new)
              .orSatisfies(isEven).ifNotThrowAn(IllegalStateException::new)
              .validateThenReturnOptional()
      )
          .hasMessage(null)
          .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void usingCustomExceptionWithMessageThrowsCustomException() {
      assertThatThrownBy(() ->
          forInput(1)
              .couldSatisfy(isEven).ifNotThrowAn(() -> new IllegalStateException("Some Exception"))
              .orSatisfies(isGreaterThan2).ifNotThrowAn(() -> new IllegalArgumentException("Some Other Exception"))
              .validateThenReturnOptional()
      )
          .hasMessage("Some Exception")
          .isInstanceOf(IllegalStateException.class);

      assertThatThrownBy(() ->
          forInput(1)
              .couldSatisfy(isGreaterThan2).ifNotThrowAn(() -> new IllegalArgumentException("Some Other Exception"))
              .orSatisfies(isEven).ifNotThrowAn(() -> new IllegalStateException("Some Exception"))
              .validateThenReturnOptional()
      )
          .hasMessage("Some Other Exception")
          .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void usingCustomExceptionWithCustomMessageUsingInputThrowsCustomException() {
      assertThatThrownBy(() ->
          forInput(1)
              .couldSatisfy(isEven).ifNotThrow(IllegalStateException::new)
              .withMessage(input -> "Is not even, for input: " + input)
              .orSatisfies(isGreaterThan2).ifNotThrow(IllegalArgumentException::new)
              .withMessage(input -> "Is not greater than 2, for input: " + input)
              .validateThenReturnOptional()
      )
          .hasMessage("Is not even, for input: 1")
          .isInstanceOf(IllegalStateException.class);

      assertThatThrownBy(() ->
          forInput(1)
              .couldSatisfy(isGreaterThan2).ifNotThrow(IllegalArgumentException::new)
              .withMessage(input -> "Is not greater than 2, for input: " + input)
              .orSatisfies(isEven).ifNotThrow(IllegalStateException::new)
              .withMessage(input -> "Is not even, for input: " + input)
              .validateThenReturnOptional()
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
              .validateThenReturnOptional()
      )
          .hasMessage("Is not even, for input: 1")
          .isInstanceOf(ValidationException.class);

      assertThatThrownBy(() ->
          forInput(1)
              .couldSatisfy(isGreaterThan2)
              .butWas(input -> "Is not greater than 2, for input: " + input)
              .orSatisfies(isEven)
              .butWas(input -> "Is not even, for input: " + input)
              .validateThenReturnOptional()
      )
          .hasMessage("Is not greater than 2, for input: 1")
          .isInstanceOf(ValidationException.class);
    }
  }
}
