package com.github.hanfak.valid8or.implmentation.mustsatisfy.multiplerule;

import com.github.hanfak.valid8or.implmentation.domain.ValidationException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import testinfrastructure.TestFixtures;

import static com.github.hanfak.valid8or.api.Valid8or.forInput;

// Despite using ifNotWillThrown or ifNotWillThrowAn, with butIs will not throw the custom exception defined
// when the input is invalid. Too avoid wastage, best to use butIs without defining custom exception methods
public class ValidateUsingValidationExceptionWithMessageInExceptionTest extends TestFixtures {

  @Nested
  class ReturnsInputAndNoExceptionThrownWhenInputIsValid {

    @Test
    void usingCustomExceptionWithNoMessage() {
      assertThat(
          forInput(4)
              .mustSatisfy(isEven).ifNotWillThrowAn(IllegalStateException::new)
              .andSatisfies(isGreaterThan2).ifNotWillThrowAn(IllegalArgumentException::new)
              .orElseThrowValidationException()
      ).isEqualTo(4);

      assertThat(
          forInput(4)
              .mustSatisfy(isGreaterThan2).ifNotWillThrowAn(IllegalArgumentException::new)
              .andSatisfies(isEven).ifNotWillThrowAn(IllegalStateException::new)
              .orElseThrowValidationException()
      ).isEqualTo(4);
    }

    @Test
    void usingCustomExceptionhasMessage() {
      assertThat(
          forInput(4)
              .mustSatisfy(isEven).ifNotWillThrowAn(() -> new IllegalStateException("Some Exception"))
              .andSatisfies(isGreaterThan2).ifNotWillThrowAn(() -> new IllegalArgumentException("Some Other Exception"))
              .orElseThrowValidationException()
      ).isEqualTo(4);

      assertThat(
          forInput(4)
              .mustSatisfy(isGreaterThan2).ifNotWillThrowAn(() -> new IllegalArgumentException("Some Other Exception"))
              .andSatisfies(isEven).ifNotWillThrowAn(() -> new IllegalStateException("Some Exception"))
              .orElseThrowValidationException()
      ).isEqualTo(4);
    }

    @Test
    void usingCustomExceptionWithCustomMessageUsingInput() {
      assertThat(
          forInput(4)
              .mustSatisfy(isEven).ifNotWillThrow(IllegalStateException::new)
              .hasMessage(input -> "Is not even, for input: " + input)
              .andSatisfies(isGreaterThan2).ifNotWillThrow(IllegalArgumentException::new)
              .hasMessage(input -> "Is not greater than 2, for input: " + input)
              .orElseThrowValidationException()
      ).isEqualTo(4);

      assertThat(
          forInput(4)
              .mustSatisfy(isGreaterThan2).ifNotWillThrow(IllegalArgumentException::new)
              .hasMessage(input -> "Is not greater than 2, for input: " + input)
              .andSatisfies(isEven).ifNotWillThrow(IllegalStateException::new)
              .hasMessage(input -> "Is not even, for input: " + input)
              .orElseThrowValidationException()
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
              .orElseThrowValidationException()
      ).isEqualTo(4);

      assertThat(
          forInput(4)
              .mustSatisfy(isGreaterThan2)
              .butIs(input -> "Is not greater than 2, for input: " + input)
              .andSatisfies(isEven)
              .butIs(input -> "Is not even, for input: " + input)
              .orElseThrowValidationException()
      ).isEqualTo(4);
    }
  }

  @Nested
  class ThrowsAnExceptionWhenInputIsInvalid {

    @ParameterizedTest
    @ValueSource(ints = {3, 2, 1})
    void usingCustomExceptionWithNoMessageThrowsCustomException(int value) {
      assertThatThrownBy(() ->
          forInput(value)
              .mustSatisfy(isEven).ifNotWillThrowAn(IllegalStateException::new)
              .andSatisfies(isGreaterThan2).ifNotWillThrowAn(IllegalArgumentException::new)
              .orElseThrowValidationException()
      )
          .hasMessage(String.format("For input: '%s', the following problems occurred: 'null'", value))
          .isInstanceOf(ValidationException.class);

      assertThatThrownBy(() ->
          forInput(value)
              .mustSatisfy(isGreaterThan2).ifNotWillThrowAn(IllegalArgumentException::new)
              .andSatisfies(isEven).ifNotWillThrowAn(IllegalStateException::new)
              .orElseThrowValidationException()
      )
          .hasMessage(String.format("For input: '%s', the following problems occurred: 'null'", value))
          .isInstanceOf(ValidationException.class);
    }

