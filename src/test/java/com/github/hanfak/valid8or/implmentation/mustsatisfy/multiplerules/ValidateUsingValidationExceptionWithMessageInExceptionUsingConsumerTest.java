package com.github.hanfak.valid8or.implmentation.mustsatisfy.multiplerules;
import com.github.hanfak.valid8or.implmentation.ValidationException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import testinfrastructure.TestFixtures;

import static com.github.hanfak.valid8or.implmentation.Valid8orMustSatisfyAllRules.forInput;

class ValidateUsingValidationExceptionWithMessageInExceptionUsingConsumerTest extends TestFixtures {

  @Nested
  class ReturnsInputAndNoExceptionThrownOrConsumerUsedWhenInputIsValid {

    @Test
    void usingCustomExceptionWithCustomMessageUsingInputAndConsumer() {
      assertThat(
          forInput(4)
              .mustSatisfy(isEven).orThrow(IllegalStateException::new)
              .withMessage(input -> "Is not even, for input: " + input)
              .andSatisfies(isGreaterThan2).orThrow(IllegalStateException::new)
              .withMessage(input -> "Is not greater than 2, for input: " + input)
              .thenConsume(stubLogger::log)
              .validateOrThrowNotify()
      ).isEqualTo(4);

      assertThat(
          forInput(4)
              .mustSatisfy(isGreaterThan2).orThrow(IllegalStateException::new)
              .withMessage(input -> "Is not greater than 2, for input: " + input)
              .andSatisfies(isEven).orThrow(IllegalStateException::new)
              .withMessage(input -> "Is not even, for input: " + input)
              .thenConsume(stubLogger::log)
              .validateOrThrowNotify()
      ).isEqualTo(4);
    }

    @Test
    void usingCustomMessageAndNoCustomExceptionAndConsumer() {
      assertThat(
          forInput(4)
              .mustSatisfy(isEven)
              .butWas(input -> "Is not even, for input: " + input)
              .andSatisfies(isGreaterThan2)
              .butWas(input -> "Is not greater than 2, for input: " + input)
              .thenConsume(stubLogger::log)
              .validateOrThrowNotify()
      ).isEqualTo(4);

      assertThat(
          forInput(4)
              .mustSatisfy(isGreaterThan2)
              .butWas(input -> "Is not greater than 2, for input: " + input)
              .andSatisfies(isEven)
              .butWas(input -> "Is not even, for input: " + input)
              .validateOrThrowNotify()
      ).isEqualTo(4);
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
              .andSatisfies(isGreaterThan2).orThrow(IllegalArgumentException::new)
              .withMessage(input -> "Is not greater than 2, for input: " + input)
              .thenConsume(stubLogger::log)
              .validateOrThrowNotify()
      )
          .hasMessage("For input: '3', the following problems occurred: 'Is not even, for input: 3'")
          .isInstanceOf(ValidationException.class);
      assertThat(stubLogger.lastLogEventException())
          .isInstanceOf(ValidationException.class)
          .hasMessage("For input: '3', the following problems occurred: 'Is not even, for input: 3'");
      assertThat(stubLogger.lastLogEventMessage())
          .isEqualTo("For input '3', was not valid because: 'For input: '3', the following problems occurred: 'Is not even, for input: 3''");

      assertThatThrownBy(() ->
          forInput(2)
              .mustSatisfy(isEven).orThrow(IllegalStateException::new)
              .withMessage(input -> "Is not even, for input: " + input)
              .andSatisfies(isGreaterThan2).orThrow(IllegalArgumentException::new)
              .withMessage(input -> "Is not greater than 2, for input: " + input)
              .thenConsume(stubLogger::log)
              .validateOrThrowNotify()
      )
          .hasMessage("For input: '2', the following problems occurred: 'Is not greater than 2, for input: 2'")
          .isInstanceOf(ValidationException.class);
      assertThat(stubLogger.lastLogEventException())
          .isInstanceOf(ValidationException.class)
          .hasMessage("For input: '2', the following problems occurred: 'Is not greater than 2, for input: 2'");
      assertThat(stubLogger.lastLogEventMessage())
          .isEqualTo("For input '2', was not valid because: 'For input: '2', the following problems occurred: 'Is not greater than 2, for input: 2''");

