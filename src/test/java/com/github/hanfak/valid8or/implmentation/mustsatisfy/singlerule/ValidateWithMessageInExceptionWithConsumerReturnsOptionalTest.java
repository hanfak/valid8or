package com.github.hanfak.valid8or.implmentation.mustsatisfy.singlerule;

import com.github.hanfak.valid8or.implmentation.domain.ValidationException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import testinfrastructure.TestFixtures;

import static com.github.hanfak.valid8or.api.Valid8orMustSatisfyAllRules.forInput;

class ValidateWithMessageInExceptionWithConsumerReturnsOptionalTest extends TestFixtures {
  // TODO: Test where input from lambda arg is not used
  // TODO: Test where input from lambda arg is not used, but variable is
  @Nested
  class ReturnsInputAsOptionalAndNoExceptionThrownOrConsumerUsedWhenInputIsValid {

    @Test
    void usingCustomExceptionWithCustomMessageUsingInputAndConsumer() {
      assertThat(
          forInput(4)
              .mustSatisfy(isEven).orThrow(IllegalStateException::new)
              .withMessage(input -> "Is not even, for input: " + input)
              .thenConsume(stubLogger::log)
              .validateThenReturnOptional()
      ).isPresent().containsInstanceOf(Integer.class).contains(4);
    }

    @Test
    void usingCustomMessageAndNoCustomExceptionAndConsumer() {
      assertThat(
          forInput(4)
              .mustSatisfy(isEven)
              .butWas(input -> "Is not even, for input: " + input)
              .thenConsume(stubLogger::log)
              .validateThenReturnOptional()
      ).isPresent().containsInstanceOf(Integer.class).contains(4);
    }
  }

  @Nested
  class ThrowsAnExceptionAndUsesConsumerWhenInputIsInvalid {

    @Test
    void usingCustomExceptionWithCustomMessageUsingInputAndConsumerThrowsCustomException() {
      assertThatThrownBy(() ->
          forInput(3)
              .mustSatisfy(isEven).orThrow(IllegalStateException::new)
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
              .mustSatisfy(isEven)
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
