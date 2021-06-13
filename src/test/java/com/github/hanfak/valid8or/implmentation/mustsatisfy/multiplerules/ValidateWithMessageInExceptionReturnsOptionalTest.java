package com.github.hanfak.valid8or.implmentation.mustsatisfy.multiplerules;

import com.github.hanfak.valid8or.implmentation.domain.ValidationException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import testinfrastructure.TestFixtures;

import static com.github.hanfak.valid8or.api.Valid8orMustSatisfyAllRules.forInput;
import static java.lang.String.format;

public class ValidateWithMessageInExceptionReturnsOptionalTest extends TestFixtures {

  // TODO separate tests with in tests
  @Nested
  class ReturnsInputAsOptionalAndNoExceptionThrownWhenInputIsValid {

    @Test
    void usingCustomExceptionWithCustomMessageUsingInput() {
      assertThat(
          forInput(4)
              .mustSatisfy(isEven).orThrow(IllegalStateException::new)
              .withMessage(input -> "Is not even, for input: " + input)
              .andSatisfies(isGreaterThan2).orThrow(IllegalArgumentException::new)
              .withMessage(input -> "Is not greater than 2, for input: " + input)
              .validateThenReturnOptional()
      ).isPresent().containsInstanceOf(Integer.class).contains(4);

      assertThat(
          forInput(4)
              .mustSatisfy(isGreaterThan2).orThrow(IllegalArgumentException::new)
              .withMessage(input -> "Is not greater than 2, for input: " + input)
              .andSatisfies(isEven).orThrow(IllegalStateException::new)
              .withMessage(input -> "Is not even, for input: " + input)
              .validateThenReturnOptional()
      ).isPresent().containsInstanceOf(Integer.class).contains(4);
    }

    @Test
    void usingCustomExceptionWithCustomMessageNotUsingInput() {
      assertThat(
          forInput(4)
              .mustSatisfy(isEven).orThrow(IllegalStateException::new)
              .withMessage(input -> "Is not even, for input: " + input )
              .andSatisfies(isGreaterThan2).orThrow(IllegalArgumentException::new)
              .withMessage(input -> "Is not greater than 2, for input: " + input)
              .validateThenReturnOptional()
      ).isPresent().containsInstanceOf(Integer.class).contains(4);

      assertThat(
          forInput(4)
              .mustSatisfy(isGreaterThan2).orThrow(IllegalArgumentException::new)
              .withMessage(input -> "Is not greater than 2, for input: " + input)
              .andSatisfies(isEven).orThrow(IllegalStateException::new)
              .withMessage(input -> "Is not even, for input: " + input)
              .validateThenReturnOptional()
      ).isPresent().containsInstanceOf(Integer.class).contains(4);
    }

    @Test
    void usingCustomMessageAndNoCustomException() {
      assertThat(
          forInput(4)
              .mustSatisfy(isEven)
              .butWas(input -> "Is not even, for input: " + input)
              .andSatisfies(isGreaterThan2)
              .butWas(input -> "Is not greater than 2, for input: " + input)
              .validateThenReturnOptional()
      ).isPresent().containsInstanceOf(Integer.class).contains(4);

      assertThat(
          forInput(4)
              .mustSatisfy(isGreaterThan2)
              .butWas(input -> "Is not greater than 2, for input: " + input)
              .andSatisfies(isEven)
              .butWas(input -> "Is not even, for input: " + input)
              .validateThenReturnOptional()
      ).isPresent().containsInstanceOf(Integer.class).contains(4);
    }

    @Test
    void usingCustomMessageNotUsingInputAndNoCustomException() {
      assertThat(
          forInput(4)
              .mustSatisfy(isEven)
              .butWas(input -> "Is not even, for input: " + input)
              .andSatisfies(isGreaterThan2)
              .butWas(input -> "Is not greater than 2, for input: " + input)
              .validateThenReturnOptional()
      ).isPresent().containsInstanceOf(Integer.class).contains(4);

      assertThat(
          forInput(4)
              .mustSatisfy(isGreaterThan2)
              .butWas(input -> "Is not greater than 2, for input: " + input)
              .andSatisfies(isEven)
              .butWas(input -> "Is not even, for input: " + input)
              .validateThenReturnOptional()
      ).isPresent().containsInstanceOf(Integer.class).contains(4);
    }

