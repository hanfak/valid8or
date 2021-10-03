package com.github.hanfak.valid8or.implmentation.couldsatisfy.multiplerules;

import com.github.hanfak.valid8or.implmentation.ValidationException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import testinfrastructure.TestFixtures;

import static com.github.hanfak.valid8or.implmentation.Valid8orCouldSatisfyAllRules.forInput;

class ValidateUsingValidationExceptionWithMessageInExceptionUsingConsumerTest extends TestFixtures {

  @Nested
  class ReturnsInputAndNoExceptionThrownOrConsumerUsedWhenInputIsValid {

    @ParameterizedTest
    @ValueSource(ints = {4, 3, 2})
    void usingCustomExceptionWithCustomMessageUsingInputAndConsumer(int value) {
      assertThat(
          forInput(value)
              .couldSatisfy(isEven).orElseThrow(IllegalStateException::new)
              .withMessage(input -> "Is not even, for input: " + input)
              .or(isGreaterThan2).orElseThrow(IllegalArgumentException::new)
              .withMessage(input -> "Is not greater than 2, for input: " + input)
              .useConsumer(stubLogger::log)
              .isValidOrThrowCombined()
      ).isEqualTo(value);

      assertThat(
          forInput(value)
              .couldSatisfy(isGreaterThan2).orElseThrow(IllegalArgumentException::new)
              .withMessage(input -> "Is not greater than 2, for input: " + input)
              .or(isEven).orElseThrow(IllegalStateException::new)
              .withMessage(input -> "Is not even, for input: " + input)
              .useConsumer(stubLogger::log)
              .isValidOrThrowCombined()
      ).isEqualTo(value);
    }

    @ParameterizedTest
    @ValueSource(ints = {4, 3, 2})
    void usingCustomMessageAndNoCustomExceptionAndConsumer(int value) {
      assertThat(
          forInput(value)
              .couldSatisfy(isEven)
              .orThrowExceptionWith(input -> "Is not even, for input: " + input)
              .or(isGreaterThan2)
              .orThrowExceptionWith(input -> "Is not greater than 2, for input: " + input)
              .useConsumer(stubLogger::log)
              .isValidOrThrowCombined()
      ).isEqualTo(value);

      assertThat(
          forInput(value)
              .couldSatisfy(isGreaterThan2)
              .orThrowExceptionWith(input -> "Is not greater than 2, for input: " + input)
              .or(isEven)
              .orThrowExceptionWith(input -> "Is not even, for input: " + input)
              .isValidOrThrowCombined()
      ).isEqualTo(value);
    }
  }

  @Nested
  class ThrowsAnExceptionAndUsesConsumerWhenInputIsInvalid {

    @Test
    void usingCustomExceptionWithCustomMessageUsingInputAndConsumerThrowsCustomException() {
      assertThatThrownBy(() ->
          forInput(1)
              .couldSatisfy(isEven).orElseThrow(IllegalStateException::new)
              .withMessage(input -> "Is not even, for input: " + input)
              .or(isGreaterThan2).orElseThrow(IllegalArgumentException::new)
              .withMessage(input -> "Is not greater than 2, for input: " + input)
              .useConsumer(stubLogger::log)
              .isValidOrThrowCombined()
      )
          .hasMessage("For input: '1', the following problems occurred: 'Is not even, for input: 1; Is not greater than 2, for input: 1'")
          .isInstanceOf(ValidationException.class);
      assertThat(stubLogger.lastLogEventException())
          .isInstanceOf(ValidationException.class)
          .hasMessage("For input: '1', the following problems occurred: 'Is not even, for input: 1; Is not greater than 2, for input: 1'");
      assertThat(stubLogger.lastLogEventMessage())
          .isEqualTo("For input '1', was not valid because: 'For input: '1', the following problems occurred: 'Is not even, for input: 1; Is not greater than 2, for input: 1''");

      assertThatThrownBy(() ->
          forInput(1)
              .couldSatisfy(isGreaterThan2).orElseThrow(IllegalArgumentException::new)
              .withMessage(input -> "Is not greater than 2, for input: " + input)
              .or(isEven).orElseThrow(IllegalStateException::new)
              .withMessage(input -> "Is not even, for input: " + input)
              .useConsumer(stubLogger::log)
              .isValidOrThrowCombined()
      )
          .hasMessage("For input: '1', the following problems occurred: 'Is not greater than 2, for input: 1; Is not even, for input: 1'")
          .isInstanceOf(ValidationException.class);
      assertThat(stubLogger.lastLogEventException())
          .isInstanceOf(ValidationException.class)
          .hasMessage("For input: '1', the following problems occurred: 'Is not greater than 2, for input: 1; Is not even, for input: 1'");
      assertThat(stubLogger.lastLogEventMessage())
          .isEqualTo("For input '1', was not valid because: 'For input: '1', the following problems occurred: 'Is not greater than 2, for input: 1; Is not even, for input: 1''");
    }

    @Test
    void usingCustomMessageAndNoCustomExceptionAndConsumerWillThrowValidationException() {
      assertThatThrownBy(() ->
          forInput(1)
              .couldSatisfy(isEven)
              .orThrowExceptionWith(input -> "Is not even, for input: " + input)
              .or(isGreaterThan2)
              .orThrowExceptionWith(input -> "Is not greater than 2, for input: " + input)
              .useConsumer(stubLogger::log)
              .isValidOrThrowCombined()
      )
          .hasMessage("For input: '1', the following problems occurred: 'Is not even, for input: 1; Is not greater than 2, for input: 1'")
          .isInstanceOf(ValidationException.class);
      assertThat(stubLogger.lastLogEventException())
          .isInstanceOf(ValidationException.class)
          .hasMessage("For input: '1', the following problems occurred: 'Is not even, for input: 1; Is not greater than 2, for input: 1'");
      assertThat(stubLogger.lastLogEventMessage())
          .isEqualTo("For input '1', was not valid because: 'For input: '1', the following problems occurred: 'Is not even, for input: 1; Is not greater than 2, for input: 1''");

      assertThatThrownBy(() ->
          forInput(1)
              .couldSatisfy(isGreaterThan2)
              .orThrowExceptionWith(input -> "Is not greater than 2, for input: " + input)
              .or(isEven)
              .orThrowExceptionWith(input -> "Is not even, for input: " + input)
              .useConsumer(stubLogger::log)
              .isValidOrThrowCombined()
      )
          .hasMessage("For input: '1', the following problems occurred: 'Is not greater than 2, for input: 1; Is not even, for input: 1'")
          .isInstanceOf(ValidationException.class);
      assertThat(stubLogger.lastLogEventException())
          .isInstanceOf(ValidationException.class)
          .hasMessage("For input: '1', the following problems occurred: 'Is not greater than 2, for input: 1; Is not even, for input: 1'");
      assertThat(stubLogger.lastLogEventMessage())
          .isEqualTo("For input '1', was not valid because: 'For input: '1', the following problems occurred: 'Is not greater than 2, for input: 1; Is not even, for input: 1''");
    }
  }
}