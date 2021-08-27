package com.github.hanfak.valid8or.implmentation.mustsatisfy.multiplerules;

import com.github.hanfak.valid8or.implmentation.ValidationException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import testinfrastructure.TestFixtures;

import static com.github.hanfak.valid8or.implmentation.Valid8orMustSatisfyAllRules.forInput;

class ValidateUsingValidationExceptionWithMessageInExceptionTest extends TestFixtures {

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
              .throwNotificationIfNotValid()
      ).isEqualTo(4);

      assertThat(
          forInput(4)
              .mustSatisfy(isGreaterThan2).orElseThrow(IllegalArgumentException::new)
              .withExceptionMessage(input -> "Is not greater than 2, for input: " + input)
              .and(isEven).orElseThrow(IllegalStateException::new)
              .withExceptionMessage(input -> "Is not even, for input: " + input)
              .throwNotificationIfNotValid()
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
              .throwNotificationIfNotValid()
      ).isEqualTo(4);

      assertThat(
          forInput(4)
              .mustSatisfy(isGreaterThan2)
              .orThrowExceptionWith(input -> "Is not greater than 2, for input: " + input)
              .and(isEven)
              .orThrowExceptionWith(input -> "Is not even, for input: " + input)
              .throwNotificationIfNotValid()
      ).isEqualTo(4);
    }
  }

  @Nested
  class ThrowsAnExceptionWhenInputIsInvalid {

    @Test
    void usingCustomExceptionWithCustomMessageUsingInputThrowsCustomException() {
      assertThatThrownBy(() ->
          forInput(3)
              .mustSatisfy(isEven).orElseThrow(IllegalStateException::new)
              .withExceptionMessage(input -> "Is not even, for input: " + input)
              .and(isGreaterThan2).orElseThrow(IllegalArgumentException::new)
              .withExceptionMessage(input -> "Is not greater than 2, for input: " + input)
              .throwNotificationIfNotValid()
      )
          .hasMessage("For input: '3', the following problems occurred: 'Is not even, for input: 3'")
          .isInstanceOf(ValidationException.class);
      assertThatThrownBy(() ->
          forInput(2)
              .mustSatisfy(isEven).orElseThrow(IllegalStateException::new)
              .withExceptionMessage(input -> "Is not even, for input: " + input)
              .and(isGreaterThan2).orElseThrow(IllegalArgumentException::new)
              .withExceptionMessage(input -> "Is not greater than 2, for input: " + input)
              .throwNotificationIfNotValid()
      )
          .hasMessage("For input: '2', the following problems occurred: 'Is not greater than 2, for input: 2'")
          .isInstanceOf(ValidationException.class);
      assertThatThrownBy(() ->
          forInput(1)
              .mustSatisfy(isEven).orElseThrow(IllegalStateException::new)
              .withExceptionMessage(input -> "Is not even, for input: " + input)
              .and(isGreaterThan2).orElseThrow(IllegalArgumentException::new)
              .withExceptionMessage(input -> "Is not greater than 2, for input: " + input)
              .throwNotificationIfNotValid()
      )
          .hasMessage("For input: '1', the following problems occurred: 'Is not even, for input: 1; Is not greater than 2, for input: 1'")
          .isInstanceOf(ValidationException.class);

      assertThatThrownBy(() ->
          forInput(3)
              .mustSatisfy(isGreaterThan2).orElseThrow(IllegalArgumentException::new)
              .withExceptionMessage(input -> "Is not greater than 2, for input: " + input)
              .and(isEven).orElseThrow(IllegalStateException::new)
              .withExceptionMessage(input -> "Is not even, for input: " + input)
              .throwNotificationIfNotValid()
      )
          .hasMessage("For input: '3', the following problems occurred: 'Is not even, for input: 3'")
          .isInstanceOf(ValidationException.class);
      assertThatThrownBy(() ->
          forInput(2)
              .mustSatisfy(isGreaterThan2).orElseThrow(IllegalArgumentException::new)
              .withExceptionMessage(input -> "Is not greater than 2, for input: " + input)
              .and(isEven).orElseThrow(IllegalStateException::new)
              .withExceptionMessage(input -> "Is not even, for input: " + input)
              .throwNotificationIfNotValid()
      )
          .hasMessage("For input: '2', the following problems occurred: 'Is not greater than 2, for input: 2'")
          .isInstanceOf(ValidationException.class);
      assertThatThrownBy(() ->
          forInput(1)
              .mustSatisfy(isGreaterThan2).orElseThrow(IllegalArgumentException::new)
              .withExceptionMessage(input -> "Is not greater than 2, for input: " + input)
              .and(isEven).orElseThrow(IllegalStateException::new)
              .withExceptionMessage(input -> "Is not even, for input: " + input)
              .throwNotificationIfNotValid()
      )
          .hasMessage("For input: '1', the following problems occurred: 'Is not greater than 2, for input: 1; Is not even, for input: 1'")
          .isInstanceOf(ValidationException.class);
    }

    @Test
    void usingCustomMessageAndNoCustomExceptionWillThrowValidationException() {
      assertThatThrownBy(() ->
          forInput(3)
              .mustSatisfy(isEven)
              .orThrowExceptionWith(input -> "Is not even, for input: " + input)
              .and(isGreaterThan2)
              .orThrowExceptionWith(input -> "Is not greater than 2, for input: " + input)
              .throwNotificationIfNotValid()
      )
          .hasMessage("For input: '3', the following problems occurred: 'Is not even, for input: 3'")
          .isInstanceOf(ValidationException.class);
      assertThatThrownBy(() ->
          forInput(2)
              .mustSatisfy(isEven)
              .orThrowExceptionWith(input -> "Is not even, for input: " + input)
              .and(isGreaterThan2)
              .orThrowExceptionWith(input -> "Is not greater than 2, for input: " + input)
              .throwNotificationIfNotValid()
      )
          .hasMessage("For input: '2', the following problems occurred: 'Is not greater than 2, for input: 2'")
          .isInstanceOf(ValidationException.class);
      assertThatThrownBy(() ->
          forInput(1)
              .mustSatisfy(isEven)
              .orThrowExceptionWith(input -> "Is not even, for input: " + input)
              .and(isGreaterThan2)
              .orThrowExceptionWith(input -> "Is not greater than 2, for input: " + input)
              .throwNotificationIfNotValid()
      )
          .hasMessage("For input: '1', the following problems occurred: 'Is not even, for input: 1; Is not greater than 2, for input: 1'")
          .isInstanceOf(ValidationException.class);

      assertThatThrownBy(() ->
          forInput(3)
              .mustSatisfy(isGreaterThan2)
              .orThrowExceptionWith(input -> "Is not greater than 2, for input: " + input)
              .and(isEven)
              .orThrowExceptionWith(input -> "Is not even, for input: " + input)
              .throwNotificationIfNotValid()
      )
          .hasMessage("For input: '3', the following problems occurred: 'Is not even, for input: 3'")
          .isInstanceOf(ValidationException.class);
      assertThatThrownBy(() ->
          forInput(2)
              .mustSatisfy(isGreaterThan2)
              .orThrowExceptionWith(input -> "Is not greater than 2, for input: " + input)
              .and(isEven)
              .orThrowExceptionWith(input -> "Is not even, for input: " + input)
              .throwNotificationIfNotValid()
      )
          .hasMessage("For input: '2', the following problems occurred: 'Is not greater than 2, for input: 2'")
          .isInstanceOf(ValidationException.class);
      assertThatThrownBy(() ->
          forInput(1)
              .mustSatisfy(isGreaterThan2)
              .orThrowExceptionWith(input -> "Is not greater than 2, for input: " + input)
              .and(isEven)
              .orThrowExceptionWith(input -> "Is not even, for input: " + input)
              .throwNotificationIfNotValid()
      )
          .hasMessage("For input: '1', the following problems occurred: 'Is not greater than 2, for input: 1; Is not even, for input: 1'")
          .isInstanceOf(ValidationException.class);
    }
  }
}
