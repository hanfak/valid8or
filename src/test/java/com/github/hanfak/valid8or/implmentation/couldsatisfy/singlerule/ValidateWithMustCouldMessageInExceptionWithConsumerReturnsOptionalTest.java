package com.github.hanfak.valid8or.implmentation.couldsatisfy.singlerule;

import com.github.hanfak.valid8or.implmentation.ValidationException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import testinfrastructure.TestFixtures;

import java.util.Optional;

import static com.github.hanfak.valid8or.implmentation.Valid8orCouldSatisfyAllRules.forInput;

class ValidateWithMustCouldMessageInExceptionWithConsumerReturnsOptionalTest extends TestFixtures {

  @Nested
  class ReturnsInputAsOptionalAndNoExceptionThrownOrConsumerUsedWhenInputIsValid {

    @Test
    void usingCustomExceptionWithCustomMessageUsingInputAndConsumer() {
      assertThat(
          forInput(4)
              .couldSatisfy(isEven).orThrow(IllegalStateException::new)
              .withMessage(input -> "Is not even, for input: " + input)
              .thenConsume(stubLogger::log)
              .validateThenReturnOptional()
      ).isEqualTo(Optional.of(4));
    }

    @Test
    void usingCustomMessageAndNoCustomExceptionAndConsumer() {
      assertThat(
          forInput(4)
              .couldSatisfy(isEven)
              .butWas(input -> "Is not even, for input: " + input)
              .thenConsume(stubLogger::log)
              .validateThenReturnOptional()
      ).isEqualTo(Optional.of(4));
    }
  }

  @Nested
  class ThrowsAnExceptionAndUsesConsumerWhenInputIsInvalid {

    @Test
    void usingCustomExceptionWithCustomMessageUsingInputAndConsumerThrowsCustomException() {
      assertThatThrownBy(() ->
          forInput(3)
              .couldSatisfy(isEven).orThrow(IllegalStateException::new)
              .withMessage(input -> "Is not even, for input: " + input)
              .thenConsume(stubLogger::log)
              .validateThenReturnOptional()
      )
          .hasMessage("Is not even, for input: 3")
          .isInstanceOf(IllegalStateException.class);
      assertThat(stubLogger.lastLogEventException())
          .isInstanceOf(IllegalStateException.class)
          .hasMessage("Is not even, for input: 3");
      assertThat(stubLogger.lastLogEventMessage())
          .isEqualTo("For input '3', was not valid because: 'Is not even, for input: 3'");
    }

    @Test
    void usingCustomMessageAndNoCustomExceptionAndConsumerWillThrowsValidationException() {
      assertThatThrownBy(() ->
          forInput(3)
              .couldSatisfy(isEven)
              .butWas(input -> "Is not even, for input: " + input)
              .thenConsume(stubLogger::log)
              .validate()
      )
          .hasMessage("Is not even, for input: 3")
          .isInstanceOf(ValidationException.class);
      assertThat(stubLogger.lastLogEventException())
          .isInstanceOf(ValidationException.class)
          .hasMessage("Is not even, for input: 3");
      assertThat(stubLogger.lastLogEventMessage())
          .isEqualTo("For input '3', was not valid because: 'Is not even, for input: 3'");
    }
  }
}
