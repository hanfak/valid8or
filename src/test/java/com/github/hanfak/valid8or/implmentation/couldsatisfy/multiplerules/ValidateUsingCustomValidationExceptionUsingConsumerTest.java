package com.github.hanfak.valid8or.implmentation.couldsatisfy.multiplerules;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import testinfrastructure.CustomException;
import testinfrastructure.TestFixtures;

import static com.github.hanfak.valid8or.implmentation.Valid8orCouldSatisfyAllRules.forInput;
import static java.lang.String.format;

class ValidateUsingCustomValidationExceptionUsingConsumerTest extends TestFixtures {

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
              .isValidOrThrowCombined(CustomException::new,
                  (input, errors) -> format("All problems are, for input: %s, with messages: '%s'", input, errors))
      ).isEqualTo(value);

      assertThat(
          forInput(value)
              .couldSatisfy(isGreaterThan2).orElseThrow(IllegalArgumentException::new)
              .withMessage(input -> "Is not greater than 2, for input: " + input)
              .or(isEven).orElseThrow(IllegalStateException::new)
              .withMessage(input -> "Is not even, for input: " + input)
              .useConsumer(stubLogger::log)
              .isValidOrThrowCombined(CustomException::new,
                  (input, errors) -> format("All problems are, for input: %s, with messages: '%s'", input, errors))
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
              .isValidOrThrowCombined(CustomException::new,
                  (input, errors) -> format("All problems are, for input: %s, with messages: '%s'", input, errors))
      ).isEqualTo(value);

      assertThat(
          forInput(value)
              .couldSatisfy(isGreaterThan2)
              .orThrowExceptionWith(input -> "Is not greater than 2, for input: " + input)
              .or(isEven)
              .orThrowExceptionWith(input -> "Is not even, for input: " + input)
              .useConsumer(stubLogger::log)
              .isValidOrThrowCombined(CustomException::new,
                  (input, errors) -> format("All problems are, for input: %s, with messages: '%s'", input, errors))
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
              .isValidOrThrowCombined(CustomException::new,
                  (input, errors) -> format("All problems are, for input: %s, with messages: '%s'", input, errors))
      )
          .hasMessage("All problems are, for input: 1, with messages: 'Is not even, for input: 1; Is not greater than 2, for input: 1'")
          .isInstanceOf(CustomException.class);
      assertThat(stubLogger.lastLogEventException())
          .isInstanceOf(CustomException.class)
          .hasMessage("All problems are, for input: 1, with messages: 'Is not even, for input: 1; Is not greater than 2, for input: 1'");
      assertThat(stubLogger.lastLogEventMessage())
          .isEqualTo("For input '1', was not valid because: 'All problems are, for input: 1, with messages: 'Is not even, for input: 1; Is not greater than 2, for input: 1''");

      assertThatThrownBy(() ->
          forInput(1)
              .couldSatisfy(isGreaterThan2).orElseThrow(IllegalArgumentException::new)
              .withMessage(input -> "Is not greater than 2, for input: " + input)
              .or(isEven).orElseThrow(IllegalStateException::new)
              .withMessage(input -> "Is not even, for input: " + input)
              .useConsumer(stubLogger::log)
              .isValidOrThrowCombined(CustomException::new,
                  (input, errors) -> format("All problems are, for input: %s, with messages: '%s'", input, errors))
      )
          .hasMessage("All problems are, for input: 1, with messages: 'Is not greater than 2, for input: 1; Is not even, for input: 1'")
          .isInstanceOf(CustomException.class);
      assertThat(stubLogger.lastLogEventException())
          .isInstanceOf(CustomException.class)
          .hasMessage("All problems are, for input: 1, with messages: 'Is not greater than 2, for input: 1; Is not even, for input: 1'");
      assertThat(stubLogger.lastLogEventMessage())
          .isEqualTo("For input '1', was not valid because: 'All problems are, for input: 1, with messages: 'Is not greater than 2, for input: 1; Is not even, for input: 1''");
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
              .isValidOrThrowCombined(CustomException::new,
                  (input, errors) -> format("All problems are, for input: %s, with messages: '%s'", input, errors))
      )
          .hasMessage("All problems are, for input: 1, with messages: 'Is not even, for input: 1; Is not greater than 2, for input: 1'")
          .isInstanceOf(CustomException.class);
      assertThat(stubLogger.lastLogEventException())
          .isInstanceOf(CustomException.class)
          .hasMessage("All problems are, for input: 1, with messages: 'Is not even, for input: 1; Is not greater than 2, for input: 1'");
      assertThat(stubLogger.lastLogEventMessage())
          .isEqualTo("For input '1', was not valid because: 'All problems are, for input: 1, with messages: 'Is not even, for input: 1; Is not greater than 2, for input: 1''");

      assertThatThrownBy(() ->
          forInput(1)
              .couldSatisfy(isGreaterThan2)
              .orThrowExceptionWith(input -> "Is not greater than 2, for input: " + input)
              .or(isEven)
              .orThrowExceptionWith(input -> "Is not even, for input: " + input)
              .useConsumer(stubLogger::log)
              .isValidOrThrowCombined(CustomException::new,
                  (input, errors) -> format("All problems are, for input: %s, with messages: '%s'", input, errors))
      )
          .hasMessage("All problems are, for input: 1, with messages: 'Is not greater than 2, for input: 1; Is not even, for input: 1'")
          .isInstanceOf(CustomException.class);
      assertThat(stubLogger.lastLogEventException())
          .isInstanceOf(CustomException.class)
          .hasMessage("All problems are, for input: 1, with messages: 'Is not greater than 2, for input: 1; Is not even, for input: 1'");
      assertThat(stubLogger.lastLogEventMessage())
          .isEqualTo("For input '1', was not valid because: 'All problems are, for input: 1, with messages: 'Is not greater than 2, for input: 1; Is not even, for input: 1''");
    }
  }
}