    @Test
    void usingCustomExceptionWithMessageThrowsCustomException() {
      assertThatThrownBy(() ->
          forInput(3)
              .mustSatisfy(isEven).ifNotWillThrowAn(() -> new IllegalStateException("Some Exception"))
              .andSatisfies(isGreaterThan2).ifNotWillThrowAn(() -> new IllegalArgumentException("Some Other Exception"))
              .orElseThrowValidationException()
      )
          .hasMessage("For input: '3', the following problems occurred: 'Some Exception'")
          .isInstanceOf(ValidationException.class);
      assertThatThrownBy(() ->
          forInput(2)
              .mustSatisfy(isEven).ifNotWillThrowAn(() -> new IllegalStateException("Some Exception"))
              .andSatisfies(isGreaterThan2).ifNotWillThrowAn(() -> new IllegalArgumentException("Some Other Exception"))
              .orElseThrowValidationException()
      )
          .hasMessage("For input: '2', the following problems occurred: 'Some Other Exception'")
          .isInstanceOf(ValidationException.class);
      assertThatThrownBy(() ->
          forInput(1)
              .mustSatisfy(isEven).ifNotWillThrowAn(() -> new IllegalStateException("Some Exception"))
              .andSatisfies(isGreaterThan2).ifNotWillThrowAn(() -> new IllegalArgumentException("Some Other Exception"))
              .orElseThrowValidationException()
      )
          .hasMessage("For input: '1', the following problems occurred: 'Some Exception, Some Other Exception'")
          .isInstanceOf(ValidationException.class);

      assertThatThrownBy(() ->
          forInput(3)
              .mustSatisfy(isGreaterThan2).ifNotWillThrowAn(() -> new IllegalArgumentException("Some Other Exception"))
              .andSatisfies(isEven).ifNotWillThrowAn(() -> new IllegalStateException("Some Exception"))
              .orElseThrowValidationException()
      )
          .hasMessage("For input: '3', the following problems occurred: 'Some Exception'")
          .isInstanceOf(ValidationException.class);
      assertThatThrownBy(() ->
          forInput(2)
              .mustSatisfy(isGreaterThan2).ifNotWillThrowAn(() -> new IllegalArgumentException("Some Other Exception"))
              .andSatisfies(isEven).ifNotWillThrowAn(() -> new IllegalStateException("Some Exception"))
              .orElseThrowValidationException()
      )
          .hasMessage("For input: '2', the following problems occurred: 'Some Other Exception'")
          .isInstanceOf(ValidationException.class);
      assertThatThrownBy(() ->
          forInput(1)
              .mustSatisfy(isGreaterThan2).ifNotWillThrowAn(() -> new IllegalArgumentException("Some Other Exception"))
              .andSatisfies(isEven).ifNotWillThrowAn(() -> new IllegalStateException("Some Exception"))
              .orElseThrowValidationException()
      )
          .hasMessage("For input: '1', the following problems occurred: 'Some Other Exception, Some Exception'")
          .isInstanceOf(ValidationException.class);
    }

    @Test
    void usingCustomExceptionWithCustomMessageUsingInputThrowsCustomException() {
      assertThatThrownBy(() ->
          forInput(3)
              .mustSatisfy(isEven).ifNotWillThrow(IllegalStateException::new)
              .hasMessage(input -> "Is not even, for input: " + input)
              .andSatisfies(isGreaterThan2).ifNotWillThrow(IllegalArgumentException::new)
              .hasMessage(input -> "Is not greater than 2, for input: " + input)
              .orElseThrowValidationException()
      )
          .hasMessage("For input: '3', the following problems occurred: 'Is not even, for input: 3'")
          .isInstanceOf(ValidationException.class);
      assertThatThrownBy(() ->
          forInput(2)
              .mustSatisfy(isEven).ifNotWillThrow(IllegalStateException::new)
              .hasMessage(input -> "Is not even, for input: " + input)
              .andSatisfies(isGreaterThan2).ifNotWillThrow(IllegalArgumentException::new)
              .hasMessage(input -> "Is not greater than 2, for input: " + input)
              .orElseThrowValidationException()
      )
          .hasMessage("For input: '2', the following problems occurred: 'Is not greater than 2, for input: 2'")
          .isInstanceOf(ValidationException.class);
      assertThatThrownBy(() ->
          forInput(1)
              .mustSatisfy(isEven).ifNotWillThrow(IllegalStateException::new)
              .hasMessage(input -> "Is not even, for input: " + input)
              .andSatisfies(isGreaterThan2).ifNotWillThrow(IllegalArgumentException::new)
              .hasMessage(input -> "Is not greater than 2, for input: " + input)
              .orElseThrowValidationException()
      )
          .hasMessage("For input: '1', the following problems occurred: 'Is not even, for input: 1, Is not greater than 2, for input: 1'")
          .isInstanceOf(ValidationException.class);

      assertThatThrownBy(() ->
          forInput(3)
              .mustSatisfy(isGreaterThan2).ifNotWillThrow(IllegalArgumentException::new)
              .hasMessage(input -> "Is not greater than 2, for input: " + input)
              .andSatisfies(isEven).ifNotWillThrow(IllegalStateException::new)
              .hasMessage(input -> "Is not even, for input: " + input)
              .orElseThrowValidationException()
      )
          .hasMessage("For input: '3', the following problems occurred: 'Is not even, for input: 3'")
          .isInstanceOf(ValidationException.class);
      assertThatThrownBy(() ->
          forInput(2)
              .mustSatisfy(isGreaterThan2).ifNotWillThrow(IllegalArgumentException::new)
              .hasMessage(input -> "Is not greater than 2, for input: " + input)
              .andSatisfies(isEven).ifNotWillThrow(IllegalStateException::new)
              .hasMessage(input -> "Is not even, for input: " + input)
              .orElseThrowValidationException()
      )
          .hasMessage("For input: '2', the following problems occurred: 'Is not greater than 2, for input: 2'")
          .isInstanceOf(ValidationException.class);
      assertThatThrownBy(() ->
          forInput(1)
              .mustSatisfy(isGreaterThan2).ifNotWillThrow(IllegalArgumentException::new)
              .hasMessage(input -> "Is not greater than 2, for input: " + input)
              .andSatisfies(isEven).ifNotWillThrow(IllegalStateException::new)
              .hasMessage(input -> "Is not even, for input: " + input)
              .orElseThrowValidationException()
      )
          .hasMessage("For input: '1', the following problems occurred: 'Is not greater than 2, for input: 1, Is not even, for input: 1'")
          .isInstanceOf(ValidationException.class);
    }