      assertThatThrownBy(() ->
          forInput(1)
              .mustSatisfy(isEven).orThrow(IllegalStateException::new)
              .withMessage(input -> "Is not even, for input: " + input)
              .andSatisfies(isGreaterThan2).orThrow(IllegalArgumentException::new)
              .withMessage(input -> "Is not greater than 2, for input: " + input)
              .thenConsume(stubLogger::log)
              .validateOrThrowNotify()
      )
          .hasMessage("For input: '1', the following problems occurred: 'Is not even, for input: 1, Is not greater than 2, for input: 1'")
          .isInstanceOf(ValidationException.class);
      assertThat(stubLogger.lastLogEventException())
          .isInstanceOf(ValidationException.class)
          .hasMessage("For input: '1', the following problems occurred: 'Is not even, for input: 1, Is not greater than 2, for input: 1'");
      assertThat(stubLogger.lastLogEventMessage())
          .isEqualTo("For input '1', was not valid because: 'For input: '1', the following problems occurred: 'Is not even, for input: 1, Is not greater than 2, for input: 1''");


      assertThatThrownBy(() ->
          forInput(3)
              .mustSatisfy(isGreaterThan2).orThrow(IllegalArgumentException::new)
              .withMessage(input -> "Is not greater than 2, for input: " + input)
              .andSatisfies(isEven).orThrow(IllegalStateException::new)
              .withMessage(input -> "Is not even, for input: " + input)
              .thenConsume(stubLogger::log)
              .validateOrThrowNotify()
      )
          .hasMessage("For input: '3', the following problems occurred: 'Is not even, for input: 3'")
          .isInstanceOf(ValidationException.class);
      assertThat(stubLogger.lastLogEventException())
          .isInstanceOf(ValidationException.class)
          .hasMessage("For input: '3', the following problems occurred: 'Is not even, for input: 3'");
      assertThat(stubLogger.lastLogEventMessage())
          .isEqualTo("For input '3', was not valid because: 'For input: '3', the following problems occurred: 'Is not even, for input: 3''");

      assertThatThrownBy(() ->
          forInput(2)
              .mustSatisfy(isGreaterThan2).orThrow(IllegalArgumentException::new)
              .withMessage(input -> "Is not greater than 2, for input: " + input)
              .andSatisfies(isEven).orThrow(IllegalStateException::new)
              .withMessage(input -> "Is not even, for input: " + input)
              .thenConsume(stubLogger::log)
              .validateOrThrowNotify()
      )
          .hasMessage("For input: '2', the following problems occurred: 'Is not greater than 2, for input: 2'")
          .isInstanceOf(ValidationException.class);
      assertThat(stubLogger.lastLogEventException())
          .isInstanceOf(ValidationException.class)
          .hasMessage("For input: '2', the following problems occurred: 'Is not greater than 2, for input: 2'");
      assertThat(stubLogger.lastLogEventMessage())
          .isEqualTo("For input '2', was not valid because: 'For input: '2', the following problems occurred: 'Is not greater than 2, for input: 2''");

      assertThatThrownBy(() ->
          forInput(1)
              .mustSatisfy(isGreaterThan2).orThrow(IllegalArgumentException::new)
              .withMessage(input -> "Is not greater than 2, for input: " + input)
              .andSatisfies(isEven).orThrow(IllegalStateException::new)
              .withMessage(input -> "Is not even, for input: " + input)
              .thenConsume(stubLogger::log)
              .validateOrThrowNotify()
      )
          .hasMessage("For input: '1', the following problems occurred: 'Is not greater than 2, for input: 1, Is not even, for input: 1'")
          .isInstanceOf(ValidationException.class);
      assertThat(stubLogger.lastLogEventException())
          .isInstanceOf(ValidationException.class)
          .hasMessage("For input: '1', the following problems occurred: 'Is not greater than 2, for input: 1, Is not even, for input: 1'");
      assertThat(stubLogger.lastLogEventMessage())
          .isEqualTo("For input '1', was not valid because: 'For input: '1', the following problems occurred: 'Is not greater than 2, for input: 1, Is not even, for input: 1''");
    }

