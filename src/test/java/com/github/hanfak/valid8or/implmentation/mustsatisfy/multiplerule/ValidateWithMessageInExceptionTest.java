package com.github.hanfak.valid8or.implmentation.mustsatisfy.multiplerule;

import com.github.hanfak.valid8or.implmentation.domain.ValidationException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import testinfrastructure.TestFixtures;

import static com.github.hanfak.valid8or.api.Valid8or.forInput;
import static java.lang.String.format;

class ValidateWithMessageInExceptionTest extends TestFixtures {

  @Nested
  class ReturnsInputAndNoExceptionThrownWhenInputIsValid {

    @Test
    void usingCustomExceptionWithNoMessage() {
      assertThat(
          forInput(4)
              .mustSatisfy(isEven).ifNotWillThrowAn(IllegalStateException::new)
              .andSatisfies(isGreaterThan2).ifNotWillThrowAn(IllegalArgumentException::new)
              .validate()
      ).isEqualTo(4);

      assertThat(
          forInput(4)
              .mustSatisfy(isGreaterThan2).ifNotWillThrowAn(IllegalArgumentException::new)
              .andSatisfies(isEven).ifNotWillThrowAn(IllegalStateException::new)
              .validate()
      ).isEqualTo(4);
    }

    @Test
    void usingCustomExceptionhasMessage() {
      assertThat(
          forInput(4)
              .mustSatisfy(isEven).ifNotWillThrowAn(() -> new IllegalStateException("Some Exception"))
              .andSatisfies(isGreaterThan2).ifNotWillThrowAn(() -> new IllegalArgumentException("Some Other Exception"))
              .validate()
      ).isEqualTo(4);

      assertThat(
          forInput(4)
              .mustSatisfy(isGreaterThan2).ifNotWillThrowAn(() -> new IllegalArgumentException("Some Other Exception"))
              .andSatisfies(isEven).ifNotWillThrowAn(() -> new IllegalStateException("Some Exception"))
              .validate()
      ).isEqualTo(4);
    }

    @Test
    void usingCustomExceptionWithCustomMessageUsingInput() {
      assertThat(
          forInput(4)
              .mustSatisfy(isEven).ifNotWillThrow(IllegalStateException::new)
              .hasMessage(input -> "Is not even, for input: " + input)
              .andSatisfies(isGreaterThan2).ifNotWillThrow(IllegalStateException::new)
              .hasMessage(input -> "Is not greater than 2, for input: " + input)
              .validate()
      ).isEqualTo(4);

      assertThat(
          forInput(4)
              .mustSatisfy(isGreaterThan2).ifNotWillThrow(IllegalStateException::new)
              .hasMessage(input -> "Is not greater than 2, for input: " + input)
              .andSatisfies(isEven).ifNotWillThrow(IllegalStateException::new)
              .hasMessage(input -> "Is not even, for input: " + input)
              .validate()
      ).isEqualTo(4);
    }

    @Test
    void usingCustomMessageAndNoCustomException() {
      assertThat(
          forInput(4)
              .mustSatisfy(isEven)
              .butIs(input -> "Is not even, for input: " + input)
              .andSatisfies(isGreaterThan2)
              .butIs(input -> "Is not greater than 2, for input: " + input)
              .validate()
      ).isEqualTo(4);

      assertThat(
          forInput(4)
              .mustSatisfy(isGreaterThan2)
              .butIs(input -> "Is not greater than 2, for input: " + input)
              .andSatisfies(isEven)
              .butIs(input -> "Is not even, for input: " + input)
              .validate()
      ).isEqualTo(4);
    }
  }

  @Nested
  class ThrowsAnExceptionWhenInputIsInvalid {