    @Test
    void usingCustomMessageAndNoCustomExceptionWillThrowValidationException() {
      assertThatThrownBy(() ->
          forInput(3)
              .mustSatisfy(isEven)
              .butIs(input -> "Is not even, for input: " + input)
              .andSatisfies(isGreaterThan2)
              .butIs(input -> "Is not greater than 2, for input: " + input)
              .orElseThrowValidationException()
      )
          .hasMessage("For input: '3', the following problems occurred: 'Is not even, for input: 3'")
          .isInstanceOf(ValidationException.class);
      assertThatThrownBy(() ->
          forInput(2)
              .mustSatisfy(isEven)
              .butIs(input -> "Is not even, for input: " + input)
              .andSatisfies(isGreaterThan2)
              .butIs(input -> "Is not greater than 2, for input: " + input)
              .orElseThrowValidationException()
      )
          .hasMessage("For input: '2', the following problems occurred: 'Is not greater than 2, for input: 2'")
          .isInstanceOf(ValidationException.class);
      assertThatThrownBy(() ->
          forInput(1)
              .mustSatisfy(isEven)
              .butIs(input -> "Is not even, for input: " + input)
              .andSatisfies(isGreaterThan2)
              .butIs(input -> "Is not greater than 2, for input: " + input)
              .orElseThrowValidationException()
      )
          .hasMessage("For input: '1', the following problems occurred: 'Is not even, for input: 1, Is not greater than 2, for input: 1'")
          .isInstanceOf(ValidationException.class);

      assertThatThrownBy(() ->
          forInput(3)
              .mustSatisfy(isGreaterThan2)
              .butIs(input -> "Is not greater than 2, for input: " + input)
              .andSatisfies(isEven)
              .butIs(input -> "Is not even, for input: " + input)
              .orElseThrowValidationException()
      )
          .hasMessage("For input: '3', the following problems occurred: 'Is not even, for input: 3'")
          .isInstanceOf(ValidationException.class);
      assertThatThrownBy(() ->
          forInput(2)
              .mustSatisfy(isGreaterThan2)
              .butIs(input -> "Is not greater than 2, for input: " + input)
              .andSatisfies(isEven)
              .butIs(input -> "Is not even, for input: " + input)
              .orElseThrowValidationException()
      )
          .hasMessage("For input: '2', the following problems occurred: 'Is not greater than 2, for input: 2'")
          .isInstanceOf(ValidationException.class);
      assertThatThrownBy(() ->
          forInput(1)
              .mustSatisfy(isGreaterThan2)
              .butIs(input -> "Is not greater than 2, for input: " + input)
              .andSatisfies(isEven)
              .butIs(input -> "Is not even, for input: " + input)
              .orElseThrowValidationException()
      )
          .hasMessage("For input: '1', the following problems occurred: 'Is not greater than 2, for input: 1, Is not even, for input: 1'")
          .isInstanceOf(ValidationException.class);
    }
  }
}