    @Test
    void usingCustomMessageAndNoCustomExceptionAndConsumerWillThrowValidationException() {
      assertThatThrownBy(() ->
          forInput(3)
              .mustSatisfy(isEven)
              .butWas(input -> "Is not even, for input: " + input)
              .andSatisfies(isGreaterThan2)
              .butWas(input -> "Is not greater than 2, for input: " + input)
              .thenConsume(stubLogger::log)
              .validateOrThrowNotify()
      )
          .hasMessage("For input: '3', the following problems occurred: 'Is not even, for input: 3'")
          .isInstanceOf(ValidationException.class);
      assertThat(stubLogger.lastLogEventException())
          .isInstanceOf(ValidationException.class)
          .hasMessage("For input: '3', the following problems occurred: 'Is not even, for input: 3'");
      assertThat(stubLogger.lastLogEventMessage())
          .isEqualTo("For input '3', was not valid because: 'For input: '3', the following problems occurred: 'Is not even, for input: 3''");

      assertThatThrownBy(() ->
          forInput(2)
              .mustSatisfy(isEven)
              .butWas(input -> "Is not even, for input: " + input)
              .andSatisfies(isGreaterThan2)
              .butWas(input -> "Is not greater than 2, for input: " + input)
              .thenConsume(stubLogger::log)
              .validateOrThrowNotify()
      )
          .hasMessage("For input: '2', the following problems occurred: 'Is not greater than 2, for input: 2'")
          .isInstanceOf(ValidationException.class);
      assertThat(stubLogger.lastLogEventException())
          .isInstanceOf(ValidationException.class)
          .hasMessage("For input: '2', the following problems occurred: 'Is not greater than 2, for input: 2'");
      assertThat(stubLogger.lastLogEventMessage())
          .isEqualTo("For input '2', was not valid because: 'For input: '2', the following problems occurred: 'Is not greater than 2, for input: 2''");

      assertThatThrownBy(() ->
          forInput(1)
              .mustSatisfy(isEven)
              .butWas(input -> "Is not even, for input: " + input)
              .andSatisfies(isGreaterThan2)
              .butWas(input -> "Is not greater than 2, for input: " + input)
              .thenConsume(stubLogger::log)
              .validateOrThrowNotify()
      )
          .hasMessage("For input: '1', the following problems occurred: 'Is not even, for input: 1, Is not greater than 2, for input: 1'")
          .isInstanceOf(ValidationException.class);
      assertThat(stubLogger.lastLogEventException())
          .isInstanceOf(ValidationException.class)
          .hasMessage("For input: '1', the following problems occurred: 'Is not even, for input: 1, Is not greater than 2, for input: 1'");
      assertThat(stubLogger.lastLogEventMessage())
          .isEqualTo("For input '1', was not valid because: 'For input: '1', the following problems occurred: 'Is not even, for input: 1, Is not greater than 2, for input: 1''");

      assertThatThrownBy(() ->
          forInput(3)
              .mustSatisfy(isGreaterThan2)
              .butWas(input -> "Is not greater than 2, for input: " + input)
              .andSatisfies(isEven)
              .butWas(input -> "Is not even, for input: " + input)
              .thenConsume(stubLogger::log)
              .validateOrThrowNotify()
      )
          .hasMessage("For input: '3', the following problems occurred: 'Is not even, for input: 3'")
          .isInstanceOf(ValidationException.class);
      assertThat(stubLogger.lastLogEventException())
          .isInstanceOf(ValidationException.class)
          .hasMessage("For input: '3', the following problems occurred: 'Is not even, for input: 3'");
      assertThat(stubLogger.lastLogEventMessage())
          .isEqualTo("For input '3', was not valid because: 'For input: '3', the following problems occurred: 'Is not even, for input: 3''");

      assertThatThrownBy(() ->
          forInput(2)
              .mustSatisfy(isGreaterThan2)
              .butWas(input -> "Is not greater than 2, for input: " + input)
              .andSatisfies(isEven)
              .butWas(input -> "Is not even, for input: " + input)
              .thenConsume(stubLogger::log)
              .validateOrThrowNotify()
      )
          .hasMessage("For input: '2', the following problems occurred: 'Is not greater than 2, for input: 2'")
          .isInstanceOf(ValidationException.class);
      assertThat(stubLogger.lastLogEventException())
          .isInstanceOf(ValidationException.class)
          .hasMessage("For input: '2', the following problems occurred: 'Is not greater than 2, for input: 2'");
      assertThat(stubLogger.lastLogEventMessage())
          .isEqualTo("For input '2', was not valid because: 'For input: '2', the following problems occurred: 'Is not greater than 2, for input: 2''");

      assertThatThrownBy(() ->
          forInput(1)
              .mustSatisfy(isGreaterThan2)
              .butWas(input -> "Is not greater than 2, for input: " + input)
              .andSatisfies(isEven)
              .butWas(input -> "Is not even, for input: " + input)
              .thenConsume(stubLogger::log)
              .validateOrThrowNotify()
      )
          .hasMessage("For input: '1', the following problems occurred: 'Is not greater than 2, for input: 1, Is not even, for input: 1'")
          .isInstanceOf(ValidationException.class);
      assertThat(stubLogger.lastLogEventException())
          .isInstanceOf(ValidationException.class)
          .hasMessage("For input: '1', the following problems occurred: 'Is not greater than 2, for input: 1, Is not even, for input: 1'");
      assertThat(stubLogger.lastLogEventMessage())
          .isEqualTo("For input '1', was not valid because: 'For input: '1', the following problems occurred: 'Is not greater than 2, for input: 1, Is not even, for input: 1''");

    }
  }
}