    @Test
    void usingBothCustomMessageOnlyAndCustomExceptionForDifferentRules() {
      assertThat(
          forInput(4)
              .mustSatisfy(isEven).orThrow(IllegalStateException::new)
              .withMessage(input -> "Is not even, for input: " + input)
              .andSatisfies(isGreaterThan2)
              .butWas(input -> "Is not greater than 2, for input: " + input)
              .validateThenReturnOptional()
      ).isPresent().containsInstanceOf(Integer.class).contains(4);

      assertThat(
          forInput(4)
              .mustSatisfy(isEven)
              .butWas(input -> "Is not even, for input: " + input)
              .andSatisfies(isGreaterThan2).orThrow(IllegalStateException::new)
              .withMessage(input -> "Is not greater than 2, for input: " + input)
              .validateThenReturnOptional()
      ).isPresent().containsInstanceOf(Integer.class).contains(4);
    }
  }

  @Nested
  class ThrowsAnExceptionWhenInputIsInvalid {

    @ParameterizedTest
    @ValueSource(ints = {3, 1})
    void usingCustomExceptionWithCustomMessageUsingInputThrowsCustomException(int value) {
      assertThatThrownBy(() ->
          forInput(value)
              .mustSatisfy(isEven).orThrow(IllegalStateException::new)
              .withMessage(input -> "Is not even, for input: " + input)
              .andSatisfies(isGreaterThan2).orThrow(IllegalStateException::new)
              .withMessage(input -> "Is not greater than 2, for input: " + input)
              .validateThenReturnOptional()
      )
          .hasMessage(format("Is not even, for input: %s", value))
          .isInstanceOf(IllegalStateException.class);
      assertThatThrownBy(() ->
          forInput(2)
              .mustSatisfy(isEven).orThrow(IllegalStateException::new)
              .withMessage(input -> "Is not even, for input: " + input)
              .andSatisfies(isGreaterThan2).orThrow(IllegalArgumentException::new)
              .withMessage(input -> "Is not greater than 2, for input: " + input)
              .validateThenReturnOptional()
      )
          .hasMessage(format("Is not greater than 2, for input: %s", 2))
          .isInstanceOf(IllegalArgumentException.class);

      assertThatThrownBy(() ->
          forInput(3)
              .mustSatisfy(isGreaterThan2).orThrow(IllegalArgumentException::new)
              .withMessage(input -> "Is not greater than 2, for input: " + input)
              .andSatisfies(isEven).orThrow(IllegalStateException::new)
              .withMessage(input -> "Is not even, for input: " + input)
              .validateThenReturnOptional()
      )
          .hasMessage(format("Is not even, for input: %s", 3))
          .isInstanceOf(IllegalStateException.class);
      assertThatThrownBy(() ->
          forInput(2)
              .mustSatisfy(isGreaterThan2).orThrow(IllegalArgumentException::new)
              .withMessage(input -> "Is not greater than 2, for input: " + input)
              .andSatisfies(isEven).orThrow(IllegalStateException::new)
              .withMessage(input -> "Is not even, for input: " + input)
              .validateThenReturnOptional()
      )
          .hasMessage(format("Is not greater than 2, for input: %s", 2))
          .isInstanceOf(IllegalArgumentException.class);
      assertThatThrownBy(() ->
          forInput(1)
              .mustSatisfy(isGreaterThan2).orThrow(IllegalArgumentException::new)
              .withMessage(input -> "Is not greater than 2, for input: " + input)
              .andSatisfies(isEven).orThrow(IllegalStateException::new)
              .withMessage(input -> "Is not even, for input: " + input)
              .validateThenReturnOptional()
      )
          .hasMessage(format("Is not greater than 2, for input: %s", 1))
          .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(ints = {3, 1})
    void usingCustomExceptionWithCustomMessageNotUsingInputThrowsCustomException(int value) {
      assertThatThrownBy(() ->
          forInput(value)
              .mustSatisfy(isEven).orThrow(IllegalStateException::new)
              .withMessage(input -> "Is not even, for input: " + input )
              .andSatisfies(isGreaterThan2).orThrow(IllegalArgumentException::new)
              .withMessage(input -> "Is not greater than 2, for input: " + input)
              .validateThenReturnOptional()
      )
          .hasMessage("Is not even, for input: " + value)
          .isInstanceOf(IllegalStateException.class);
      assertThatThrownBy(() ->
          forInput(2)
              .mustSatisfy(isEven).orThrow(IllegalStateException::new)
              .withMessage(input -> "Is not even, for input: " + input )
              .andSatisfies(isGreaterThan2).orThrow(IllegalArgumentException::new)
              .withMessage(input -> "Is not greater than 2, for input: " + input)
              .validateThenReturnOptional()
      )
          .hasMessage("Is not greater than 2, for input: 2")
          .isInstanceOf(IllegalArgumentException.class);

      assertThatThrownBy(() ->
          forInput(3)
              .mustSatisfy(isGreaterThan2).orThrow(IllegalArgumentException::new)
              .withMessage(input -> "Is not greater than 2, for input: " + input)
              .andSatisfies(isEven).orThrow(IllegalStateException::new)
              .withMessage(input -> "Is not even, for input: " + input)
              .validateThenReturnOptional()
      )
          .hasMessage("Is not even, for input: 3")
          .isInstanceOf(IllegalStateException.class);
      assertThatThrownBy(() ->
          forInput(2)
              .mustSatisfy(isGreaterThan2).orThrow(IllegalArgumentException::new)
              .withMessage(input -> "Is not greater than 2, for input: " + input)
              .andSatisfies(isEven).orThrow(IllegalStateException::new)
              .withMessage(input -> "Is not even, for input: " + input)
              .validateThenReturnOptional()
      )
          .hasMessage("Is not greater than 2, for input: 2")
          .isInstanceOf(IllegalArgumentException.class);
      assertThatThrownBy(() ->
          forInput(1)
              .mustSatisfy(isGreaterThan2).orThrow(IllegalArgumentException::new)
              .withMessage(input -> "Is not greater than 2, for input: " + input)
              .andSatisfies(isEven).orThrow(IllegalStateException::new)
              .withMessage(input -> "Is not even, for input: " + input)
              .validateThenReturnOptional()
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
              .butWas(input -> "Is not even, for input: " + input)
              .andSatisfies(isGreaterThan2)
              .butWas(input -> "Is not greater than 2, for input: " + input)
              .validateThenReturnOptional()
      )
          .hasMessage(format("Is not even, for input: %s", value))
          .isInstanceOf(ValidationException.class);
      assertThatThrownBy(() ->
          forInput(2)
              .mustSatisfy(isEven)
              .butWas(input -> "Is not even, for input: " + input)
              .andSatisfies(isGreaterThan2)
              .butWas(input -> "Is not greater than 2, for input: " + input)
              .validateThenReturnOptional()
      )
          .hasMessage(format("Is not greater than 2, for input: %s", 2))
          .isInstanceOf(ValidationException.class);

      assertThatThrownBy(() ->
          forInput(3)
              .mustSatisfy(isGreaterThan2)
              .butWas(input -> "Is not greater than 2, for input: " + input)
              .andSatisfies(isEven)
              .butWas(input -> "Is not even, for input: " + input)
              .validateThenReturnOptional()
      )
          .hasMessage(format("Is not even, for input: %s", 3))
          .isInstanceOf(ValidationException.class);
      assertThatThrownBy(() ->
          forInput(2)
              .mustSatisfy(isGreaterThan2)
              .butWas(input -> "Is not greater than 2, for input: " + input)
              .andSatisfies(isEven)
              .butWas(input -> "Is not even, for input: " + input)
              .validateThenReturnOptional()
      )
          .hasMessage("Is not greater than 2, for input: 2")
          .isInstanceOf(ValidationException.class);
      assertThatThrownBy(() ->
          forInput(1)
              .mustSatisfy(isGreaterThan2)
              .butWas(input -> "Is not greater than 2, for input: " + input)
              .andSatisfies(isEven)
              .butWas(input -> "Is not even, for input: " + input)
              .validateThenReturnOptional()
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
              .butWas(input -> "Is not even, for input: " + input)
              .andSatisfies(isGreaterThan2)
              .butWas(input -> "Is not greater than 2, for input: " + input)
              .validateThenReturnOptional()
      )
          .hasMessage("Is not even, for input: " + value)
          .isInstanceOf(ValidationException.class);
      assertThatThrownBy(() ->
          forInput(2)
              .mustSatisfy(isEven)
              .butWas(input -> "Is not even, for input: " + input)
              .andSatisfies(isGreaterThan2)
              .butWas(input -> "Is not greater than 2, for input: " + input)
              .validateThenReturnOptional()
      )
          .hasMessage("Is not greater than 2, for input: 2")
          .isInstanceOf(ValidationException.class);

      assertThatThrownBy(() ->
          forInput(3)
              .mustSatisfy(isGreaterThan2)
              .butWas(input -> "Is not greater than 2, for input: " + input)
              .andSatisfies(isEven)
              .butWas(input -> "Is not even, for input: " + input)
              .validateThenReturnOptional()
      )
          .hasMessage("Is not even, for input: 3")
          .isInstanceOf(ValidationException.class);
      assertThatThrownBy(() ->
          forInput(2)
              .mustSatisfy(isGreaterThan2)
              .butWas(input -> "Is not greater than 2, for input: " + input)
              .andSatisfies(isEven)
              .butWas(input -> "Is not even, for input: " + input)
              .validateThenReturnOptional()
      )
          .hasMessage("Is not greater than 2, for input: 2")
          .isInstanceOf(ValidationException.class);
      assertThatThrownBy(() ->
          forInput(1)
              .mustSatisfy(isGreaterThan2)
              .butWas(input -> "Is not greater than 2, for input: " + input)
              .andSatisfies(isEven)
              .butWas(input -> "Is not even, for input: " + input)
              .validateThenReturnOptional()
      )
          .hasMessage("Is not greater than 2, for input: 1")
          .isInstanceOf(ValidationException.class);
    }

    @ParameterizedTest
    @ValueSource(ints = {3, 1})
    void usingBothCustomMessageOnlyAndCustomExceptionForDifferentRulesThrowsException(int value) {
      assertThatThrownBy(() ->
          forInput(value)
              .mustSatisfy(isEven).orThrow(IllegalStateException::new)
              .withMessage(input -> "Is not even, for input: " + input)
              .andSatisfies(isGreaterThan2)
              .butWas(input -> "Is not greater than 2, for input: " + input)
              .validateThenReturnOptional()
      )
          .hasMessage(format("Is not even, for input: %s", value))
          .isInstanceOf(IllegalStateException.class);
      assertThatThrownBy(() ->
          forInput(2)
              .mustSatisfy(isEven).orThrow(IllegalStateException::new)
              .withMessage(input -> "Is not even, for input: " + input)
              .andSatisfies(isGreaterThan2)
              .butWas(input -> "Is not greater than 2, for input: " + input)
              .validateThenReturnOptional()
      )
          .hasMessage(format("Is not greater than 2, for input: %s", 2))
          .isInstanceOf(ValidationException.class);



      assertThatThrownBy(() ->
          forInput(3)
              .mustSatisfy(isEven)
              .butWas(input -> "Is not even, for input: " + input)
              .andSatisfies(isGreaterThan2).orThrow(IllegalArgumentException::new)
              .withMessage(input -> "Is not greater than 2, for input: " + input)
              .validateThenReturnOptional()
      )
          .hasMessage(format("Is not even, for input: %s", 3))
          .isInstanceOf(ValidationException.class);
      assertThatThrownBy(() ->
          forInput(2)
              .mustSatisfy(isEven)
              .butWas(input -> "Is not even, for input: " + input)
              .andSatisfies(isGreaterThan2).orThrow(IllegalArgumentException::new)
              .withMessage(input -> "Is not greater than 2, for input: " + input)
              .validateThenReturnOptional()
      )
          .hasMessage(format("Is not greater than 2, for input: %s", 2))
          .isInstanceOf(IllegalArgumentException.class);
      assertThatThrownBy(() ->
          forInput(1)
              .mustSatisfy(isEven)
              .butWas(input -> "Is not even, for input: " + input)
              .andSatisfies(isGreaterThan2).orThrow(IllegalArgumentException::new)
              .withMessage(input -> "Is not greater than 2, for input: " + input)
              .validateThenReturnOptional()
      )
          .hasMessage(format("Is not even, for input: %s", 1))
          .isInstanceOf(ValidationException.class);
    }
  }
}
