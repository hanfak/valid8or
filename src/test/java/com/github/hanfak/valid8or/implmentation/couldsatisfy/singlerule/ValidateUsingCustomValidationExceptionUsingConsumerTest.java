package com.github.hanfak.valid8or.implmentation.couldsatisfy.singlerule;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import testinfrastructure.CustomException;
import testinfrastructure.TestFixtures;

import static com.github.hanfak.valid8or.implmentation.Valid8orCouldSatisfyAllRules.forInput;
import static java.lang.String.format;

public class ValidateUsingCustomValidationExceptionUsingConsumerTest extends TestFixtures {

  @Nested
  class ReturnsInputAndNoExceptionThrownOrConsumerUsedWhenInputIsValid {

    @Test
    void usingCustomExceptionWithCustomMessageUsingInputAndConsumer() {
      assertThat(
          forInput(4)
              .couldSatisfy(isEven).orElseThrow(IllegalStateException::new)
              .withExceptionMessage(input -> "Is not even, for input: " + input)
              .useConsumer(stubLogger::log)
              .throwNotificationIfNotValid(CustomException::new,
                  (input, errors) -> format("All problems are, for input: %s, with messages: '%s'", input, errors))
      ).isEqualTo(4);
    }

    @Test
    void usingCustomMessageAndNoCustomExceptionAndConsumer() {
      assertThat(
          forInput(4)
              .couldSatisfy(isEven)
              .butWas(input -> "Is not even, for input: " + input)
              .useConsumer(stubLogger::log)
              .throwNotificationIfNotValid(CustomException::new,
                  (input, errors) -> format("All problems are, for input: %s, with messages: '%s'", input, errors))
      ).isEqualTo(4);
    }
  }

  @Nested
  class ThrowsAnExceptionAndUsesConsumerWhenInputIsInvalid {

    @Test
    void usingCustomExceptionWithCustomMessageUsingInputAndConsumerThrowsCustomException() {
      assertThatThrownBy(() ->
          forInput(3)
              .couldSatisfy(isEven).orElseThrow(IllegalStateException::new)
              .withExceptionMessage(input -> "Is not even, for input: " + input)
              .useConsumer(stubLogger::log)
              .throwNotificationIfNotValid(CustomException::new,
                  (input, errors) -> format("All problems are, for input: %s, with messages: '%s'", input, errors))
      )
          .hasMessage("All problems are, for input: 3, with messages: 'Is not even, for input: 3'")
          .isInstanceOf(CustomException.class);
      assertThat(stubLogger.lastLogEventException())
          .isInstanceOf(CustomException.class)
          .hasMessage("All problems are, for input: 3, with messages: 'Is not even, for input: 3'");
      assertThat(stubLogger.lastLogEventMessage())
          .isEqualTo("For input '3', was not valid because: 'All problems are, for input: 3, with messages: 'Is not even, for input: 3''");
    }

    @Test
    void usingCustomMessageAndNoCustomExceptionAndConsumerWillThrowValidationException() {
      assertThatThrownBy(() ->
          forInput(3)
              .couldSatisfy(isEven)
              .butWas(input -> "Is not even, for input: " + input)
              .useConsumer(stubLogger::log)
              .throwNotificationIfNotValid(CustomException::new,
                  (input, errors) -> format("All problems are, for input: %s, with messages: '%s'", input, errors))
      )
          .hasMessage("All problems are, for input: 3, with messages: 'Is not even, for input: 3'")
          .isInstanceOf(CustomException.class);
      assertThat(stubLogger.lastLogEventException())
          .isInstanceOf(CustomException.class)
          .hasMessage("All problems are, for input: 3, with messages: 'Is not even, for input: 3'");
      assertThat(stubLogger.lastLogEventMessage())
          .isEqualTo("For input '3', was not valid because: 'All problems are, for input: 3, with messages: 'Is not even, for input: 3''");
    }
  }
}
