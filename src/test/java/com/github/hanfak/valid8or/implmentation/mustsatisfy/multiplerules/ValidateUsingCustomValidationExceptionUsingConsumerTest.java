package com.github.hanfak.valid8or.implmentation.mustsatisfy.multiplerules;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import testinfrastructure.CustomException;
import testinfrastructure.TestFixtures;

import static com.github.hanfak.valid8or.implmentation.Valid8orMustSatisfyAllRules.forInput;
import static java.lang.String.format;

class ValidateUsingCustomValidationExceptionUsingConsumerTest extends TestFixtures {

  @Nested
  class ReturnsInputAndNoExceptionThrownOrConsumerUsedWhenInputIsValid {

    @Test
    void usingCustomExceptionWithCustomMessageUsingInputAndConsumer() {
      assertThat(
          forInput(4)
              .mustSatisfy(isEven).orElseThrow(IllegalStateException::new)
              .withExceptionMessage(input -> "Is not even, for input: " + input)
              .and(isGreaterThan2).orElseThrow(IllegalArgumentException::new)
              .withExceptionMessage(input -> "Is not greater than 2, for input: " + input)
              .useConsumer(stubLogger::log)
              .throwNotificationIfNotValid(CustomException::new,
                  (input, errors) -> format("All problems are, for input: %s, with messages: '%s'", input, errors))
      ).isEqualTo(4);

      assertThat(
          forInput(4)
              .mustSatisfy(isGreaterThan2).orElseThrow(IllegalArgumentException::new)
              .withExceptionMessage(input -> "Is not greater than 2, for input: " + input)
              .and(isEven).orElseThrow(IllegalStateException::new)
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
              .mustSatisfy(isEven)
              .orThrowExceptionWith(input -> "Is not even, for input: " + input)
              .and(isGreaterThan2)
              .orThrowExceptionWith(input -> "Is not greater than 2, for input: " + input)
              .useConsumer(stubLogger::log)
              .throwNotificationIfNotValid(CustomException::new,
                  (input, errors) -> format("All problems are, for input: %s, with messages: '%s'", input, errors))
      ).isEqualTo(4);

      assertThat(
          forInput(4)
              .mustSatisfy(isGreaterThan2)
              .orThrowExceptionWith(input -> "Is not greater than 2, for input: " + input)
              .and(isEven)
              .orThrowExceptionWith(input -> "Is not even, for input: " + input)
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
              .mustSatisfy(isEven).orElseThrow(IllegalStateException::new)
              .withExceptionMessage(input -> "Is not even, for input: " + input)
              .and(isGreaterThan2).orElseThrow(IllegalArgumentException::new)
              .withExceptionMessage(input -> "Is not greater than 2, for input: " + input)
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

      assertThatThrownBy(() ->
          forInput(2)
              .mustSatisfy(isEven).orElseThrow(IllegalStateException::new)
              .withExceptionMessage(input -> "Is not even, for input: " + input)
              .and(isGreaterThan2).orElseThrow(IllegalArgumentException::new)
              .withExceptionMessage(input -> "Is not greater than 2, for input: " + input)
              .useConsumer(stubLogger::log)
              .throwNotificationIfNotValid(CustomException::new,
                  (input, errors) -> format("All problems are, for input: %s, with messages: '%s'", input, errors))
      )
          .hasMessage("All problems are, for input: 2, with messages: 'Is not greater than 2, for input: 2'")
          .isInstanceOf(CustomException.class);
      assertThat(stubLogger.lastLogEventException())
          .isInstanceOf(CustomException.class)
          .hasMessage("All problems are, for input: 2, with messages: 'Is not greater than 2, for input: 2'");
      assertThat(stubLogger.lastLogEventMessage())
          .isEqualTo("For input '2', was not valid because: 'All problems are, for input: 2, with messages: 'Is not greater than 2, for input: 2''");

      assertThatThrownBy(() ->
          forInput(1)
              .mustSatisfy(isEven).orElseThrow(IllegalStateException::new)
              .withExceptionMessage(input -> "Is not even, for input: " + input)
              .and(isGreaterThan2).orElseThrow(IllegalArgumentException::new)
              .withExceptionMessage(input -> "Is not greater than 2, for input: " + input)
              .useConsumer(stubLogger::log)
              .throwNotificationIfNotValid(CustomException::new,
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
          forInput(3)
              .mustSatisfy(isGreaterThan2).orElseThrow(IllegalArgumentException::new)
              .withExceptionMessage(input -> "Is not greater than 2, for input: " + input)
              .and(isEven).orElseThrow(IllegalStateException::new)
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

      assertThatThrownBy(() ->
          forInput(2)
              .mustSatisfy(isGreaterThan2).orElseThrow(IllegalArgumentException::new)
              .withExceptionMessage(input -> "Is not greater than 2, for input: " + input)
              .and(isEven).orElseThrow(IllegalStateException::new)
              .withExceptionMessage(input -> "Is not even, for input: " + input)
              .useConsumer(stubLogger::log)
              .throwNotificationIfNotValid(CustomException::new,
                  (input, errors) -> format("All problems are, for input: %s, with messages: '%s'", input, errors))
      )
          .hasMessage("All problems are, for input: 2, with messages: 'Is not greater than 2, for input: 2'")
          .isInstanceOf(CustomException.class);
      assertThat(stubLogger.lastLogEventException())
          .isInstanceOf(CustomException.class)
          .hasMessage("All problems are, for input: 2, with messages: 'Is not greater than 2, for input: 2'");
      assertThat(stubLogger.lastLogEventMessage())
          .isEqualTo("For input '2', was not valid because: 'All problems are, for input: 2, with messages: 'Is not greater than 2, for input: 2''");

      assertThatThrownBy(() ->
          forInput(1)
              .mustSatisfy(isGreaterThan2).orElseThrow(IllegalArgumentException::new)
              .withExceptionMessage(input -> "Is not greater than 2, for input: " + input)
              .and(isEven).orElseThrow(IllegalStateException::new)
              .withExceptionMessage(input -> "Is not even, for input: " + input)
              .useConsumer(stubLogger::log)
              .throwNotificationIfNotValid(CustomException::new,
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
          forInput(3)
              .mustSatisfy(isEven)
              .orThrowExceptionWith(input -> "Is not even, for input: " + input)
              .and(isGreaterThan2)
              .orThrowExceptionWith(input -> "Is not greater than 2, for input: " + input)
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

      assertThatThrownBy(() ->
          forInput(2)
              .mustSatisfy(isEven)
              .orThrowExceptionWith(input -> "Is not even, for input: " + input)
              .and(isGreaterThan2)
              .orThrowExceptionWith(input -> "Is not greater than 2, for input: " + input)
              .useConsumer(stubLogger::log)
              .throwNotificationIfNotValid(CustomException::new,
                  (input, errors) -> format("All problems are, for input: %s, with messages: '%s'", input, errors))
      )
          .hasMessage("All problems are, for input: 2, with messages: 'Is not greater than 2, for input: 2'")
          .isInstanceOf(CustomException.class);
      assertThat(stubLogger.lastLogEventException())
          .isInstanceOf(CustomException.class)
          .hasMessage("All problems are, for input: 2, with messages: 'Is not greater than 2, for input: 2'");
      assertThat(stubLogger.lastLogEventMessage())
          .isEqualTo("For input '2', was not valid because: 'All problems are, for input: 2, with messages: 'Is not greater than 2, for input: 2''");

      assertThatThrownBy(() ->
          forInput(1)
              .mustSatisfy(isEven)
              .orThrowExceptionWith(input -> "Is not even, for input: " + input)
              .and(isGreaterThan2)
              .orThrowExceptionWith(input -> "Is not greater than 2, for input: " + input)
              .useConsumer(stubLogger::log)
              .throwNotificationIfNotValid(CustomException::new,
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
          forInput(3)
              .mustSatisfy(isGreaterThan2)
              .orThrowExceptionWith(input -> "Is not greater than 2, for input: " + input)
              .and(isEven)
              .orThrowExceptionWith(input -> "Is not even, for input: " + input)
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

      assertThatThrownBy(() ->
          forInput(2)
              .mustSatisfy(isGreaterThan2)
              .orThrowExceptionWith(input -> "Is not greater than 2, for input: " + input)
              .and(isEven)
              .orThrowExceptionWith(input -> "Is not even, for input: " + input)
              .useConsumer(stubLogger::log)
              .throwNotificationIfNotValid(CustomException::new,
                  (input, errors) -> format("All problems are, for input: %s, with messages: '%s'", input, errors))
      )
          .hasMessage("All problems are, for input: 2, with messages: 'Is not greater than 2, for input: 2'")
          .isInstanceOf(CustomException.class);
      assertThat(stubLogger.lastLogEventException())
          .isInstanceOf(CustomException.class)
          .hasMessage("All problems are, for input: 2, with messages: 'Is not greater than 2, for input: 2'");
      assertThat(stubLogger.lastLogEventMessage())
          .isEqualTo("For input '2', was not valid because: 'All problems are, for input: 2, with messages: 'Is not greater than 2, for input: 2''");

      assertThatThrownBy(() ->
          forInput(1)
              .mustSatisfy(isGreaterThan2)
              .orThrowExceptionWith(input -> "Is not greater than 2, for input: " + input)
              .and(isEven)
              .orThrowExceptionWith(input -> "Is not even, for input: " + input)
              .useConsumer(stubLogger::log)
              .throwNotificationIfNotValid(CustomException::new,
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