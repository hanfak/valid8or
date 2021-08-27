package com.github.hanfak.valid8or.implmentation.mustsatisfy.multiplerules;

import com.github.hanfak.valid8or.implmentation.ValidationException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import testinfrastructure.TestFixtures;

import static com.github.hanfak.valid8or.implmentation.Valid8orMustSatisfyAllRules.forInput;
import static java.lang.String.format;

class ValidateWithMustCouldMessageInExceptionTest extends TestFixtures {

  @Nested
  class ReturnsInputAndNoExceptionThrownWhenInputIsValid {

    @Test
    void usingCustomExceptionWithCustomMessageUsingInput() {
      assertThat(
          forInput(4)
              .mustSatisfy(isEven).orElseThrow(IllegalStateException::new)
              .withExceptionMessage(input -> "Is not even, for input: " + input)
              .and(isGreaterThan2).orElseThrow(IllegalArgumentException::new)
              .withExceptionMessage(input -> "Is not greater than 2, for input: " + input)
              .throwIfNotValid()
      ).isEqualTo(4);


      assertThat(
          forInput(4)
              .mustSatisfy(isGreaterThan2).orElseThrow(IllegalArgumentException::new)
              .withExceptionMessage(input -> "Is not greater than 2, for input: " + input)
              .and(isEven).orElseThrow(IllegalStateException::new)
              .withExceptionMessage(input -> "Is not even, for input: " + input)
              .throwIfNotValid()
      ).isEqualTo(4);
    }

    @Test
    void usingCustomExceptionWithCustomMessageNotUsingInput() {
      assertThat(
          forInput(4)
              .mustSatisfy(isEven).orElseThrow(IllegalStateException::new)
              .withExceptionMessage(input -> "Is not even, for input: " + input )
              .and(isGreaterThan2).orElseThrow(IllegalArgumentException::new)
              .withExceptionMessage(input -> "Is not greater than 2, for input: " + input)
              .throwIfNotValid()
      ).isEqualTo(4);

      assertThat(
          forInput(4)
              .mustSatisfy(isGreaterThan2).orElseThrow(IllegalArgumentException::new)
              .withExceptionMessage(input -> "Is not greater than 2, for input: " + input)
              .and(isEven).orElseThrow(IllegalStateException::new)
              .withExceptionMessage(input -> "Is not even, for input: " + input)
              .throwIfNotValid()
      ).isEqualTo(4);
    }

    @Test
    void usingCustomMessageAndNoCustomException() {
      assertThat(
          forInput(4)
              .mustSatisfy(isEven)
              .orThrowExceptionWith(input -> "Is not even, for input: " + input)
              .and(isGreaterThan2)
              .orThrowExceptionWith(input -> "Is not greater than 2, for input: " + input)
              .throwIfNotValid()
      ).isEqualTo(4);

      assertThat(
          forInput(4)
              .mustSatisfy(isGreaterThan2)
              .orThrowExceptionWith(input -> "Is not greater than 2, for input: " + input)
              .and(isEven)
              .orThrowExceptionWith(input -> "Is not even, for input: " + input)
              .throwIfNotValid()
      ).isEqualTo(4);
    }

    @Test
    void usingCustomMessageNotUsingInputAndNoCustomException() {
      assertThat(
          forInput(4)
              .mustSatisfy(isEven)
              .orThrowExceptionWith(input -> "Is not even, for input: " + input)
              .and(isGreaterThan2)
              .orThrowExceptionWith(input -> "Is not greater than 2, for input: " + input)
              .throwIfNotValid()
      ).isEqualTo(4);

      assertThat(
          forInput(4)
              .mustSatisfy(isGreaterThan2)
              .orThrowExceptionWith(input -> "Is not greater than 2, for input: " + input)
              .and(isEven)
              .orThrowExceptionWith(input -> "Is not even, for input: " + input)
              .throwIfNotValid()
      ).isEqualTo(4);
    }

    @Test
    void usingBothCustomMessageOnlyAndCustomExceptionForDifferentRules() {
      assertThat(
          forInput(4)
              .mustSatisfy(isEven).orElseThrow(IllegalStateException::new)
              .withExceptionMessage(input -> "Is not even, for input: " + input)
              .and(isGreaterThan2)
              .orThrowExceptionWith(input -> "Is not greater than 2, for input: " + input)
              .throwIfNotValid()
      ).isEqualTo(4);

      assertThat(
          forInput(4)
              .mustSatisfy(isEven)
              .orThrowExceptionWith(input -> "Is not even, for input: " + input)
              .and(isGreaterThan2).orElseThrow(IllegalArgumentException::new)
              .withExceptionMessage(input -> "Is not greater than 2, for input: " + input)
              .throwIfNotValid()
      ).isEqualTo(4);
    }
  }

