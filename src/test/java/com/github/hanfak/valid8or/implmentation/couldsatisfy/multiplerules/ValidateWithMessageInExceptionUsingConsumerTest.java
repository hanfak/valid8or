package com.github.hanfak.valid8or.implmentation.couldsatisfy.multiplerules;

import com.github.hanfak.valid8or.implmentation.domain.ValidationException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import testinfrastructure.TestFixtures;

import static com.github.hanfak.valid8or.api.Valid8orCouldSatisfyAllRules.forInput;

public class ValidateWithMessageInExceptionUsingConsumerTest extends TestFixtures {

  @Nested
  class ReturnsInputAndNoExceptionThrownOrConsumerUsedWhenInputIsValid {

    @ParameterizedTest
    @ValueSource(ints = {4, 3, 2})
    void usingCustomExceptionWithCustomMessageUsingInputAndConsumer(int value) {
      assertThat(
          forInput(value)
              .couldSatisfy(isEven).orThrow(IllegalStateException::new)
              .withMessage(input -> "Is not even, for input: " + input)
              .orSatisfies(isGreaterThan2).orThrow(IllegalArgumentException::new)
              .withMessage(input -> "Is not greater than 2, for input: " + input)
              .thenConsume(stubLogger::log)
              .validate()
      ).isEqualTo(value);

      assertThat(
          forInput(value)
              .couldSatisfy(isGreaterThan2).orThrow(IllegalArgumentException::new)
              .withMessage(input -> "Is not greater than 2, for input: " + input)
              .orSatisfies(isEven).orThrow(IllegalStateException::new)
              .withMessage(input -> "Is not even, for input: " + input)
              .thenConsume(stubLogger::log)
              .validate()
      ).isEqualTo(value);
    }

    @ParameterizedTest
    @ValueSource(ints = {4, 3, 2})
    void usingCustomMessageAndNoCustomExceptionAndConsumer(int value) {
      assertThat(
          forInput(value)
              .couldSatisfy(isEven)
              .butWas(input -> "Is not even, for input: " + input)
              .orSatisfies(isGreaterThan2)
              .butWas(input -> "Is not greater than 2, for input: " + input)
              .thenConsume(stubLogger::log)
              .validate()
      ).isEqualTo(value);

      assertThat(
          forInput(value)
              .couldSatisfy(isGreaterThan2)
              .butWas(input -> "Is not greater than 2, for input: " + input)
              .orSatisfies(isEven)
              .butWas(input -> "Is not even, for input: " + input)
              .thenConsume(stubLogger::log)
              .validate()
      ).isEqualTo(value);
    }
  }

  @Nested
  class ThrowsAnExceptionAndUsesConsumerWhenInputIsInvalid {

    @Test
    void usingCustomExceptionWithCustomMessageUsingInputAndConsumerThrowsCustomException() {
      assertThatThrownBy(() ->
          forInput(1)
              .couldSatisfy(isEven).orThrow(IllegalStateException::new)
              .withMessage(input -> "Is not even, for input: " + input)
              .orSatisfies(isGreaterThan2).orThrow(IllegalArgumentException::new)
              .withMessage(input -> "Is not greater than 2, for input: " + input)
              .thenConsume(stubLogger::log)
              .validate()
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
              .couldSatisfy(isGreaterThan2).orThrow(IllegalArgumentException::new)
              .withMessage(input -> "Is not greater than 2, for input: " + input)
              .orSatisfies(isEven).orThrow(IllegalStateException::new)
              .withMessage(input -> "Is not even, for input: " + input)
              .thenConsume(stubLogger::log)
              .validate()
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
    void usingCustomMessageAndNoCustomExceptionAndConsumerWillThrowValidationException() {
      assertThatThrownBy(() ->
          forInput(1)
              .couldSatisfy(isEven)
              .butWas(input -> "Is not even, for input: " + input)
              .orSatisfies(isGreaterThan2)
              .butWas(input -> "Is not greater than 2, for input: " + input)
              .thenConsume(stubLogger::log)
              .validate()
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
              .orSatisfies(isEven)
              .butWas(input -> "Is not even, for input: " + input)
              .thenConsume(stubLogger::log)
              .validate()
      )
          .hasMessage("Is not greater than 2, for input: 1")
          .isInstanceOf(ValidationException.class);
      assertThat(stubLogger.lastLogEventException())
          .isInstanceOf(ValidationException.class)
          .hasMessage("Is not greater than 2, for input: 1");
      assertThat(stubLogger.lastLogEventMessage())
          .isEqualTo("For input '1', was not valid because: 'Is not greater than 2, for input: 1'");       }
  }
}