    @ParameterizedTest
    @ValueSource(ints = {3, 1})
    void usingCustomExceptionWithNoMessageThrowsCustomException(int value) {
      assertThatThrownBy(() ->
          forInput(value)
              .mustSatisfy(isEven).ifNotWillThrowAn(IllegalStateException::new)
              .andSatisfies(isGreaterThan2).ifNotWillThrowAn(IllegalArgumentException::new)
              .validate()
      )
          .hasMessage(null)
          .isInstanceOf(IllegalStateException.class);
      assertThatThrownBy(() ->
          forInput(2)
              .mustSatisfy(isEven).ifNotWillThrowAn(IllegalStateException::new)
              .andSatisfies(isGreaterThan2).ifNotWillThrowAn(IllegalArgumentException::new)
              .validate()
      )
          .hasMessage(null)
          .isInstanceOf(IllegalArgumentException.class);

      assertThatThrownBy(() ->
          forInput(3)
              .mustSatisfy(isGreaterThan2).ifNotWillThrowAn(IllegalArgumentException::new)
              .andSatisfies(isEven).ifNotWillThrowAn(IllegalStateException::new)
              .validate()
      )
          .hasMessage(null)
          .isInstanceOf(IllegalStateException.class);
      assertThatThrownBy(() ->
          forInput(2)
              .mustSatisfy(isGreaterThan2).ifNotWillThrowAn(IllegalArgumentException::new)
              .andSatisfies(isEven).ifNotWillThrowAn(IllegalStateException::new)
              .validate()
      )
          .hasMessage(null)
          .isInstanceOf(IllegalArgumentException.class);
      assertThatThrownBy(() ->
          forInput(1)
              .mustSatisfy(isGreaterThan2).ifNotWillThrowAn(IllegalArgumentException::new)
              .andSatisfies(isEven).ifNotWillThrowAn(IllegalStateException::new)
              .validate()
      )
          .hasMessage(null)
          .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(ints = {3,  1})
    void usingCustomExceptionhasMessageThrowsCustomException(int value) {
      assertThatThrownBy(() ->
          forInput(value)
              .mustSatisfy(isEven).ifNotWillThrowAn(() -> new IllegalStateException("Some Exception"))
              .andSatisfies(isGreaterThan2).ifNotWillThrowAn(() -> new IllegalArgumentException("Some Other Exception"))
              .validate()
      )
          .hasMessage("Some Exception")
          .isInstanceOf(IllegalStateException.class);
      assertThatThrownBy(() ->
          forInput(2)
              .mustSatisfy(isEven).ifNotWillThrowAn(() -> new IllegalStateException("Some Exception"))
              .andSatisfies(isGreaterThan2).ifNotWillThrowAn(() -> new IllegalArgumentException("Some Other Exception"))
              .validate()
      )
          .hasMessage("Some Other Exception")
          .isInstanceOf(IllegalArgumentException.class);

      assertThatThrownBy(() ->
          forInput(3)
              .mustSatisfy(isGreaterThan2).ifNotWillThrowAn(() -> new IllegalArgumentException("Some Other Exception"))
              .andSatisfies(isEven).ifNotWillThrowAn(() -> new IllegalStateException("Some Exception"))
              .validate()
      )
          .hasMessage("Some Exception")
          .isInstanceOf(IllegalStateException.class);
      assertThatThrownBy(() ->
          forInput(2)
              .mustSatisfy(isGreaterThan2).ifNotWillThrowAn(() -> new IllegalArgumentException("Some Other Exception"))
              .andSatisfies(isEven).ifNotWillThrowAn(() -> new IllegalStateException("Some Exception"))
              .validate()
      )
          .hasMessage("Some Other Exception")
          .isInstanceOf(IllegalArgumentException.class);
      assertThatThrownBy(() ->
          forInput(1)
              .mustSatisfy(isGreaterThan2).ifNotWillThrowAn(() -> new IllegalArgumentException("Some Other Exception"))
              .andSatisfies(isEven).ifNotWillThrowAn(() -> new IllegalStateException("Some Exception"))
              .validate()
      )
          .hasMessage("Some Other Exception")
          .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(ints = {3, 1})
    void usingCustomExceptionWithCustomMessageUsingInputThrowsCustomException(int value) {
      assertThatThrownBy(() ->
          forInput(value)
              .mustSatisfy(isEven).ifNotWillThrow(IllegalStateException::new)
              .hasMessage(input -> "Is not even, for input: " + input)
              .andSatisfies(isGreaterThan2).ifNotWillThrow(IllegalStateException::new)
              .hasMessage(input -> "Is not greater than 2, for input: " + input)
              .validate()
      )
          .hasMessage(format("Is not even, for input: %s", value))
          .isInstanceOf(IllegalStateException.class);
      assertThatThrownBy(() ->
          forInput(2)
              .mustSatisfy(isEven).ifNotWillThrow(IllegalStateException::new)
              .hasMessage(input -> "Is not even, for input: " + input)
              .andSatisfies(isGreaterThan2).ifNotWillThrow(IllegalArgumentException::new)
              .hasMessage(input -> "Is not greater than 2, for input: " + input)
              .validate()
      )
          .hasMessage(format("Is not greater than 2, for input: %s", 2))
          .isInstanceOf(IllegalArgumentException.class);

      assertThatThrownBy(() ->
          forInput(3)
              .mustSatisfy(isGreaterThan2).ifNotWillThrow(IllegalArgumentException::new)
              .hasMessage(input -> "Is not greater than 2, for input: " + input)
              .andSatisfies(isEven).ifNotWillThrow(IllegalStateException::new)
              .hasMessage(input -> "Is not even, for input: " + input)
              .validate()
      )
          .hasMessage(format("Is not even, for input: %s", 3))
          .isInstanceOf(IllegalStateException.class);
      assertThatThrownBy(() ->
          forInput(2)
              .mustSatisfy(isGreaterThan2).ifNotWillThrow(IllegalArgumentException::new)
              .hasMessage(input -> "Is not greater than 2, for input: " + input)
              .andSatisfies(isEven).ifNotWillThrow(IllegalStateException::new)
              .hasMessage(input -> "Is not even, for input: " + input)
              .validate()
      )
          .hasMessage(format("Is not greater than 2, for input: %s", 2))
          .isInstanceOf(IllegalArgumentException.class);
      assertThatThrownBy(() ->
          forInput(1)
              .mustSatisfy(isGreaterThan2).ifNotWillThrow(IllegalArgumentException::new)
              .hasMessage(input -> "Is not greater than 2, for input: " + input)
              .andSatisfies(isEven).ifNotWillThrow(IllegalStateException::new)
              .hasMessage(input -> "Is not even, for input: " + input)
              .validate()
      )
          .hasMessage(format("Is not greater than 2, for input: %s", 1))
          .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(ints = {3, 1})
    void usingCustomMessageAndNoCustomExceptionWillThrowValidationException(int value) {
      assertThatThrownBy(() ->
          forInput(value)
              .mustSatisfy(isEven)
              .butIs(input -> "Is not even, for input: " + input)
              .andSatisfies(isGreaterThan2)
              .butIs(input -> "Is not greater than 2, for input: " + input)
              .validate()
      )
          .hasMessage(format("Is not even, for input: %s", value))
          .isInstanceOf(ValidationException.class);
      assertThatThrownBy(() ->
          forInput(2)
              .mustSatisfy(isEven)
              .butIs(input -> "Is not even, for input: " + input)
              .andSatisfies(isGreaterThan2)
              .butIs(input -> "Is not greater than 2, for input: " + input)
              .validate()
      )
          .hasMessage(format("Is not greater than 2, for input: %s", 2))
          .isInstanceOf(ValidationException.class);

      assertThatThrownBy(() ->
          forInput(3)
              .mustSatisfy(isGreaterThan2)
              .butIs(input -> "Is not greater than 2, for input: " + input)
              .andSatisfies(isEven)
              .butIs(input -> "Is not even, for input: " + input)
              .validate()
      )
          .hasMessage(format("Is not even, for input: %s", 3))
          .isInstanceOf(ValidationException.class);
      assertThatThrownBy(() ->
          forInput(2)
              .mustSatisfy(isGreaterThan2)
              .butIs(input -> "Is not greater than 2, for input: " + input)
              .andSatisfies(isEven)
              .butIs(input -> "Is not even, for input: " + input)
              .validate()
      )
          .hasMessage(format("Is not greater than 2, for input: %s", 2))
          .isInstanceOf(ValidationException.class);
      assertThatThrownBy(() ->
          forInput(1)
              .mustSatisfy(isGreaterThan2)
              .butIs(input -> "Is not greater than 2, for input: " + input)
              .andSatisfies(isEven)
              .butIs(input -> "Is not even, for input: " + input)
              .validate()
      )
          .hasMessage(format("Is not greater than 2, for input: %s", 1))
          .isInstanceOf(ValidationException.class);
    }
  }
}