  @Nested
  class ThrowsAnExceptionWhenInputIsInvalid {

    @ParameterizedTest
    @ValueSource(ints = {3, 1})
    void usingCustomExceptionWithCustomMessageUsingInputThrowsCustomException(int value) {
      assertThatThrownBy(() ->
          forInput(value)
              .mustSatisfy(isEven).orElseThrow(IllegalStateException::new)
              .withExceptionMessage(input -> "Is not even, for input: " + input)
              .and(isGreaterThan2).orElseThrow(IllegalArgumentException::new)
              .withExceptionMessage(input -> "Is not greater than 2, for input: " + input)
              .throwIfNotValid()
      )
          .hasMessage(format("Is not even, for input: %s", value))
          .isInstanceOf(IllegalStateException.class);
      assertThatThrownBy(() ->
          forInput(2)
              .mustSatisfy(isEven).orElseThrow(IllegalStateException::new)
              .withExceptionMessage(input -> "Is not even, for input: " + input)
              .and(isGreaterThan2).orElseThrow(IllegalArgumentException::new)
              .withExceptionMessage(input -> "Is not greater than 2, for input: " + input)
              .throwIfNotValid()
      )
          .hasMessage(format("Is not greater than 2, for input: %s", 2))
          .isInstanceOf(IllegalArgumentException.class);

      assertThatThrownBy(() ->
          forInput(3)
              .mustSatisfy(isGreaterThan2).orElseThrow(IllegalArgumentException::new)
              .withExceptionMessage(input -> "Is not greater than 2, for input: " + input)
              .and(isEven).orElseThrow(IllegalStateException::new)
              .withExceptionMessage(input -> "Is not even, for input: " + input)
              .throwIfNotValid()
      )
          .hasMessage(format("Is not even, for input: %s", 3))
          .isInstanceOf(IllegalStateException.class);
      assertThatThrownBy(() ->
          forInput(2)
              .mustSatisfy(isGreaterThan2).orElseThrow(IllegalArgumentException::new)
              .withExceptionMessage(input -> "Is not greater than 2, for input: " + input)
              .and(isEven).orElseThrow(IllegalStateException::new)
              .withExceptionMessage(input -> "Is not even, for input: " + input)
              .throwIfNotValid()
      )
          .hasMessage(format("Is not greater than 2, for input: %s", 2))
          .isInstanceOf(IllegalArgumentException.class);
      assertThatThrownBy(() ->
          forInput(1)
              .mustSatisfy(isGreaterThan2).orElseThrow(IllegalArgumentException::new)
              .withExceptionMessage(input -> "Is not greater than 2, for input: " + input)
              .and(isEven).orElseThrow(IllegalStateException::new)
              .withExceptionMessage(input -> "Is not even, for input: " + input)
              .throwIfNotValid()
      )
          .hasMessage(format("Is not greater than 2, for input: %s", 1))
          .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(ints = {3, 1})
    void usingCustomExceptionWithCustomMessageNotUsingInputThrowsCustomException(int value) {
      assertThatThrownBy(() ->
          forInput(value)
              .mustSatisfy(isEven).orElseThrow(IllegalStateException::new)
              .withExceptionMessage(input -> "Is not even, for input: " + input)
              .and(isGreaterThan2).orElseThrow(IllegalArgumentException::new)
              .withExceptionMessage(input -> "Is not greater than 2, for input: " + input)
              .throwIfNotValid()
      )
          .hasMessage("Is not even, for input: " + value)
          .isInstanceOf(IllegalStateException.class);
      assertThatThrownBy(() ->
          forInput(2)
              .mustSatisfy(isEven).orElseThrow(IllegalStateException::new)
              .withExceptionMessage(input -> "Is not even, for input: " + input)
              .and(isGreaterThan2).orElseThrow(IllegalArgumentException::new)
              .withExceptionMessage(input -> "Is not greater than 2, for input: " + input)
              .throwIfNotValid()
      )
          .hasMessage("Is not greater than 2, for input: 2")
          .isInstanceOf(IllegalArgumentException.class);

      assertThatThrownBy(() ->
          forInput(3)
              .mustSatisfy(isGreaterThan2).orElseThrow(IllegalArgumentException::new)
              .withExceptionMessage(input -> "Is not greater than 2, for input: " + input)
              .and(isEven).orElseThrow(IllegalStateException::new)
              .withExceptionMessage(input -> "Is not even, for input: " + input)
              .throwIfNotValid()
      )
          .hasMessage("Is not even, for input: 3")
          .isInstanceOf(IllegalStateException.class);
      assertThatThrownBy(() ->
          forInput(2)
              .mustSatisfy(isGreaterThan2).orElseThrow(IllegalArgumentException::new)
              .withExceptionMessage(input -> "Is not greater than 2, for input: " + input)
              .and(isEven).orElseThrow(IllegalStateException::new)
              .withExceptionMessage(input -> "Is not even, for input: " + input)
              .throwIfNotValid()
      )
          .hasMessage("Is not greater than 2, for input: 2")
          .isInstanceOf(IllegalArgumentException.class);
      assertThatThrownBy(() ->
          forInput(1)
              .mustSatisfy(isGreaterThan2).orElseThrow(IllegalArgumentException::new)
              .withExceptionMessage(input -> "Is not greater than 2, for input: " + input)
              .and(isEven).orElseThrow(IllegalStateException::new)
              .withExceptionMessage(input -> "Is not even, for input: " + input)
              .throwIfNotValid()
      )
          .hasMessage("Is not greater than 2, for input: 1")
          .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(ints = {3, 1})
    void usingCustomMessageAndNoCustomExceptionWillThrowValidationException(int value) {
      assertThatThrownBy(() ->
          forInput(value)
              .mustSatisfy(isEven)
              .orThrowExceptionWith(input -> "Is not even, for input: " + input)
              .and(isGreaterThan2)
              .orThrowExceptionWith(input -> "Is not greater than 2, for input: " + input)
              .throwIfNotValid()
      )
          .hasMessage(format("Is not even, for input: %s", value))
          .isInstanceOf(ValidationException.class);
      assertThatThrownBy(() ->
          forInput(2)
              .mustSatisfy(isEven)
              .orThrowExceptionWith(input -> "Is not even, for input: " + input)
              .and(isGreaterThan2)
              .orThrowExceptionWith(input -> "Is not greater than 2, for input: " + input)
              .throwIfNotValid()
      )
          .hasMessage(format("Is not greater than 2, for input: %s", 2))
          .isInstanceOf(ValidationException.class);

      assertThatThrownBy(() ->
          forInput(3)
              .mustSatisfy(isGreaterThan2)
              .orThrowExceptionWith(input -> "Is not greater than 2, for input: " + input)
              .and(isEven)
              .orThrowExceptionWith(input -> "Is not even, for input: " + input)
              .throwIfNotValid()
      )
          .hasMessage(format("Is not even, for input: %s", 3))
          .isInstanceOf(ValidationException.class);
      assertThatThrownBy(() ->
          forInput(2)
              .mustSatisfy(isGreaterThan2)
              .orThrowExceptionWith(input -> "Is not greater than 2, for input: " + input)
              .and(isEven)
              .orThrowExceptionWith(input -> "Is not even, for input: " + input)
              .throwIfNotValid()
      )
          .hasMessage(format("Is not greater than 2, for input: %s", 2))
          .isInstanceOf(ValidationException.class);
      assertThatThrownBy(() ->
          forInput(1)
              .mustSatisfy(isGreaterThan2)
              .orThrowExceptionWith(input -> "Is not greater than 2, for input: " + input)
              .and(isEven)
              .orThrowExceptionWith(input -> "Is not even, for input: " + input)
              .throwIfNotValid()
      )
          .hasMessage(format("Is not greater than 2, for input: %s", 1))
          .isInstanceOf(ValidationException.class);
    }

    @ParameterizedTest
    @ValueSource(ints = {3, 1})
    void usingCustomMessageNotUsingInputAndNoCustomExceptionWillThrowValidationException(int value) {
      assertThatThrownBy(() ->
          forInput(value)
              .mustSatisfy(isEven)
              .orThrowExceptionWith(input -> "Is not even, for input: " + input)
              .and(isGreaterThan2)
              .orThrowExceptionWith(input -> "Is not greater than 2, for input: " + input)
              .throwIfNotValid()
      )
          .hasMessage("Is not even, for input: " + value)
          .isInstanceOf(ValidationException.class);
      assertThatThrownBy(() ->
          forInput(2)
              .mustSatisfy(isEven)
              .orThrowExceptionWith(input -> "Is not even, for input: " + input)
              .and(isGreaterThan2)
              .orThrowExceptionWith(input -> "Is not greater than 2, for input: " + input)
              .throwIfNotValid()
      )
          .hasMessage("Is not greater than 2, for input: 2")
          .isInstanceOf(ValidationException.class);

      assertThatThrownBy(() ->
          forInput(3)
              .mustSatisfy(isGreaterThan2)
              .orThrowExceptionWith(input -> "Is not greater than 2, for input: " + input)
              .and(isEven)
              .orThrowExceptionWith(input -> "Is not even, for input: " + input)
              .throwIfNotValid()
      )
          .hasMessage("Is not even, for input: 3")
          .isInstanceOf(ValidationException.class);
      assertThatThrownBy(() ->
          forInput(2)
              .mustSatisfy(isGreaterThan2)
              .orThrowExceptionWith(input -> "Is not greater than 2, for input: " + input)
              .and(isEven)
              .orThrowExceptionWith(input -> "Is not even, for input: " + input)
              .throwIfNotValid()
      )
          .hasMessage("Is not greater than 2, for input: 2")
          .isInstanceOf(ValidationException.class);
      assertThatThrownBy(() ->
          forInput(1)
              .mustSatisfy(isGreaterThan2)
              .orThrowExceptionWith(input -> "Is not greater than 2, for input: " + input)
              .and(isEven)
              .orThrowExceptionWith(input -> "Is not even, for input: " + input)
              .throwIfNotValid()
      )
          .hasMessage("Is not greater than 2, for input: 1")
          .isInstanceOf(ValidationException.class);
    }

    @ParameterizedTest
    @ValueSource(ints = {3, 1})
    void usingBothCustomMessageOnlyAndCustomExceptionForDifferentRulesThrowsException(int value) {
      assertThatThrownBy(() ->
          forInput(value)
              .mustSatisfy(isEven).orElseThrow(IllegalStateException::new)
              .withExceptionMessage(input -> "Is not even, for input: " + input)
              .and(isGreaterThan2)
              .orThrowExceptionWith(input -> "Is not greater than 2, for input: " + input)
              .throwIfNotValid()
      )
          .hasMessage(format("Is not even, for input: %s", value))
          .isInstanceOf(IllegalStateException.class);
      assertThatThrownBy(() ->
          forInput(2)
              .mustSatisfy(isEven).orElseThrow(IllegalStateException::new)
              .withExceptionMessage(input -> "Is not even, for input: " + input)
              .and(isGreaterThan2)
              .orThrowExceptionWith(input -> "Is not greater than 2, for input: " + input)
              .throwIfNotValid()
      )
          .hasMessage(format("Is not greater than 2, for input: %s", 2))
          .isInstanceOf(ValidationException.class);



      assertThatThrownBy(() ->
          forInput(3)
              .mustSatisfy(isEven)
              .orThrowExceptionWith(input -> "Is not even, for input: " + input)
              .and(isGreaterThan2).orElseThrow(IllegalArgumentException::new)
              .withExceptionMessage(input -> "Is not greater than 2, for input: " + input)
              .throwIfNotValid()
      )
          .hasMessage(format("Is not even, for input: %s", 3))
          .isInstanceOf(ValidationException.class);
      assertThatThrownBy(() ->
          forInput(2)
              .mustSatisfy(isEven)
              .orThrowExceptionWith(input -> "Is not even, for input: " + input)
              .and(isGreaterThan2).orElseThrow(IllegalArgumentException::new)
              .withExceptionMessage(input -> "Is not greater than 2, for input: " + input)
              .throwIfNotValid()
      )
          .hasMessage(format("Is not greater than 2, for input: %s", 2))
          .isInstanceOf(IllegalArgumentException.class);
      assertThatThrownBy(() ->
          forInput(1)
              .mustSatisfy(isEven)
              .orThrowExceptionWith(input -> "Is not even, for input: " + input)
              .and(isGreaterThan2).orElseThrow(IllegalArgumentException::new)
              .withExceptionMessage(input -> "Is not greater than 2, for input: " + input)
              .throwIfNotValid()
      )
          .hasMessage(format("Is not even, for input: %s", 1))
          .isInstanceOf(ValidationException.class);
    }
  }
}
