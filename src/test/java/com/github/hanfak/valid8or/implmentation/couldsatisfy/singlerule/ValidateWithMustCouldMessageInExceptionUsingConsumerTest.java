package com.github.hanfak.valid8or.implmentation.couldsatisfy.singlerule;

import com.github.hanfak.valid8or.implmentation.ValidationException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import testinfrastructure.TestFixtures;

import static com.github.hanfak.valid8or.implmentation.Valid8orCouldSatisfyAllRules.forInput;

class ValidateWithMustCouldMessageInExceptionUsingConsumerTest extends TestFixtures {

  @Nested
  class ReturnsInputAndNoExceptionThrownOrConsumerUsedWhenInputIsValid {

    @Test
    void usingCustomExceptionWithCustomMessageUsingInputAndConsumer() {
      assertThat(
          forInput(4)
              .couldSatisfy(isEven).orElseThrow(IllegalStateException::new)
              .withMessage(input -> "Is not even, for input: " + input)
              .useConsumer(stubLogger::log)
              .isValidOrThrow()
      ).isEqualTo(4);
    }

    @Test
    void usingCustomMessageAndNoCustomExceptionAndConsumer() {
      assertThat(
          forInput(4)
              .couldSatisfy(isEven)
              .orThrowExceptionWith(input -> "Is not even, for input: " + input)
              .useConsumer(stubLogger::log)
              .isValidOrThrow()
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
              .withMessage(input -> "Is not even, for input: " + input)
              .useConsumer(stubLogger::log)
              .isValidOrThrow()
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
    void usingCustomMessageAndNoCustomExceptionAndConsumerWillThrowValidationException() {
      assertThatThrownBy(() ->
          forInput(3)
              .couldSatisfy(isEven)
              .orThrowExceptionWith(input -> "Is not even, for input: " + input)
              .useConsumer(stubLogger::log)
              .isValidOrThrow()
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
