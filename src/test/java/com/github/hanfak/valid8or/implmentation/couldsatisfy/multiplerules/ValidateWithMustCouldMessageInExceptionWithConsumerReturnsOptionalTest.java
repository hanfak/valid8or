package com.github.hanfak.valid8or.implmentation.couldsatisfy.multiplerules;

import com.github.hanfak.valid8or.implmentation.ValidationException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import testinfrastructure.TestFixtures;

import java.util.Optional;

import static com.github.hanfak.valid8or.implmentation.Valid8orCouldSatisfyAllRules.forInput;
import static java.lang.String.format;

class ValidateWithMustCouldMessageInExceptionWithConsumerReturnsOptionalTest extends TestFixtures {

  @Nested
  class ReturnsInputAsOptionalAndNoExceptionThrownOrConsumerUsedWhenInputIsValid {

    @ParameterizedTest
    @ValueSource(ints = {4, 3, 2})
    void usingCustomExceptionWithCustomMessageUsingInputAndConsumer(int value) {
      assertThat(
          forInput(value)
              .couldSatisfy(isEven).orElseThrow(IllegalStateException::new)
              .withExceptionMessage(input -> "Is not even, for input: " + input)
              .or(isGreaterThan2).orElseThrow(IllegalArgumentException::new)
              .withExceptionMessage(input -> "Is not greater than 2, for input: " + input)
              .useConsumer(stubLogger::log)
              .throwIfNotValidReturnOptional()
      ).isEqualTo(Optional.of(value));

      assertThat(
          forInput(value)
              .couldSatisfy(isGreaterThan2).orElseThrow(IllegalArgumentException::new)
              .withExceptionMessage(input -> "Is not greater than 2, for input: " + input)
              .or(isEven).orElseThrow(IllegalStateException::new)
              .withExceptionMessage(input -> "Is not even, for input: " + input)
              .useConsumer(stubLogger::log)
              .throwIfNotValidReturnOptional()
      ).isEqualTo(Optional.of(value));
    }

    @ParameterizedTest
    @ValueSource(ints = {4, 3, 2})
    void usingCustomMessageAndNoCustomExceptionAndConsumer(int value) {
      assertThat(
          forInput(value)
              .couldSatisfy(isEven)
              .butWas(input -> "Is not even, for input: " + input)
              .or(isGreaterThan2)
              .butWas(input -> "Is not greater than 2, for input: " + input)
              .useConsumer(stubLogger::log)
              .throwIfNotValidReturnOptional()
      ).isEqualTo(Optional.of(value));

      assertThat(
          forInput(value)
              .couldSatisfy(isGreaterThan2)
              .butWas(input -> "Is not greater than 2, for input: " + input)
              .or(isEven)
              .butWas(input -> "Is not even, for input: " + input)
              .useConsumer(stubLogger::log)
              .throwIfNotValidReturnOptional()
      ).isEqualTo(Optional.of(value));
    }
  }

  @Nested
  class ThrowsAnExceptionAndUsesConsumerWhenInputIsInvalid {

    @Test
    void usingCustomExceptionWithCustomMessageUsingInputAndConsumerThrowsCustomException() {
      assertThatThrownBy(() ->
          forInput(1)
              .couldSatisfy(isEven).orElseThrow(IllegalStateException::new)
              .withExceptionMessage(input -> "Is not even, for input: " + input)
              .or(isGreaterThan2).orElseThrow(IllegalArgumentException::new)
              .withExceptionMessage(input -> "Is not greater than 2, for input: " + input)
              .useConsumer(stubLogger::log)
              .throwIfNotValidReturnOptional()
      )
          .hasMessage("Is not even, for input: 1")
          .isInstanceOf(IllegalStateException.class);
      assertThat(stubLogger.lastLogEventException())
          .isInstanceOf(IllegalStateException.class)
          .hasMessage("Is not even, for input: 1");
      assertThat(stubLogger.lastLogEventMessage())
          .isEqualTo("For input '1', was not valid because: 'Is not even, for input: 1'");


      assertThatThrownBy(() ->
          forInput(1)
              .couldSatisfy(isGreaterThan2).orElseThrow(IllegalArgumentException::new)
              .withExceptionMessage(input -> "Is not greater than 2, for input: " + input)
              .or(isEven).orElseThrow(IllegalStateException::new)
              .withExceptionMessage(input -> "Is not even, for input: " + input)
              .useConsumer(stubLogger::log)
              .throwIfNotValidReturnOptional()
      )
          .hasMessage("Is not greater than 2, for input: 1")
          .isInstanceOf(IllegalArgumentException.class);
      assertThat(stubLogger.lastLogEventException())
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("Is not greater than 2, for input: 1");
      assertThat(stubLogger.lastLogEventMessage())
          .isEqualTo("For input '1', was not valid because: 'Is not greater than 2, for input: 1'");
    }

    @Test
    void usingCustomMessageAndNoCustomExceptionAndConsumerWillThrowsValidationException() {
      assertThatThrownBy(() ->
          forInput(1)
              .couldSatisfy(isEven)
              .butWas(input -> "Is not even, for input: " + input)
              .or(isGreaterThan2)
              .butWas(input -> "Is not greater than 2, for input: " + input)
              .useConsumer(stubLogger::log)
              .throwIfNotValid()
      )
          .hasMessage("Is not even, for input: 1")
          .isInstanceOf(ValidationException.class);
      assertThat(stubLogger.lastLogEventException())
          .isInstanceOf(ValidationException.class)
          .hasMessage("Is not even, for input: 1");
      assertThat(stubLogger.lastLogEventMessage())
          .isEqualTo("For input '1', was not valid because: 'Is not even, for input: 1'");

      assertThatThrownBy(() ->
          forInput(1)
              .couldSatisfy(isGreaterThan2)
              .butWas(input -> "Is not greater than 2, for input: " + input)
              .or(isEven)
              .butWas(input -> "Is not even, for input: " + input)
              .useConsumer(stubLogger::log)
              .throwIfNotValid()
      )
          .hasMessage("Is not greater than 2, for input: 1")
          .isInstanceOf(ValidationException.class);
      assertThat(stubLogger.lastLogEventException())
          .isInstanceOf(ValidationException.class)
          .hasMessage("Is not greater than 2, for input: 1");
      assertThat(stubLogger.lastLogEventMessage())
          .isEqualTo("For input '1', was not valid because: 'Is not greater than 2, for input: 1'");
    }
  }
}
